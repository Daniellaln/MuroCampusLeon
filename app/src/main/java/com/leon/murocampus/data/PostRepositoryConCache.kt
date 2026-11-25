package com.leon.murocampus.data

import androidx.room.withTransaction
import com.leon.murocampus.data.local.*
import com.leon.murocampus.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import java.io.IOException

/** La decisión red/local vive aquí. Solo un error de conexión habilita el respaldo. */
class PostRepositoryConCache(private val remoto:PostRepository,private val bd:MuroDatabase):PostRepository {
    private val dao=bd.posts()
    private val _sinConexion=MutableStateFlow(false)
    override val sinConexion=_sinConexion.asStateFlow()

    override suspend fun pagina(inicio:Int,limite:Int,usuario:Int?):Result<List<Post>> {
        val clave="${usuario ?: 0}:$inicio:$limite"
        return leer(
            red={ remoto.pagina(inicio,limite,usuario) },
            guardar={ posts -> bd.withTransaction {
                dao.guardar(posts.map(Post::entidad))
                dao.guardarPagina(PaginaEntity(clave,posts.joinToString(",") { it.id.toString() }))
            } },
            local={
                val pagina=dao.pagina(clave)
                if(pagina==null)null else {
                    val ids=pagina.ids.split(',').mapNotNull(String::toIntOrNull)
                    val guardados=dao.porIds(ids).associateBy { it.id }
                    if(guardados.size!=ids.size)null else ids.map { guardados.getValue(it).modelo() }
                }
            }
        )
    }
    override suspend fun listar()=leer(
        red={ remoto.listar() },guardar={ dao.guardar(it.map(Post::entidad)) },
        local={ dao.todas().map(PostEntity::modelo).takeIf { it.isNotEmpty() } }
    )
    override suspend fun porUsuario(userId:Int)=leer(
        red={ remoto.porUsuario(userId) },guardar={ dao.guardar(it.map(Post::entidad)) },
        local={ dao.porUsuario(userId).map(PostEntity::modelo).takeIf { it.isNotEmpty() } }
    )
    override suspend fun detalle(id:Int)=leer(
        red={ remoto.detalle(id) },guardar={ dao.guardar(listOf(it.entidad())) },
        local={ dao.detalle(id)?.modelo() }
    )
    override suspend fun comentarios(id:Int)=leer(
        red={ remoto.comentarios(id) },guardar={ dao.guardarComentarios(it.map(Comentario::entidad)) },
        local={ dao.comentarios(id).map(ComentarioEntity::modelo).takeIf { it.isNotEmpty() } }
    )
    // JSONPlaceholder no persiste las escrituras: la caché tampoco inventa ese guardado.
    override suspend fun crear(nuevo:NuevoPost)=remoto.crear(nuevo)
    override suspend fun editar(id:Int,cambios:EditarPost)=remoto.editar(id,cambios)
    override suspend fun eliminar(id:Int)=remoto.eliminar(id)

    private suspend fun <T:Any> leer(red:suspend ()->Result<T>,guardar:suspend (T)->Unit,local:suspend ()->T?):Result<T> {
        val resultado=red()
        if(resultado.isSuccess) {
            _sinConexion.value=false
            try { guardar(resultado.getOrThrow()) }
            catch(e:CancellationException) { throw e }
            catch(e:android.database.sqlite.SQLiteException) { return Result.failure(e) }
            return resultado
        }
        val error=resultado.exceptionOrNull()!!
        if(error is IOException) {
            _sinConexion.value=true
            return try { local()?.let { Result.success(it) } ?: resultado }
            catch(e:CancellationException) { throw e }
            catch(e:android.database.sqlite.SQLiteException) { Result.failure(e) }
        }
        return resultado
    }
}
