package com.leon.murocampus.data
import com.leon.murocampus.data.remote.PostApi
import com.leon.murocampus.model.*
import kotlinx.coroutines.CancellationException
interface PostRepository {
    suspend fun listar():Result<List<Post>>
    suspend fun detalle(id:Int):Result<Post>
    suspend fun comentarios(id:Int):Result<List<Comentario>>
    suspend fun porUsuario(userId:Int):Result<List<Post>>
    suspend fun eliminar(id:Int):Result<Int>
    suspend fun editar(id:Int,cambios:EditarPost):Result<Post>
    suspend fun crear(nuevo:NuevoPost):Result<Post>
}
class PostRepositoryRemote(private val api:PostApi):PostRepository {
    override suspend fun listar()=llamar { api.listar() }
    override suspend fun detalle(id:Int)=llamar { api.detalle(id) }
    override suspend fun comentarios(id:Int)=llamar { api.comentarios(id) }
    override suspend fun porUsuario(userId:Int)=llamar { api.porUsuario(userId) }
    override suspend fun eliminar(id:Int)=llamar {
        val respuesta=api.eliminar(id)
        if(!respuesta.isSuccessful)throw retrofit2.HttpException(respuesta)
        respuesta.code()
    }
    override suspend fun editar(id:Int,cambios:EditarPost)=llamar { api.editar(id,cambios) }
    override suspend fun crear(nuevo:NuevoPost)=llamar { api.crear(nuevo) }
    private suspend fun <T> llamar(bloque:suspend ()->T):Result<T> = try { Result.success(bloque()) }
    catch(e:CancellationException) { throw e }
    catch(e:Exception) { Result.failure(e) }
}
