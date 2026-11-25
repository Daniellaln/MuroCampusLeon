package com.leon.murocampus
import com.leon.murocampus.data.*
import com.leon.murocampus.data.remote.*
import com.leon.murocampus.model.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.*
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import org.junit.*
import org.junit.Assert.*
class FaseDosHttpTest {
    private val server=MockWebServer();private lateinit var repo:PostRepository
    @Before fun iniciar() {
        server.start()
        val api=Retrofit.Builder().baseUrl(server.url("/")).client(OkHttpClient.Builder().addInterceptor(AcceptJsonInterceptor()).build())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build().create(PostApi::class.java)
        repo=PostRepositoryRemote(api)
    }
    @After fun cerrar() { server.shutdown() }
    @Test fun paginaUsaInicioLimiteYUsuarioEnLaMismaPeticion()=runTest {
        server.enqueue(MockResponse().setBody("[]"));repo.pagina(20,20,2).getOrThrow()
        val req=server.takeRequest();assertEquals("/posts?_start=20&_limit=20&userId=2",req.path);assertEquals("application/json",req.getHeader("Accept"))
    }
    @Test fun patchEnviaSoloTituloYContenido()=runTest {
        server.enqueue(MockResponse().setBody("""{"id":7,"userId":1,"title":"Nuevo","body":"Texto"}"""))
        assertEquals("Nuevo",repo.editar(7,EditarPost("Nuevo","Texto")).getOrThrow().title)
        val req=server.takeRequest();assertEquals("PATCH",req.method);assertEquals("/posts/7",req.path)
        assertEquals("""{"title":"Nuevo","body":"Texto"}""",req.body.readUtf8())
    }
    @Test fun deleteCompruebaCodigoYRechazaRespuestaDeError()=runTest {
        for(codigo in listOf(200,204)) {
            server.enqueue(MockResponse().setResponseCode(codigo));assertEquals(codigo,repo.eliminar(7).getOrThrow());assertEquals("DELETE",server.takeRequest().method)
        }
        server.enqueue(MockResponse().setResponseCode(403));assertTrue(repo.eliminar(7).isFailure)
    }
}
