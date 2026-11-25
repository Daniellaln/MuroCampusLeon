@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
package com.leon.murocampus
import androidx.lifecycle.ViewModelStore
import com.leon.murocampus.data.PostRepository
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import java.io.IOException
import org.junit.*
import org.junit.Assert.*
class PostsViewModelTest {
    private val store=ViewModelStore()
    private class Repo:PostRepository {
        override val sinConexion=kotlinx.coroutines.flow.MutableStateFlow(false)
        var listas=0;var filtro:Int?=null;var creaciones=0;var fallo=false
        var detalleBloqueado=false;var detalleCancelado=false
        val posts=listOf(Post(1,1,"Título","Contenido"))
        override suspend fun listar():Result<List<Post>> { listas++;return if(fallo)Result.failure(IOException()) else Result.success(posts) }
        override suspend fun porUsuario(userId:Int):Result<List<Post>> { filtro=userId;return Result.success(posts.map { it.copy(userId=userId) }) }
        override suspend fun detalle(id:Int):Result<Post> {
            if(detalleBloqueado)try { awaitCancellation() } finally { detalleCancelado=true }
            return if(fallo)Result.failure(IOException()) else Result.success(posts.first().copy(id=id))
        }
        override suspend fun comentarios(id:Int)=Result.success((1..5).map { Comentario(id,it,"C","a@test.test","Texto") })
        override suspend fun eliminar(id:Int)=Result.success(200)
        override suspend fun editar(id:Int,cambios:EditarPost)=Result.success(Post(1,id,cambios.title,cambios.body))
        override suspend fun crear(nuevo:NuevoPost):Result<Post> { creaciones++;return if(fallo)Result.failure(IOException()) else Result.success(Post(nuevo.userId,101,nuevo.title,nuevo.body)) }
    }
    private fun testVm(block:suspend TestScope.(Repo,PostsViewModel)->Unit)=runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try { val repo=Repo();val vm=PostsViewModel(repo);store.put("vm",vm);block(repo,vm) }
        finally { store.clear();Dispatchers.resetMain() }
    }
    @Test fun iniciaCargandoYDespuesExito()=testVm { repo,vm ->
        assertEquals(UiState.Cargando,vm.lista.value);runCurrent()
        assertEquals(repo.posts,(vm.lista.value as UiState.Exito).datos);assertEquals(1,repo.listas)
    }
    @Test fun fallaYReintentaSinRecrearViewModel()=testVm { repo,vm ->
        repo.fallo=true;runCurrent();assertTrue(vm.lista.value is UiState.Error)
        repo.fallo=false;vm.cargar();runCurrent();assertTrue(vm.lista.value is UiState.Exito);assertEquals(2,repo.listas)
    }
    @Test fun filtrarLlamaAlRepositoryYTodosRegresaAListar()=testVm { repo,vm ->
        runCurrent();vm.filtrar(2);runCurrent();assertEquals(2,repo.filtro);assertEquals(2,vm.usuarioFiltro.value)
        assertTrue((vm.lista.value as UiState.Exito).datos.all { it.userId==2 })
        vm.cargar();runCurrent();assertEquals(2,repo.filtro)
        vm.filtrar(null);runCurrent();assertNull(vm.usuarioFiltro.value);assertEquals(2,repo.listas)
    }
    @Test fun detalleTieneCincoComentariosYVolverNoRecargaLista()=testVm { repo,vm ->
        runCurrent();vm.abrir(1);runCurrent()
        assertEquals(5,(vm.detalle.value as UiState.Exito).datos.comentarios.size)
        vm.cerrarDetalle();runCurrent();assertEquals(1,repo.listas)
    }
    @Test fun volverCancelaLaPeticionPendienteDelDetalle()=testVm { repo,vm ->
        runCurrent();repo.detalleBloqueado=true;vm.abrir(1);runCurrent();vm.cerrarDetalle();runCurrent()
        assertTrue(repo.detalleCancelado);assertFalse(vm.detalle.value is UiState.Error)
    }
    @Test fun crearValidaLimpiaEspaciosYConservaUserIdUno()=testVm { repo,vm ->
        runCurrent();vm.publicar(" ","Texto");runCurrent();assertEquals(0,repo.creaciones)
        vm.publicar(" Título "," Texto ");runCurrent()
        assertEquals(Post(1,101,"Título","Texto"),(vm.creacion.value as UiState.Exito).datos)
    }
    @Test fun creacionFallidaSePuedeReintentar()=testVm { repo,vm ->
        runCurrent();repo.fallo=true;vm.publicar("Título","Texto");runCurrent();assertTrue(vm.creacion.value is UiState.Error)
        repo.fallo=false;vm.publicar("Título","Texto");runCurrent();assertTrue(vm.creacion.value is UiState.Exito)
    }
}
