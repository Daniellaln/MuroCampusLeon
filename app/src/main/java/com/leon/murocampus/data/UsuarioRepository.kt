package com.leon.murocampus.data
import com.leon.murocampus.model.Usuario
import com.leon.murocampus.data.remote.UsuarioApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val sinCopiaLocal=MutableStateFlow(false).asStateFlow()
private val sinAvisoUsuarios=MutableStateFlow<String?>(null).asStateFlow()
interface UsuarioRepository {
    val sinConexion:StateFlow<Boolean> get()=sinCopiaLocal
    val avisoCache:StateFlow<String?> get()=sinAvisoUsuarios
    suspend fun listar():Result<List<Usuario>>
    suspend fun usuario(id:Int):Result<Usuario>
}
class UsuarioRepositoryRemote(private val api:UsuarioApi):UsuarioRepository {
    override suspend fun listar()=llamar { api.listar() }
    override suspend fun usuario(id:Int)=llamar { api.usuario(id) }
    private suspend fun <T> llamar(bloque:suspend ()->T):Result<T> = try { Result.success(bloque()) }
    catch(e:CancellationException) { throw e }
    catch(e:Exception) { Result.failure(e) }
}
