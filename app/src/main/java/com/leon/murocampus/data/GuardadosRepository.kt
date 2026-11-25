package com.leon.murocampus.data

import android.content.Context
import com.leon.murocampus.model.Post
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Copias locales elegidas por la persona; no modifica publicaciones en la API. */
interface GuardadosRepository {
    val guardados: StateFlow<List<Post>>

    suspend fun cargar()
    suspend fun alternar(post: Post)
    suspend fun quitar(id: Int)
}

/** No abre preferencias ni lee el disco hasta la primera operación suspendida. */
class GuardadosPreferencesRepository(
    context: Context,
    nombrePreferencias: String = "murocampus_guardados",
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GuardadosRepository by GuardadosRepositoryLocal(
    PreferenciasGuardados(context.applicationContext, nombrePreferencias),
    ioDispatcher,
)

/** Separación del disco para probar las mismas operaciones con almacenamiento en memoria. */
internal interface PersistenciaGuardados {
    fun leer(): List<Post>
    fun escribir(posts: List<Post>)
}

internal class GuardadosRepositoryLocal(
    private val persistencia: PersistenciaGuardados,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GuardadosRepository {
    private val mutex = Mutex()
    private var inicializado = false
    private val _guardados = MutableStateFlow<List<Post>>(emptyList())
    override val guardados = _guardados.asStateFlow()

    override suspend fun cargar() = withContext(ioDispatcher) {
        mutex.withLock { cargarSiHaceFalta() }
    }

    override suspend fun alternar(post: Post) = modificar { anteriores ->
        if (anteriores.any { it.id == post.id }) anteriores.filterNot { it.id == post.id }
        else listOf(post.copy()) + anteriores
    }

    override suspend fun quitar(id: Int) = modificar { anteriores ->
        anteriores.filterNot { it.id == id }
    }

    private suspend fun modificar(transformar: (List<Post>) -> List<Post>) = withContext(ioDispatcher) {
        mutex.withLock {
            cargarSiHaceFalta()
            val nuevos = transformar(_guardados.value)
            if (nuevos != _guardados.value) {
                currentCoroutineContext().ensureActive()
                persistencia.escribir(nuevos)
                // Publicar después del commit, sin suspensión entre ambos: si el llamador
                // se cancela durante el disco, el estado aún coincide con lo persistido.
                _guardados.value = nuevos
            }
        }
    }

    private fun cargarSiHaceFalta() {
        if (inicializado) return
        val copias = persistencia.leer().distinctBy(Post::id)
        _guardados.value = copias
        inicializado = true
    }
}

private class PreferenciasGuardados(
    private val applicationContext: Context,
    private val nombre: String,
) : PersistenciaGuardados {
    private val json = Json { ignoreUnknownKeys = true }
    private val preferencias by lazy {
        applicationContext.getSharedPreferences(nombre, Context.MODE_PRIVATE)
    }

    override fun leer(): List<Post> {
        val contenido = preferencias.getString(CLAVE, null) ?: return emptyList()
        return json.decodeFromString<List<Post>>(contenido)
    }

    override fun escribir(posts: List<Post>) {
        val contenido = json.encodeToString(posts)
        if (!preferencias.edit().putString(CLAVE, contenido).commit()) {
            throw IOException("No se pudo guardar la lectura en este dispositivo.")
        }
    }

    private companion object {
        const val CLAVE = "publicaciones_v1"
    }
}
