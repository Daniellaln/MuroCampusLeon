package com.leon.murocampus.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leon.murocampus.data.PostRepository
import com.leon.murocampus.data.mensajeDe
import com.leon.murocampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
class PostsViewModel(private val repo:PostRepository):ViewModel() {
    private val _lista=MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista=_lista.asStateFlow()
    private var cargaLista:Job?=null

    init { cargar() }
    fun cargar() {
        cargaLista?.cancel()

        cargaLista=viewModelScope.launch {
            _lista.value=UiState.Cargando
            val resultado=repo.listar()
            ensureActive()
            resultado.onSuccess { _lista.value=UiState.Exito(it) }
                .onFailure { _lista.value=UiState.Error(mensajeDe(it)) }
        }
    }


}
