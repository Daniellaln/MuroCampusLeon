@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
package com.leon.murocampus

import androidx.lifecycle.ViewModelStore
import com.leon.murocampus.data.*
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
import java.io.IOException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

class AutoresViewModelTest {
    private class Posts:PostRepository {
        override val sinConexion=MutableStateFlow(false)
        override val origenDetalle=MutableStateFlow(false)
        override val origenComentarios=MutableStateFlow(false)
        override val avisoCache=MutableStateFlow<String?>(null)
        var esperarComentarios=false
        var falloComentarios=false
        var comentariosCancelados=false
        var llamadasComentarios=0
        var llamadasDetalle=0
        override suspend fun pagina(inicio:Int,limite:Int,usuario:Int?)=Result.success(listOf(Post(1,1,"Título","Texto")))
        override suspend fun listar()=pagina(0,20,null)
        override suspend fun porUsuario(userId:Int)=pagina(0,20,userId)
        override suspend fun detalle(id:Int):Result<Post> { llamadasDetalle++;return Result.success(Post(1,id,"Título","Texto")) }
        override suspend fun comentarios(id:Int):Result<List<Comentario>> {
            llamadasComentarios++
            if(esperarComentarios)try { awaitCancellation() } finally { comentariosCancelados=true }
            return if(falloComentarios)Result.failure(IOException()) else Result.success(listOf(Comentario(id,1,"Asunto","a@example.test","Respuesta")))
        }
        override suspend fun editar(id:Int,cambios:EditarPost)=Result.success(Post(1,id,cambios.title,cambios.body))
        override suspend fun eliminar(id:Int)=Result.success(200)
        override suspend fun crear(nuevo:NuevoPost)=Result.success(Post(1,101,nuevo.title,nuevo.body))
    }
    private class Autores:UsuarioRepository {
        var esperar=false
        var fallo=false
        var cancelado=false
        var llamadas=0
        val usuarios=listOf(Usuario(1,"Leanne Graham","Bret","leanne@example.test"))
        override suspend fun listar():Result<List<Usuario>> {
            llamadas++
            if(esperar)try { awaitCancellation() } finally { cancelado=true }
            return if(fallo)Result.failure(IOException()) else Result.success(usuarios)
        }
        override suspend fun usuario(id:Int)=Result.success(usuarios.first())
    }
    private fun prueba(block:suspend TestScope.(Posts,Autores,PostsViewModel)->Unit)=runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val store=ViewModelStore()
        try {
            val posts=Posts();val autores=Autores();val vm=PostsViewModel(posts,autores)
            store.put("vm",vm);block(posts,autores,vm)
        } finally { store.clear();Dispatchers.resetMain() }
    }

    @Test fun autoresLentosNoBloqueanLaListaYSePuedenReintentar()=prueba { _,repo,vm ->
        repo.esperar=true;runCurrent()
        assertTrue(vm.lista.value is UiState.Exito)
        assertEquals(UiState.Cargando,vm.estadoAutores.value)
        repo.esperar=false;vm.recargarAutores();runCurrent()
        assertTrue(repo.cancelado)
        assertEquals("Leanne Graham",vm.autores.value[1]?.name)
        assertTrue(vm.estadoAutores.value is UiState.Exito)
    }

    @Test fun falloAutoresConservaLosConocidosYElReintentoRecuperaEstado()=prueba { _,repo,vm ->
        runCurrent();repo.fallo=true;vm.recargarAutores();runCurrent()
        assertTrue(vm.estadoAutores.value is UiState.Error)
        assertEquals(repo.usuarios.associateBy(Usuario::id),vm.autores.value)
        repo.fallo=false;vm.recargarAutores();runCurrent()
        assertTrue(vm.estadoAutores.value is UiState.Exito)
        assertEquals(3,repo.llamadas)
    }

    @Test fun detalleSeLeeMientrasLosComentariosCarganYVolverLosCancela()=prueba { posts,_,vm ->
        runCurrent();posts.esperarComentarios=true;vm.abrir(7);runCurrent()
        assertEquals(7,(vm.detalle.value as UiState.Exito).datos.post.id)
        assertTrue((vm.detalle.value as UiState.Exito).datos.comentarios.isEmpty())
        assertTrue(vm.cargandoComentarios.value)
        vm.cerrarDetalle();runCurrent()
        assertTrue(posts.comentariosCancelados)
        assertFalse(vm.cargandoComentarios.value)
        assertNull(vm.errorComentarios.value)
    }

    @Test fun falloComentariosConservaPublicacionYReintentaSoloComentarios()=prueba { posts,_,vm ->
        runCurrent();posts.falloComentarios=true;vm.abrir(1);runCurrent()
        assertTrue(vm.detalle.value is UiState.Exito)
        assertNotNull(vm.errorComentarios.value)
        assertFalse(vm.cargandoComentarios.value)
        posts.falloComentarios=false;vm.reintentarComentarios(1);runCurrent()
        assertNull(vm.errorComentarios.value)
        assertEquals(1,(vm.detalle.value as UiState.Exito).datos.comentarios.size)
        assertEquals(2,posts.llamadasComentarios)
        vm.reintentarComentarios(99);runCurrent()
        assertEquals(2,posts.llamadasComentarios)
    }
    @Test fun guardadoSeAbreDeInmediatoSinRedYCancelaElDetalleAnterior()=prueba { posts,_,vm ->
        runCurrent();posts.esperarComentarios=true;vm.abrir(1);runCurrent()
        val guardado=Post(2,29,"Lectura guardada","El texto conservado en este dispositivo")
        vm.abrirGuardado(guardado)
        assertEquals(guardado,(vm.detalle.value as UiState.Exito).datos.post)
        assertFalse(vm.cargandoComentarios.value)
        runCurrent();assertTrue(posts.comentariosCancelados)
        assertEquals(1,posts.llamadasDetalle);assertEquals(1,posts.llamadasComentarios)
        assertEquals(guardado,(vm.detalle.value as UiState.Exito).datos.post)
    }
    @Test fun procedenciaDelDetalleYAdvertenciaDeCacheSeExponenSinConfundirLaLista()=prueba { posts,_,vm ->
        runCurrent();posts.sinConexion.value=true;runCurrent()
        assertTrue(vm.sinConexion.value);assertFalse(vm.detalleSinConexion.value)
        posts.origenDetalle.value=true;runCurrent();assertTrue(vm.detalleSinConexion.value)
        posts.origenDetalle.value=false;posts.origenComentarios.value=true;runCurrent()
        assertTrue(vm.detalleSinConexion.value)
        posts.origenComentarios.value=false;posts.avisoCache.value="No se guardó la copia";runCurrent()
        assertFalse(vm.detalleSinConexion.value);assertEquals("No se guardó la copia",vm.avisoCache.value)
    }
}
