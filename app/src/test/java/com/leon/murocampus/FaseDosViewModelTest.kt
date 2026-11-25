@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
package com.leon.murocampus
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.SavedStateHandle
import com.leon.murocampus.data.*
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import java.io.IOException
class FaseDosViewModelTest {
    private class Repo:PostRepository {
        override val sinConexion=MutableStateFlow(false)
        val posts=(1..45).map { Post((it-1)/10+1,it,"Título $it","Texto") }
        val llamadas=mutableListOf<Pair<Int,Int?>>();var falloMas=false;var ediciones=0;var borrados=0;var esperar=false;var cancelado=false;var demoraCancelacion=0L
        override suspend fun pagina(inicio:Int,limite:Int,usuario:Int?):Result<List<Post>> {
            llamadas+=inicio to usuario
            if(esperar && inicio==20)try { awaitCancellation() } finally {
                withContext(NonCancellable) { delay(demoraCancelacion) };cancelado=true
            }
            if(falloMas && inicio>0)return Result.failure(IOException())
            return Result.success(posts.filter { usuario==null || it.userId==usuario }.drop(inicio).take(limite))
        }
        override suspend fun listar()=Result.success(posts)
        override suspend fun porUsuario(userId:Int)=Result.success(posts.filter { it.userId==userId })
        override suspend fun detalle(id:Int)=Result.success(posts.first { it.id==id })
        override suspend fun comentarios(id:Int)=Result.success(emptyList<Comentario>())
        override suspend fun crear(nuevo:NuevoPost)=Result.success(Post(1,101,nuevo.title,nuevo.body))
        override suspend fun editar(id:Int,cambios:EditarPost):Result<Post> { ediciones++;return Result.success(Post(1,id,cambios.title,cambios.body)) }
        override suspend fun eliminar(id:Int):Result<Int> { borrados++;return Result.success(200) }
    }
    private fun prueba(block:suspend TestScope.(Repo,PostsViewModel)->Unit)=runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler));val store=ViewModelStore()
        try { val repo=Repo();val vm=PostsViewModel(repo);store.put("vm",vm);runCurrent();block(repo,vm) }
        finally { store.clear();Dispatchers.resetMain() }
    }
    @Test fun busquedaNoSolicitaRedYRespetaElFiltro()=prueba { repo,vm ->
        vm.buscar("TÍTULO 2");runCurrent();assertEquals(1,repo.llamadas.size)
        assertEquals(listOf(2,20),(vm.visibles.value as UiState.Exito).datos.map { it.id })
        assertEquals(1,repo.llamadas.size)
        vm.filtrar(2);runCurrent();assertTrue((vm.visibles.value as UiState.Exito).datos.all { it.userId==2 });assertEquals(2,repo.llamadas.size)
    }
    @Test fun paginaDeVeinteFinYNoDuplicados()=prueba { repo,vm ->
        assertEquals(20,(vm.lista.value as UiState.Exito).datos.size)
        vm.cargarMas();runCurrent();assertEquals(40,(vm.lista.value as UiState.Exito).datos.size)
        vm.cargarMas();runCurrent();assertEquals(45,(vm.lista.value as UiState.Exito).datos.size);assertFalse(vm.hayMas.value)
        vm.cargarMas();runCurrent();assertEquals(listOf(0,20,40),repo.llamadas.map { it.first })
    }
    @Test fun reintentoDePaginaConservaLoAnteriorYRepiteElMismoInicio()=prueba { repo,vm ->
        repo.falloMas=true;vm.cargarMas();runCurrent();assertNotNull(vm.errorMas.value);assertEquals(20,(vm.lista.value as UiState.Exito).datos.size)
        repo.falloMas=false;vm.cargarMas();runCurrent();assertNull(vm.errorMas.value)
        val ids=(vm.lista.value as UiState.Exito).datos.map { it.id };assertEquals(40,ids.size);assertEquals(ids.distinct(),ids)
        assertEquals(listOf(0,20,20),repo.llamadas.map { it.first })
    }
    @Test fun cambiarUsuarioCancelaLaPaginaAnteriorYReiniciaOffset()=prueba { repo,vm ->
        repo.esperar=true;vm.cargarMas();runCurrent();vm.filtrar(2);runCurrent()
        assertTrue(repo.cancelado);assertEquals(0 to 2,repo.llamadas.last());assertFalse(vm.cargandoMas.value)
        assertEquals(10,(vm.lista.value as UiState.Exito).datos.size)
    }
    @Test fun finalizarUnaPaginaCanceladaNoApagaLaCargaDeLaNuevaLista()=prueba { repo,vm ->
        repo.esperar=true;repo.demoraCancelacion=100
        vm.cargarMas();runCurrent();vm.cargar();runCurrent()
        vm.cargarMas();runCurrent();assertTrue(vm.cargandoMas.value)
        advanceTimeBy(100);runCurrent()
        assertTrue(repo.cancelado);assertTrue(vm.cargandoMas.value)
        repo.demoraCancelacion=0
    }
    @Test fun editarValidaEspaciosYExponeLaRespuesta()=prueba { repo,vm ->
        vm.editar(1," ","Texto");runCurrent();assertEquals(0,repo.ediciones)
        vm.editar(1," Editado "," Texto ");runCurrent();assertEquals("Editado",(vm.edicion.value as UiState.Exito).datos.title)
        vm.cerrarEdicion();assertNull(vm.edicion.value)
    }
    @Test fun eliminarExponeCodigoYSeLimpiaAlCerrarDetalle()=prueba { repo,vm ->
        vm.eliminar(1);runCurrent();assertEquals(200,(vm.eliminacion.value as UiState.Exito).datos);assertEquals(1,repo.borrados)
        vm.cerrarDetalle();assertNull(vm.eliminacion.value)
    }
    @Test fun reconstruirConEstadoGuardadoRestauraFiltroYBusquedaDesdeLaPrimeraPagina()=runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler));val store=ViewModelStore()
        try {
            val estado=SavedStateHandle();val vm=PostsViewModel(Repo(),savedStateHandle=estado)
            store.put("anterior",vm);runCurrent()
            vm.filtrar(2);vm.buscar("Título 12");runCurrent()
            val copiaEstado=estado.keys().associateWith { estado.get<Any?>(it) }
            store.clear()
            val repoRestaurado=Repo()
            val restaurado=PostsViewModel(repoRestaurado,savedStateHandle=SavedStateHandle(copiaEstado))
            store.put("restaurado",restaurado)
            assertEquals(2,restaurado.usuarioFiltro.value)
            assertEquals("Título 12",restaurado.busqueda.value)
            runCurrent()
            assertEquals(listOf(0 to 2),repoRestaurado.llamadas)
            assertEquals(listOf(12),(restaurado.visibles.value as UiState.Exito).datos.map { it.id })
        } finally { store.clear();Dispatchers.resetMain() }
    }
}
