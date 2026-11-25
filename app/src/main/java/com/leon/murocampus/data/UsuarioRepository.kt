package com.leon.murocampus.data
import com.leon.murocampus.model.Usuario
import com.leon.murocampus.data.remote.UsuarioApi
import kotlinx.coroutines.CancellationException
interface UsuarioRepository { suspend fun usuario(id:Int):Result<Usuario> }
class UsuarioRepositoryRemote(private val api:UsuarioApi):UsuarioRepository {
    override suspend fun usuario(id:Int):Result<Usuario> = try { Result.success(api.usuario(id)) }
    catch(e:CancellationException) { throw e }
    catch(e:Exception) { Result.failure(e) }
}
