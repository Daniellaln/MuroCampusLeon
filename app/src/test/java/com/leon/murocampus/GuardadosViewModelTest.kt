@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.leon.murocampus

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.leon.murocampus.data.GuardadosRepository
import com.leon.murocampus.data.GuardadosRepositoryLocal
import com.leon.murocampus.data.PersistenciaGuardados
import com.leon.murocampus.model.Post
import com.leon.murocampus.viewmodel.GuardadosViewModel
import com.leon.murocampus.viewmodel.GuardadosViewModelFactory
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.Assert.*
import org.junit.Test

class GuardadosViewModelTest {
    private val primero = Post(7, 42, "Título completo", "Texto guardado sin red")
    private val segundo = Post(2, 8, "Otro título", "Otra lectura")

    private class EnMemoria(var contenido: List<Post> = emptyList()) : PersistenciaGuardados {
        var lecturas = 0
        var escrituras = 0
        var fallaLectura = false
        var fallaEscritura = false

        override fun leer(): List<Post> {
            lecturas++
            if (fallaLectura) throw IOException("Lectura fallida")
            return contenido.toList()
        }

        override fun escribir(posts: List<Post>) {
            if (fallaEscritura) throw IOException("Disco lleno")
            escrituras++
            contenido = posts.toList()
        }
    }

    private fun prueba(block: suspend TestScope.(ViewModelStore) -> Unit) = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val store = ViewModelStore()
        try { block(store) }
        finally {
            store.clear()
            Dispatchers.resetMain()
        }
    }

    private fun TestScope.crear(store: ViewModelStore, disco: EnMemoria): GuardadosViewModel {
        val repo = GuardadosRepositoryLocal(disco, StandardTestDispatcher(testScheduler))
        return ViewModelProvider(store, GuardadosViewModelFactory(repo))[GuardadosViewModel::class.java]
    }

    @Test fun restauraCopiasCompletasDeFormaAsincronaYAbreSinRed() = prueba { store ->
        val disco = EnMemoria(listOf(primero))
        val vm = crear(store, disco)
        assertEquals(0, disco.lecturas)
        assertTrue(vm.cargando.value)
        runCurrent()
        assertEquals(listOf(primero), vm.guardados.value)
        assertEquals(primero, vm.abrir(primero.id))
        assertNull(vm.abrir(999))
        assertFalse(vm.cargando.value)
        assertNull(vm.error.value)
    }

    @Test fun alternarAntesDeTerminarLaCargaConservaLasOtrasLecturas() = prueba { store ->
        val disco = EnMemoria(listOf(primero))
        val vm = crear(store, disco)
        vm.alternar(segundo)
        runCurrent()
        assertEquals(listOf(segundo, primero), vm.guardados.value)
        assertEquals(vm.guardados.value, disco.contenido)
        assertEquals(1, disco.lecturas)
    }

    @Test fun dosToquesPendientesNoPierdenCambiosNiDuplicanPublicaciones() = prueba { store ->
        val disco = EnMemoria(listOf(primero))
        val vm = crear(store, disco)
        runCurrent()
        vm.alternar(segundo)
        vm.alternar(segundo)
        runCurrent()
        assertEquals(listOf(primero), vm.guardados.value)
        assertEquals(listOf(primero), disco.contenido)
        assertEquals(2, disco.escrituras)
    }

    @Test fun falloDePersistenciaConservaEstadoYPermiteReintentar() = prueba { store ->
        val disco = EnMemoria(listOf(primero))
        val vm = crear(store, disco)
        runCurrent()
        disco.fallaEscritura = true
        vm.alternar(segundo)
        runCurrent()
        assertNotNull(vm.error.value)
        assertEquals(listOf(primero), vm.guardados.value)
        assertEquals(listOf(primero), disco.contenido)
        disco.fallaEscritura = false
        vm.alternar(segundo)
        runCurrent()
        assertEquals(listOf(segundo, primero), vm.guardados.value)
        assertNull(vm.error.value)
    }

    @Test fun errorDeCargaSePuedeReintentarSinRecrearLaPantalla() = prueba { store ->
        val disco = EnMemoria(listOf(primero)).apply { fallaLectura = true }
        val vm = crear(store, disco)
        runCurrent()
        assertNotNull(vm.error.value)
        assertFalse(vm.cargando.value)
        disco.fallaLectura = false
        vm.recargar()
        runCurrent()
        assertEquals(listOf(primero), vm.guardados.value)
        assertNull(vm.error.value)
    }

    @Test fun quitarSoloBorraLaCopiaSolicitadaYEsIdempotente() = prueba { store ->
        val disco = EnMemoria(listOf(primero, segundo))
        val vm = crear(store, disco)
        runCurrent()
        vm.quitar(primero.id)
        vm.quitar(primero.id)
        runCurrent()
        assertEquals(listOf(segundo), vm.guardados.value)
        assertEquals(listOf(segundo), disco.contenido)
        assertEquals(1, disco.escrituras)
    }

    @Test fun providerMantieneElMismoViewModelAlRecrearElConsumidor() = prueba { store ->
        val disco = EnMemoria()
        val primeroVm = crear(store, disco)
        primeroVm.alternar(primero)
        runCurrent()
        val nuevoConsumidor = crear(store, disco)
        assertSame(primeroVm, nuevoConsumidor)
        assertEquals(primero, nuevoConsumidor.abrir(primero.id))
        assertEquals(1, disco.lecturas)
    }

    @Test fun cancelarElViewModelNoConvierteLaCancelacionEnError() = prueba { store ->
        var cancelada = false
        val repo = object : GuardadosRepository {
            override val guardados = MutableStateFlow<List<Post>>(emptyList())
            override suspend fun cargar() = Unit
            override suspend fun alternar(post: Post) {
                try { awaitCancellation() }
                finally { cancelada = true }
            }
            override suspend fun quitar(id: Int) = Unit
        }
        val vm = ViewModelProvider(store, GuardadosViewModelFactory(repo))[GuardadosViewModel::class.java]
        vm.alternar(primero)
        runCurrent()
        store.clear()
        runCurrent()
        assertTrue(cancelada)
        assertNull(vm.error.value)
        assertTrue(vm.guardados.value.isEmpty())
    }
}
