package com.leon.murocampus
import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.ViewModelStore
import com.leon.murocampus.data.PostRepository
import com.leon.murocampus.data.GuardadosPreferencesRepository
import com.leon.murocampus.viewmodel.GuardadosViewModel
import androidx.test.core.app.ApplicationProvider
import android.content.Context
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
    private val prefName="ui_muro_"+java.util.UUID.randomUUID().toString()
    private class Repo:PostRepository {
        override val sinConexion=kotlinx.coroutines.flow.MutableStateFlow(false)
        var fallo=false;var listas=0;var filtro:Int?=null;var enviados=0
        val posts=(1..100).map { Post((it-1)/10+1,it,"Publicación $it","Contenido de práctica $it") }
        override suspend fun pagina(inicio:Int,limite:Int,usuario:Int?):Result<List<Post>> = (if(usuario==null)listar() else porUsuario(usuario)).map { it.drop(inicio).take(limite) }
        override suspend fun listar():Result<List<Post>> { listas++;delay(40);return if(fallo)Result.failure(IOException()) else Result.success(posts) }
        override suspend fun detalle(id:Int):Result<Post> { delay(40);return if(fallo)Result.failure(IOException()) else Result.success(posts.first { it.id==id }) }
        override suspend fun comentarios(id:Int):Result<List<Comentario>> { delay(40);return Result.success((1..5).map { Comentario(id,it,"Respuesta","correo$it@campus.test","Comentario $it") }) }
        override suspend fun porUsuario(userId:Int):Result<List<Post>> { filtro=userId;delay(40);return Result.success(posts.filter { it.userId==userId }) }
        override suspend fun eliminar(id:Int)=Result.success(200)
        override suspend fun editar(id:Int,cambios:EditarPost)=Result.success(Post(1,id,cambios.title,cambios.body))
        override suspend fun crear(nuevo:NuevoPost):Result<Post> { enviados++;delay(40);return if(fallo)Result.failure(IOException()) else Result.success(Post(nuevo.userId,101,nuevo.title,nuevo.body)) }
    }
    private fun mostrar(repo:Repo=Repo(),guardados:GuardadosViewModel?=null):Pair<Repo,PostsViewModel> {
        val vm=PostsViewModel(repo);store.put("vm",vm)
        ui.setContent { MuroCampusTheme { MuroCampusApp(vm,guardados) } };return repo to vm
    }
    @After fun cerrar() { ui.runOnIdle { store.clear() };ApplicationProvider.getApplicationContext<Context>().deleteSharedPreferences(prefName) }
    private fun esperar(vm:PostsViewModel) { ui.waitUntil(5000) { vm.lista.value is UiState.Exito } }
    @Test fun listaCargaLaPrimeraPagina() {
        val (_,vm)=mostrar();esperar(vm);assertEquals(20,(vm.lista.value as UiState.Exito).datos.size)
        ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Publicación 1"));ui.onNodeWithText("Publicación 1").assertIsDisplayed()
    }
    @Test fun detalleYRegresoMantienenLaLista() {
        val (repo,vm)=mostrar();esperar(vm);ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Publicación 1"));ui.onNodeWithText("Publicación 1").performClick()
        ui.waitUntil(5000) { vm.detalle.value is UiState.Exito && !vm.cargandoComentarios.value }
        ui.onNodeWithText("Comentarios (5)").assertIsDisplayed();ui.onNodeWithContentDescription("Volver").performClick()
        ui.onNodeWithText("MuroCampus").assertIsDisplayed();assertEquals(1,repo.listas)
    }
    @Test fun filtroUsuarioDosYTODOSUsanRepository() {
        val (repo,vm)=mostrar();esperar(vm);ui.onNodeWithContentDescription("Filtrar por usuario").performClick();ui.onAllNodesWithText("Usuario 2").onLast().performClick()
        ui.waitUntil(5000) { vm.lista.value is UiState.Exito && vm.usuarioFiltro.value==2 }
        assertEquals(2,repo.filtro);assertEquals(10,(vm.lista.value as UiState.Exito).datos.size)
        ui.onNodeWithContentDescription("Filtrar por usuario").performClick();ui.onNodeWithText("Todas las personas").performClick();ui.waitUntil { (vm.lista.value as? UiState.Exito)?.datos?.size==20 }
    }
    @Test fun formularioDeshabilitaVaciosYLimpiaTrasExito() {
        val (_,vm)=mostrar();esperar(vm);ui.onNodeWithText("Publicar").performClick()
        ui.onNodeWithText("Publicar").assertIsNotEnabled()
        ui.onNodeWithText("Título").performTextInput("Aviso de Daniella")
        ui.onNodeWithText("Contenido").performTextInput("Encuentro en el campus")
        ui.onNodeWithText("Publicar").performScrollTo().performClick();ui.waitUntil { vm.creacion.value is UiState.Exito }
        ui.onNodeWithText("Publicado con id 101").performScrollTo().assertIsDisplayed()
        ui.onNodeWithText("Título").assertTextEquals("Título","")
        ui.onNodeWithText("Contenido").assertTextEquals("Contenido","")
    }
    @Test fun errorDePublicacionConservaElTexto() {
        val (repo,vm)=mostrar();esperar(vm);ui.onNodeWithText("Publicar").performClick()
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
    @Test fun volverDelDetalleConservaLaPosicionDeLectura() {
        val (repo,vm)=mostrar();esperar(vm)
        ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Publicación 20"))
        ui.onNodeWithText("Publicación 20").performClick();ui.waitUntil { vm.detalle.value is UiState.Exito && !vm.cargandoComentarios.value }
        ui.onNodeWithContentDescription("Volver").performClick()
        ui.onNodeWithText("Publicación 20").assertIsDisplayed();assertEquals(1,repo.listas)
    }
    @Test fun buscarNoLlamaDeNuevoALaRedYSePuedeLimpiar() {
        val (repo,vm)=mostrar();esperar(vm)
        ui.onNodeWithContentDescription("Buscar publicaciones").performClick();ui.onNodeWithText("Buscar por título").performTextInput("ZZZ")
        ui.waitUntil { (vm.visibles.value as? UiState.Exito)?.datos?.isEmpty()==true }
        assertEquals(1,repo.listas)
        ui.onNodeWithContentDescription("Limpiar búsqueda").performClick()
        ui.waitUntil { (vm.visibles.value as? UiState.Exito)?.datos?.size==20 }
    }
    @Test fun editarMuestraLaRespuestaSinSustituirElOriginalDelServidor() {
        val (_,vm)=mostrar();esperar(vm)
        ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Publicación 1"));ui.onNodeWithText("Publicación 1").performClick()
        ui.waitUntil { vm.detalle.value is UiState.Exito && !vm.cargandoComentarios.value }
        ui.onNodeWithContentDescription("Opciones de publicación").performClick();ui.onNodeWithText("Editar publicación").performClick()
        ui.onNodeWithText("Título").performTextReplacement("Editado")
        ui.onNodeWithText("Guardar cambios").performScrollTo().performClick();ui.waitUntil { vm.edicion.value is UiState.Exito }
        assertEquals("Editado",(vm.edicion.value as UiState.Exito).datos.title)
        ui.onNodeWithContentDescription("Volver").performClick();assertEquals("Publicación 1",(vm.detalle.value as UiState.Exito).datos.post.title)
    }
    @Test fun eliminarPideConfirmacionYCancelarNoEnviaLaAccion() {
        val (_,vm)=mostrar();esperar(vm)
        ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Publicación 1"));ui.onNodeWithText("Publicación 1").performClick()
        ui.waitUntil { vm.detalle.value is UiState.Exito && !vm.cargandoComentarios.value }
        ui.onNodeWithContentDescription("Opciones de publicación").performClick();ui.onNodeWithText("Eliminar publicación").performClick()
        ui.onNodeWithText("Cancelar").performClick();assertNull(vm.eliminacion.value)
        ui.onNodeWithContentDescription("Opciones de publicación").performClick();ui.onNodeWithText("Eliminar publicación").performClick()
        ui.onNodeWithText("Eliminar").performClick();ui.waitUntil { vm.eliminacion.value is UiState.Exito }
        assertEquals(200,(vm.eliminacion.value as UiState.Exito).datos)
    }
    @Test fun guardarPermiteAbrirSnapshotLocalYQuitarSinRed() {
        val gvm=GuardadosViewModel(GuardadosPreferencesRepository(ApplicationProvider.getApplicationContext(),prefName));store.put("guardados",gvm)
        val (_,vm)=mostrar(guardados=gvm);esperar(vm)
        ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Publicación 1"))
        ui.onAllNodesWithContentDescription("Guardar para leer").onFirst().performClick();ui.waitUntil { gvm.guardados.value.size==1 }
        ui.onNodeWithContentDescription("Guardadas").performClick()
        ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Publicación 1"));ui.onNodeWithText("Publicación 1").performClick()
        ui.waitUntil { vm.detalle.value is UiState.Exito };assertEquals("Contenido de práctica 1",(vm.detalle.value as UiState.Exito).datos.post.body)
        ui.onNodeWithContentDescription("Quitar de guardadas").performClick();ui.waitUntil { gvm.guardados.value.isEmpty() }
    }
    @Test fun borradorPideConfirmacionYPermiteSeguirEscribiendo() {
        val (_,vm)=mostrar();esperar(vm);ui.onNodeWithText("Publicar").performClick()
        ui.onNodeWithText("Título").performTextInput("Mi borrador");ui.onNodeWithContentDescription("Volver").performClick()
        ui.onNodeWithText("Seguir escribiendo").performClick();ui.onNodeWithText("Título").assertTextContains("Mi borrador")
        ui.onNodeWithContentDescription("Volver").performClick();ui.onNodeWithText("Descartar").performClick();ui.onNodeWithText("MuroCampus").assertIsDisplayed()
    }
    @Test fun publicarTieneSalidaClaraAlMuro() {
        val (_,vm)=mostrar();esperar(vm);ui.onNodeWithText("Publicar").performClick()
        ui.onNodeWithText("Título").performTextInput("Aviso");ui.onNodeWithText("Contenido").performTextInput("Texto")
        ui.onNodeWithText("Publicar").performScrollTo().performClick();ui.waitUntil { vm.creacion.value is UiState.Exito }
        ui.onNodeWithText("Volver al muro").performScrollTo().performClick();ui.onNodeWithText("MuroCampus").assertIsDisplayed()
    }
    @Test fun compartirIncluyeTituloTextoYEnlaceSinEnviarNada() {
        val post=Post(1,7,"Aviso","Texto de práctica");val intent=intentCompartir(post)
        assertEquals(Intent.ACTION_SEND,intent.action);assertEquals("text/plain",intent.type)
        assertEquals(post.title,intent.getStringExtra(Intent.EXTRA_SUBJECT))
        val cuerpo=intent.getStringExtra(Intent.EXTRA_TEXT)!!
        assertTrue(cuerpo.contains(post.body));assertTrue(cuerpo.contains("https://jsonplaceholder.typicode.com/posts/7"))
    }
    @Test fun elMuroMuestraElCuerpoYLaAccionAbreComentarios() {
        val (_,vm)=mostrar();esperar(vm)
        ui.onNodeWithTag("lista_publicaciones").performScrollToNode(hasText("Contenido de práctica 1"))
        ui.onNodeWithText("Contenido de práctica 1").assertIsDisplayed()
        ui.onAllNodesWithContentDescription("Ver comentarios").onFirst().performClick()
        ui.waitUntil(5000) { vm.detalle.value is UiState.Exito && !vm.cargandoComentarios.value }
        assertEquals(5,(vm.detalle.value as UiState.Exito).datos.comentarios.size)
        ui.onNodeWithText("Comentario 1").assertIsDisplayed()
    }
    @Test fun buscarDesdeLaBarraYVolverAlMuroConservaLaDescarga() {
        val (repo,vm)=mostrar();esperar(vm)
        ui.onNodeWithContentDescription("Buscar publicaciones").performClick()
        ui.onNodeWithText("Buscar por título").performTextInput("Publicación 12")
        ui.waitUntil { (vm.visibles.value as? UiState.Exito)?.datos?.map { it.id }==listOf(12) }
        ui.onNodeWithTag("post_12").assertIsDisplayed()
        ui.onNodeWithContentDescription("Cerrar búsqueda").performClick()
        ui.waitUntil { vm.busqueda.value.isBlank() && (vm.visibles.value as? UiState.Exito)?.datos?.size==20 }
        ui.onNodeWithContentDescription("Muro").assertIsSelected()
        ui.onNodeWithTag("post_1").assertIsDisplayed()
        assertEquals(1,repo.listas)
    }
}
