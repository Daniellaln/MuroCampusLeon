package com.leon.murocampus.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leon.murocampus.data.UsuarioRepository
import com.leon.murocampus.data.mensajeDe
import com.leon.murocampus.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class UsuarioViewModel(private val repo:UsuarioRepository):ViewModel() {
    private val _estado=MutableStateFlow<UiState<Usuario>>(UiState.Cargando)
    val estado=_estado.asStateFlow()
    fun cargar(id:Int) { viewModelScope.launch { _estado.value=UiState.Cargando;repo.usuario(id).onSuccess { _estado.value=UiState.Exito(it) }.onFailure { _estado.value=UiState.Error(mensajeDe(it)) } } }
}
