package com.leon.murocampus

import com.leon.murocampus.data.remote.AcceptJsonInterceptor
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.*
import org.junit.Test
import org.junit.Assert.*

class AcceptJsonTest {
    @Test fun todasLasRutasRecibenAcceptSinCambiarElCuerpo() {
        val server=MockWebServer();server.start()
        try {
            val client=OkHttpClient.Builder().addInterceptor(AcceptJsonInterceptor()).build()
            for(method in listOf("GET","POST","PATCH","DELETE")) {
                server.enqueue(MockResponse().setBody("{}"))
                val body=if(method in listOf("POST","PATCH")) "{\"title\":\"Aviso\"}".toRequestBody() else null
                client.newCall(Request.Builder().url(server.url("/posts/1")).method(method,body).build()).execute().use { assertTrue(it.isSuccessful) }
                val request=server.takeRequest();assertEquals("application/json",request.getHeader("Accept"))
                if(body!=null)assertEquals("{\"title\":\"Aviso\"}",request.body.readUtf8())
            }
        } finally { server.shutdown() }
    }
}
