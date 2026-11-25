package com.leon.murocampus.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leon.murocampus.data.UsuarioRepository
import com.leon.murocampus.data.mensajeDe
import com.leon.murocampus.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
class UsuarioViewModel(private val repo:UsuarioRepository):ViewModel() {
    private val _estado=MutableStateFlow<UiState<Usuario>>(UiState.Cargando)
    val estado=_estado.asStateFlow()
    private var carga:Job?=null
    fun cargar(id:Int) {
        carga?.cancel()
        carga=viewModelScope.launch {
            _estado.value=UiState.Cargando
            val resultado=repo.usuario(id);ensureActive()
            resultado.onSuccess { _estado.value=UiState.Exito(it) }.onFailure { _estado.value=UiState.Error(mensajeDe(it)) }
        }
    }
}
