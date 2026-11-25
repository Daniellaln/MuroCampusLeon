package com.leon.murocampus.data

import androidx.room.withTransaction
import com.leon.murocampus.data.local.*
import com.leon.murocampus.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import java.io.IOException

/** La decisión red/local vive aquí. Solo un error de conexión habilita el respaldo. */
class PostRepositoryConCache(private val remoto:PostRepository,private val bd:MuroDatabase):PostRepository {
    private val dao=bd.posts()
    private val _sinConexion=MutableStateFlow(false)
    override val sinConexion=_sinConexion.asStateFlow()
    private val _origenDetalle=MutableStateFlow(false)
    override val origenDetalle=_origenDetalle.asStateFlow()
    private val _origenComentarios=MutableStateFlow(false)
    override val origenComentarios=_origenComentarios.asStateFlow()
    private val _avisoCache=MutableStateFlow<String?>(null)
    override val avisoCache=_avisoCache.asStateFlow()
    private val avisosPorRecurso=mutableMapOf<String,String>()
    private fun actualizarAviso(clave:String,mensaje:String?)=synchronized(avisosPorRecurso) {
        if(mensaje==null)avisosPorRecurso.remove(clave) else avisosPorRecurso[clave]=mensaje
        _avisoCache.value=avisosPorRecurso.values.firstOrNull()
    }

    override suspend fun pagina(inicio:Int,limite:Int,usuario:Int?):Result<List<Post>> {
        val clave="${usuario ?: 0}:$inicio:$limite"
        if(inicio==0)_sinConexion.value=false
        return leer(
            origen=_sinConexion,claveAviso="pagina:$clave",conservarOrigenRed=inicio>0,
            red={ remoto.pagina(inicio,limite,usuario) },
            guardar={ posts -> bd.withTransaction {
                dao.guardar(posts.map(Post::entidad))
                dao.guardarPagina(PaginaEntity(clave,posts.joinToString(",") { it.id.toString() }))
            } },
            local={ bd.withTransaction {
                val pagina=dao.pagina(clave)
                if(pagina==null)null else {
                    val ids=pagina.ids.split(',').mapNotNull(String::toIntOrNull)
                    val guardados=dao.porIds(ids).associateBy { it.id }
                    if(guardados.size!=ids.size)null else ids.map { guardados.getValue(it).modelo() }
                }
            } }
        )
    }
    override suspend fun listar():Result<List<Post>> {
        _sinConexion.value=false
        return leer(origen=_sinConexion,claveAviso="lista",
        red={ remoto.listar() },guardar={ dao.guardar(it.map(Post::entidad)) },
        local={ dao.todas().map(PostEntity::modelo).takeIf { it.isNotEmpty() } }
        )
    }
    override suspend fun porUsuario(userId:Int):Result<List<Post>> {
        _sinConexion.value=false
        return leer(origen=_sinConexion,claveAviso="usuario:$userId",
        red={ remoto.porUsuario(userId) },guardar={ dao.guardar(it.map(Post::entidad)) },
        local={ dao.porUsuario(userId).map(PostEntity::modelo).takeIf { it.isNotEmpty() } }
        )
    }
    override suspend fun detalle(id:Int):Result<Post> {
        _origenDetalle.value=false
        return leer(origen=_origenDetalle,claveAviso="detalle:$id",
        red={ remoto.detalle(id) },guardar={ dao.guardar(listOf(it.entidad())) },
        local={ dao.detalle(id)?.modelo() }
        )
    }
    override suspend fun comentarios(id:Int):Result<List<Comentario>> {
        _origenComentarios.value=false
        return leer(origen=_origenComentarios,claveAviso="comentarios:$id",
        red={ remoto.comentarios(id) },guardar={ comentarios -> bd.withTransaction {
            dao.borrarComentarios(id)
            dao.guardarComentarios(comentarios.map(Comentario::entidad))
            dao.guardarPagina(PaginaEntity("comentarios:$id",comentarios.joinToString(",") { it.id.toString() }))
        } },
        local={ bd.withTransaction {
            val comentarios=dao.comentarios(id).map(ComentarioEntity::modelo)
            // Las copias de versión 1 no tienen marcador; un resultado no vacío sigue siendo utilizable.
            if(dao.pagina("comentarios:$id")!=null || comentarios.isNotEmpty())comentarios else null
        } }
        )
    }
    // JSONPlaceholder no persiste las escrituras: la caché tampoco inventa ese guardado.
    override suspend fun crear(nuevo:NuevoPost)=remoto.crear(nuevo)
    override suspend fun editar(id:Int,cambios:EditarPost)=remoto.editar(id,cambios)
    override suspend fun eliminar(id:Int)=remoto.eliminar(id)

    private suspend fun <T:Any> leer(origen:MutableStateFlow<Boolean>,claveAviso:String,conservarOrigenRed:Boolean=false,red:suspend ()->Result<T>,guardar:suspend (T)->Unit,local:suspend ()->T?):Result<T> {
        val resultado=red()
        currentCoroutineContext().ensureActive()
        if(resultado.isSuccess) {
            if(!conservarOrigenRed)origen.value=false
            try { guardar(resultado.getOrThrow());actualizarAviso(claveAviso,null) }
            catch(e:CancellationException) { throw e }
            catch(e:android.database.sqlite.SQLiteException) {
                actualizarAviso(claveAviso,"No pudimos guardar una copia sin conexión. Puedes seguir leyendo los datos descargados.")
            }
            return resultado
        }
        val error=resultado.exceptionOrNull()!!
        if(error is IOException) {
            return try {
                val copia=local()
                currentCoroutineContext().ensureActive()
                if(copia!=null)origen.value=true
                copia?.let { Result.success(it) } ?: resultado
            }
            catch(e:CancellationException) { throw e }
            catch(e:android.database.sqlite.SQLiteException) { Result.failure(e) }
        }
        return resultado
    }
}
