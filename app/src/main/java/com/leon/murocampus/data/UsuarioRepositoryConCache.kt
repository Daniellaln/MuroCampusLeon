package com.leon.murocampus.data

import android.database.sqlite.SQLiteException
import androidx.room.withTransaction
import com.leon.murocampus.data.local.*
import com.leon.murocampus.model.Usuario
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** El listado conserva su pertenencia exacta, incluso si se consultó un usuario suelto. */
class UsuarioRepositoryConCache(private val remoto:UsuarioRepository,private val bd:MuroDatabase):UsuarioRepository {
    private val dao=bd.usuarios()
    private val _sinConexion=MutableStateFlow(false)
    override val sinConexion=_sinConexion.asStateFlow()
    private val _avisoCache=MutableStateFlow<String?>(null)
    override val avisoCache=_avisoCache.asStateFlow()

    override suspend fun listar()=leer(
        red={ remoto.listar() },
        guardar={ usuarios -> bd.withTransaction {
            dao.guardar(usuarios.map(Usuario::entidad))
            bd.posts().guardarPagina(PaginaEntity("usuarios:todos",usuarios.joinToString(",") { it.id.toString() }))
        } },
        local={ bd.withTransaction {
            val pagina=bd.posts().pagina("usuarios:todos")
            if(pagina==null)null else {
                val ids=pagina.ids.split(',').mapNotNull(String::toIntOrNull)
                val usuarios=dao.porIds(ids).associateBy { it.id }
                if(usuarios.size!=ids.size)null else ids.map { usuarios.getValue(it).modelo() }
            }
        } }
    )

    override suspend fun usuario(id:Int)=leer(
        red={ remoto.usuario(id) },guardar={ dao.guardar(listOf(it.entidad())) },
        local={ dao.usuario(id)?.modelo() }
    )

    private suspend fun <T:Any> leer(red:suspend ()->Result<T>,guardar:suspend (T)->Unit,local:suspend ()->T?):Result<T> {
        val resultado=red()
        currentCoroutineContext().ensureActive()
        if(resultado.isSuccess) {
            _sinConexion.value=false
            try { guardar(resultado.getOrThrow());_avisoCache.value=null }
            catch(e:CancellationException) { throw e }
            catch(e:SQLiteException) {
                _avisoCache.value="No pudimos guardar los autores para leerlos sin conexión."
            }
            return resultado
        }
        if(resultado.exceptionOrNull() is IOException) {
            return try {
                val copia=local()
                currentCoroutineContext().ensureActive()
                _sinConexion.value=copia!=null
                copia?.let { Result.success(it) } ?: resultado
            }
            catch(e:CancellationException) { throw e }
            catch(e:SQLiteException) { Result.failure(e) }
        }
        _sinConexion.value=false
        return resultado
    }
}
