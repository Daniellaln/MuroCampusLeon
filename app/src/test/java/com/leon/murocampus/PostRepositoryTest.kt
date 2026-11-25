package com.leon.murocampus
import com.leon.murocampus.data.*
import com.leon.murocampus.data.remote.PostApi
import com.leon.murocampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.*
import retrofit2.Retrofit
import retrofit2.HttpException
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException
import org.junit.*
import org.junit.Assert.*
class PostRepositoryTest {
    private val server=MockWebServer()
    private lateinit var repo:PostRepository
    @Before fun iniciar() {
        server.start()
        val api=Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(Json { ignoreUnknownKeys=true }.asConverterFactory("application/json".toMediaType())).build().create(PostApi::class.java)
        repo=PostRepositoryRemote(api)
    }
    @After fun cerrar() { server.shutdown() }
    private fun json(text:String)=MockResponse().setHeader("Content-Type","application/json").setBody(text)
    @Test fun listarRealizaGetYConvierteLista()=runTest {
        server.enqueue(json("""[{"userId":1,"id":1,"title":"A","body":"B"}]"""))
        assertEquals(1,repo.listar().getOrThrow().size)
        val request=server.takeRequest();assertEquals("GET",request.method);assertEquals("/posts",request.path)
    }
    @Test fun detalleUsaPathYComentariosRutaAnidada()=runTest {
        server.enqueue(json("""{"userId":1,"id":7,"title":"A","body":"B"}"""));assertEquals(7,repo.detalle(7).getOrThrow().id)
        assertEquals("/posts/7",server.takeRequest().path)
        server.enqueue(json("""[{"postId":7,"id":1,"name":"R","email":"a@test.test","body":"C"}]"""))
        assertEquals(7,repo.comentarios(7).getOrThrow().first().postId);assertEquals("/posts/7/comments",server.takeRequest().path)
    }
    @Test fun filtroViajaComoQueryNoComoFiltroLocal()=runTest {
        server.enqueue(json("[]"));assertTrue(repo.porUsuario(2).getOrThrow().isEmpty())
        val request=server.takeRequest();assertEquals("/posts?userId=2",request.path)
    }
    @Test fun postEnviaBodyYRecibeLaRespuesta201()=runTest {
        server.enqueue(json("""{"userId":1,"id":101,"title":"Nuevo","body":"Contenido"}""").setResponseCode(201))
        assertEquals(101,repo.crear(NuevoPost(1,"Nuevo","Contenido")).getOrThrow().id)
        val request=server.takeRequest();assertEquals("POST",request.method);assertEquals("/posts",request.path)
        assertEquals("""{"userId":1,"title":"Nuevo","body":"Contenido"}""",request.body.readUtf8())
    }
    @Test fun error404EsResultFailureYMensajeDeServidor()=runTest {
        server.enqueue(json("{}").setResponseCode(404))
        val result=repo.detalle(9999);assertTrue(result.isFailure);assertTrue(result.exceptionOrNull() is HttpException)
        assertEquals("Error del servidor (404)",mensajeDe(result.exceptionOrNull()!!))
    }
    @Test fun jsonIncompletoEsFailureYNoCierraLaApp()=runTest {
        server.enqueue(json("""{"id":1}"""))
        val result=repo.detalle(1);assertTrue(result.isFailure);assertEquals("Respuesta inesperada",mensajeDe(result.exceptionOrNull()!!))
    }
    @Test fun ioTieneMensajeDeConexionYCancelacionSeRelanza()=runTest {
        assertTrue(mensajeDe(IOException()).contains("Sin conexión a internet"))
        val api=object:PostApi {
            override suspend fun listar():List<Post> = throw CancellationException("Cancelado")
            override suspend fun detalle(id:Int):Post=error("No usado")
            override suspend fun comentarios(id:Int):List<Comentario> = emptyList()
            override suspend fun porUsuario(userId:Int):List<Post> = emptyList()
            override suspend fun crear(nuevo:NuevoPost):Post=error("No usado")
        }
        try { PostRepositoryRemote(api).listar();fail("Se ocultó cancelación") } catch(expected:CancellationException) { assertEquals("Cancelado",expected.message) }
    }
}
