package com.leon.murocampus
import com.leon.murocampus.model.*
import kotlinx.serialization.json.*
import kotlinx.serialization.*
import org.junit.Test
import org.junit.Assert.*
class ModelosTest {
    private val json=Json { ignoreUnknownKeys=true }
    @Test fun postUsaNombresExactosYAdmiteCamposExtras() {
        val post=json.decodeFromString<Post>("""{"userId":2,"id":11,"title":"Título","body":"Texto","extra":true}""")
        assertEquals(Post(2,11,"Título","Texto"),post)
    }
    @Test fun faltaUnCampoObligatorioEsRespuestaInesperada() {
        val error=runCatching { json.decodeFromString<Post>("""{"userId":2,"id":11,"title":"Título"}""") }.exceptionOrNull()
        assertTrue(error is SerializationException)
    }
    @Test fun nuevoPostNoEnviaId() {
        val valor=json.parseToJsonElement(json.encodeToString(NuevoPost(1,"Título","Texto"))).jsonObject
        assertEquals(setOf("userId","title","body"),valor.keys)
        assertEquals(1,valor["userId"]!!.jsonPrimitive.int)
    }
    @Test fun comentarioConservaPostIdYCorreo() {
        val c=json.decodeFromString<Comentario>("""{"postId":1,"id":1,"name":"Respuesta","email":"campus@example.test","body":"Texto"}""")
        assertEquals(1,c.postId);assertEquals("campus@example.test",c.email)
    }
}
