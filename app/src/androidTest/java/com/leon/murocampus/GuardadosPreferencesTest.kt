package com.leon.murocampus

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.leon.murocampus.data.GuardadosPreferencesRepository
import com.leon.murocampus.model.Post
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class GuardadosPreferencesTest {
    private fun prueba(block: suspend (Context, String) -> Unit) = runBlocking {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        val nombre = "prueba_guardados_${UUID.randomUUID()}"
        try { block(contexto, nombre) }
        finally { contexto.deleteSharedPreferences(nombre) }
    }

    @Test fun nuevaInstanciaRecuperaTodoElPostYLaEliminacionPersiste() = prueba { contexto, nombre ->
        val primero = Post(7, 42, "Leer \"después\" — campus", "Línea uno\nLínea dos: acción y café")
        val segundo = Post(2, 8, "Proyecto de aula", "Contenido completo de la publicación")
        val original = GuardadosPreferencesRepository(contexto, nombre)
        original.alternar(primero)
        original.alternar(segundo)

        val restaurado = GuardadosPreferencesRepository(contexto, nombre)
        assertTrue(restaurado.guardados.value.isEmpty())
        restaurado.cargar()
        assertEquals(listOf(segundo, primero), restaurado.guardados.value)
        assertEquals(primero.body, restaurado.guardados.value.last().body)
        restaurado.quitar(segundo.id)

        val final = GuardadosPreferencesRepository(contexto, nombre)
        final.cargar()
        assertEquals(listOf(primero), final.guardados.value)
    }

    @Test fun alternarAntesDeCargarLeeLaListaPersistidaYNoLaReemplaza() = prueba { contexto, nombre ->
        val primero = Post(1, 11, "Primera lectura", "Primera copia")
        val segundo = Post(2, 22, "Segunda lectura", "Segunda copia")
        GuardadosPreferencesRepository(contexto, nombre).alternar(primero)
        GuardadosPreferencesRepository(contexto, nombre).alternar(segundo)
        val restaurado = GuardadosPreferencesRepository(contexto, nombre)
        restaurado.cargar()
        assertEquals(listOf(segundo, primero), restaurado.guardados.value)
        restaurado.alternar(primero)
        val sinPrimero = GuardadosPreferencesRepository(contexto, nombre)
        sinPrimero.cargar()
        assertEquals(listOf(segundo), sinPrimero.guardados.value)
    }

    @Test fun datosIlegiblesDanErrorSinBorrarseSilenciosamente() = prueba { contexto, nombre ->
        val preferencias = contexto.getSharedPreferences(nombre, Context.MODE_PRIVATE)
        val contenidoInvalido = "{incompleto"
        assertTrue(preferencias.edit().putString("publicaciones_v1", contenidoInvalido).commit())
        val repo = GuardadosPreferencesRepository(contexto, nombre)
        assertTrue(runCatching { repo.cargar() }.isFailure)
        assertTrue(repo.guardados.value.isEmpty())
        assertEquals(contenidoInvalido, preferencias.getString("publicaciones_v1", null))
    }
}
