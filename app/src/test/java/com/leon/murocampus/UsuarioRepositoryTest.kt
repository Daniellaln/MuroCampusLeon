package com.leon.murocampus

import com.leon.murocampus.data.UsuarioRepositoryRemote
import com.leon.murocampus.data.remote.AcceptJsonInterceptor
import com.leon.murocampus.data.remote.UsuarioApi
import com.leon.murocampus.model.Usuario
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class UsuarioRepositoryTest {
    private val server=MockWebServer()
    private lateinit var repo:UsuarioRepositoryRemote
    @Before fun iniciar() {
        server.start()
        val api=Retrofit.Builder().baseUrl(server.url("/"))
            .client(OkHttpClient.Builder().addInterceptor(AcceptJsonInterceptor()).build())
            .addConverterFactory(Json { ignoreUnknownKeys=true }.asConverterFactory("application/json".toMediaType()))
            .build().create(UsuarioApi::class.java)
        repo=UsuarioRepositoryRemote(api)
    }
    @After fun cerrar() { server.shutdown() }
    @Test fun listadoUsaUnaPeticionYConservaNombresDelServicio()=runTest {
        server.enqueue(MockResponse().setBody("""[{"id":1,"name":"Leanne Graham","username":"Bret","email":"leanne@example.test","address":{"city":"Gwenborough"}}]"""))
        assertEquals(Usuario(1,"Leanne Graham","Bret","leanne@example.test"),repo.listar().getOrThrow().single())
        val request=server.takeRequest()
        assertEquals("/users",request.path)
        assertEquals("application/json",request.getHeader("Accept"))
    }
    @Test fun errorHttpNoSeConvierteEnUnaListaVacia()=runTest {
        server.enqueue(MockResponse().setResponseCode(503))
        assertTrue(repo.listar().isFailure)
    }
    @Test fun cancelacionNoSeConvierteEnErrorVisible()=runTest {
        val api=object:UsuarioApi {
            override suspend fun listar():List<Usuario> = throw CancellationException("Cancelado")
            override suspend fun usuario(id:Int):Usuario = error("No usado")
        }
        try { UsuarioRepositoryRemote(api).listar();fail("Se ocultó la cancelación") }
        catch(expected:CancellationException) { assertEquals("Cancelado",expected.message) }
    }
}
