package com.leon.murocampus
import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.ViewModelStore
import com.leon.murocampus.data.PostRepository
import com.leon.murocampus.model.*
import com.leon.murocampus.ui.*
import com.leon.murocampus.ui.theme.MuroCampusTheme
import com.leon.murocampus.viewmodel.PostsViewModel
import kotlinx.coroutines.delay
import java.io.IOException
import org.junit.*
import org.junit.Assert.*
class MuroCampusTest {
    @get:Rule val ui=createComposeRule()
    private val store=ViewModelStore()
    private class Repo:PostRepository {
        override val sinConexion=kotlinx.coroutines.flow.MutableStateFlow(false)
        var fallo=false;var listas=0;var filtro:Int?=null;var enviados=0
        val posts=(1..100).map { Post((it-1)/10+1,it,"Publicación $it","Contenido de práctica $it") }
        override suspend fun listar():Result<List<Post>> { listas++;delay(40);return if(fallo)Result.failure(IOException()) else Result.success(posts) }
        override suspend fun detalle(id:Int):Result<Post> { delay(40);return if(fallo)Result.failure(IOException()) else Result.success(posts.first { it.id==id }) }
        override suspend fun comentarios(id:Int):Result<List<Comentario>> { delay(40);return Result.success((1..5).map { Comentario(id,it,"Respuesta","correo$it@campus.test","Comentario $it") }) }
        override suspend fun porUsuario(userId:Int):Result<List<Post>> { filtro=userId;delay(40);return Result.success(posts.filter { it.userId==userId }) }
        override suspend fun eliminar(id:Int)=Result.success(200)
        override suspend fun editar(id:Int,cambios:EditarPost)=Result.success(Post(1,id,cambios.title,cambios.body))
        override suspend fun crear(nuevo:NuevoPost):Result<Post> { enviados++;delay(40);return if(fallo)Result.failure(IOException()) else Result.success(Post(nuevo.userId,101,nuevo.title,nuevo.body)) }
    }
    private fun mostrar(repo:Repo=Repo()):Pair<Repo,PostsViewModel> {
        val vm=PostsViewModel(repo);store.put("vm",vm)
        ui.setContent { MuroCampusTheme { MuroCampusApp(vm) } };return repo to vm
    }
    @After fun cerrar() { ui.runOnIdle { store.clear() } }
    private fun esperar(vm:PostsViewModel) { ui.waitUntil(5000) { vm.lista.value is UiState.Exito } }
    @Test fun listaCargaLosCienPosts() {
        val (_,vm)=mostrar();esperar(vm);assertEquals(100,(vm.lista.value as UiState.Exito).datos.size)
        ui.onNodeWithText("Publicación 1").assertIsDisplayed()
    }
    @Test fun detalleYRegresoMantienenLaLista() {
        val (repo,vm)=mostrar();esperar(vm);ui.onNodeWithText("Publicación 1").performClick()
        ui.waitUntil(5000) { vm.detalle.value is UiState.Exito }
        ui.onNodeWithText("Comentarios (5)").assertIsDisplayed();ui.onNodeWithContentDescription("Volver").performClick()
        ui.onNodeWithText("MuroCampus").assertIsDisplayed();assertEquals(1,repo.listas)
    }
    @Test fun filtroUsuarioDosYTODOSUsanRepository() {
        val (repo,vm)=mostrar();esperar(vm);ui.onAllNodesWithText("Usuario 2",useUnmergedTree=true).onFirst().performClick()
        ui.waitUntil(5000) { vm.lista.value is UiState.Exito && vm.usuarioFiltro.value==2 }
        assertEquals(2,repo.filtro);assertEquals(10,(vm.lista.value as UiState.Exito).datos.size)
        ui.onNodeWithText("Todos").performClick();ui.waitUntil { (vm.lista.value as? UiState.Exito)?.datos?.size==100 }
    }
    @Test fun formularioDeshabilitaVaciosYLimpiaTrasExito() {
        val (_,vm)=mostrar();esperar(vm);ui.onNodeWithContentDescription("Crear publicación").performClick()
        ui.onNodeWithText("Publicar").assertIsNotEnabled()
        ui.onNodeWithText("Título").performTextInput("Aviso de Daniella")
        ui.onNodeWithText("Contenido").performTextInput("Encuentro en el campus")
        ui.onNodeWithText("Publicar").performScrollTo().performClick();ui.waitUntil { vm.creacion.value is UiState.Exito }
        ui.onNodeWithText("Publicado con id 101").performScrollTo().assertIsDisplayed()
        ui.onNodeWithText("Título").assertTextEquals("Título","")
        ui.onNodeWithText("Contenido").assertTextEquals("Contenido","")
    }
    @Test fun errorDePublicacionConservaElTexto() {
        val (repo,vm)=mostrar();esperar(vm);ui.onNodeWithContentDescription("Crear publicación").performClick()
        ui.onNodeWithText("Título").performTextInput("Mi título");ui.onNodeWithText("Contenido").performTextInput("Mi contenido")
        ui.runOnIdle { repo.fallo=true };ui.onNodeWithText("Publicar").performScrollTo().performClick()
        ui.waitUntil { vm.creacion.value is UiState.Error }
        ui.onNodeWithText("Título").assertTextContains("Mi título")
        ui.onNodeWithText("Contenido").assertTextContains("Mi contenido")
        ui.runOnIdle { repo.fallo=false };ui.onNodeWithText("Reintentar").performScrollTo().performClick()
        ui.waitUntil { vm.creacion.value is UiState.Exito };assertEquals(2,repo.enviados)
    }
    @Test fun errorListaSeRecuperaAlReintentar() {
        val repo=Repo().apply { fallo=true };val (_,vm)=mostrar(repo)
        ui.waitUntil { vm.lista.value is UiState.Error };ui.onNodeWithText("Reintentar").assertIsDisplayed()
        ui.runOnIdle { repo.fallo=false };ui.onNodeWithText("Reintentar").performClick();esperar(vm)
    }
    @Test fun compartirIncluyeTituloTextoYEnlaceSinEnviarNada() {
        val post=Post(1,7,"Aviso","Texto de práctica");val intent=intentCompartir(post)
        assertEquals(Intent.ACTION_SEND,intent.action);assertEquals("text/plain",intent.type)
        assertEquals(post.title,intent.getStringExtra(Intent.EXTRA_SUBJECT))
        val cuerpo=intent.getStringExtra(Intent.EXTRA_TEXT)!!
        assertTrue(cuerpo.contains(post.body));assertTrue(cuerpo.contains("https://jsonplaceholder.typicode.com/posts/7"))
    }
}
