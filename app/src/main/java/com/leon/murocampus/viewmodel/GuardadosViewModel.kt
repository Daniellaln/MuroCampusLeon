package com.leon.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leon.murocampus.data.GuardadosRepository
import com.leon.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GuardadosViewModel(private val repo: GuardadosRepository) : ViewModel() {
    val guardados = repo.guardados
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()

    init { recargar() }

    fun recargar() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.cargar()
                _error.value = null
            } catch (cancelada: CancellationException) {
                throw cancelada
            } catch (_: Exception) {
                _error.value = "No pudimos abrir tus lecturas guardadas. Vuelve a intentarlo."
            } finally {
                _cargando.value = false
            }
        }
    }

    fun alternar(post: Post) = cambiar { repo.alternar(post) }

    fun quitar(id: Int) = cambiar { repo.quitar(id) }

    /** Devuelve la copia guardada, incluso cuando la API no está disponible. */
    fun abrir(id: Int): Post? = guardados.value.firstOrNull { it.id == id }

    fun limpiarError() { _error.value = null }

    private fun cambiar(operacion: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                operacion()
                _error.value = null
            } catch (cancelada: CancellationException) {
                throw cancelada
            } catch (_: Exception) {
                _error.value = "No pudimos actualizar tus lecturas guardadas. Vuelve a intentarlo."
            }
        }
    }
}

class GuardadosViewModelFactory(private val repo: GuardadosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(GuardadosViewModel::class.java))
        @Suppress("UNCHECKED_CAST")
        return GuardadosViewModel(repo) as T
    }
}
