package com.leon.murocampus
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leon.murocampus.data.*
import com.leon.murocampus.data.remote.RetrofitClient
import com.leon.murocampus.model.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
@RunWith(AndroidJUnit4::class)
class ServicioRealTest {
    @Test fun endpointsRealesDeJsonPlaceholder() {
        val listo=CountDownLatch(1);var error:Throwable?=null
        val scope=CoroutineScope(SupervisorJob()+Dispatchers.IO)
        scope.launch {
            try {
                val repo=PostRepositoryRemote(RetrofitClient.api)
                val posts=repo.listar().getOrThrow();assertEquals(100,posts.size)
                for(user in 1..10) {
                    val datos=repo.porUsuario(user).getOrThrow();assertEquals(10,datos.size);assertTrue(datos.all { it.userId==user })
                }
                for(id in listOf(1,50,100)) {
                    assertEquals(id,repo.detalle(id).getOrThrow().id)
                    val comentarios=repo.comentarios(id).getOrThrow();assertEquals(5,comentarios.size);assertTrue(comentarios.all { it.postId==id })
                }
                val primera=repo.pagina(0,20,null).getOrThrow();val segunda=repo.pagina(20,20,null).getOrThrow()
                assertEquals(20,primera.size);assertEquals(20,segunda.size);assertTrue(primera.none { a -> segunda.any { it.id==a.id } })
                assertEquals(10,repo.pagina(0,20,2).getOrThrow().size)
                val original=repo.detalle(1).getOrThrow()
                assertEquals("Edicion de practica",repo.editar(1,EditarPost("Edicion de practica","Contenido de practica")).getOrThrow().title)
                assertEquals(200,repo.eliminar(1).getOrThrow());assertEquals(original,repo.detalle(1).getOrThrow())
                val inexistente=repo.detalle(9999);assertTrue(inexistente.isFailure)
                assertEquals("Error del servidor (404)",mensajeDe(inexistente.exceptionOrNull()!!))
                val nuevo=repo.crear(NuevoPost(1,"Aviso del campus","Hola desde MuroCampus")).getOrThrow()
                assertEquals(101,nuevo.id);assertEquals("Aviso del campus",nuevo.title)
                android.util.Log.d("MuroCampusReal","VERIFICADO:100posts,10porcadausuario,5comentarios,404,POST101")
            } catch(e:CancellationException) { throw e }
            catch(e:Throwable) { error=e }
            finally { listo.countDown() }
        }
        val termino=listo.await(75,TimeUnit.SECONDS);scope.cancel()
        assertTrue("El servicio no respondió dentro del tiempo de prueba",termino)
        if(error!=null)throw AssertionError("Falló la prueba contra el servicio real",error)
    }
}
