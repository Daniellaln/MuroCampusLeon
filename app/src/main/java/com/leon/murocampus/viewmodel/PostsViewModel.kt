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
    private val _usuarioFiltro=MutableStateFlow<Int?>(null)
    val usuarioFiltro=_usuarioFiltro.asStateFlow()
    fun filtrar(userId:Int?) {
        if(userId!=null && userId !in 1..10)return
        if(_usuarioFiltro.value==userId)return
        _usuarioFiltro.value=userId;cargar()
    }
    init { cargar() }
    fun cargar() {
        cargaLista?.cancel()
        val filtro=_usuarioFiltro.value
        cargaLista=viewModelScope.launch {
            _lista.value=UiState.Cargando
            val resultado=if(filtro==null)repo.listar() else repo.porUsuario(filtro)
            ensureActive()
            resultado.onSuccess { _lista.value=UiState.Exito(it) }
                .onFailure { _lista.value=UiState.Error(mensajeDe(it)) }
        }
    }
    private val _detalle=MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle=_detalle.asStateFlow()
    private var cargaDetalle:Job?=null
    fun abrir(id:Int) {
        cargaDetalle?.cancel()
        _detalle.value=UiState.Cargando
        cargaDetalle=viewModelScope.launch {
            coroutineScope {
                val a=async { repo.detalle(id) }
                val b=async { repo.comentarios(id) }
                val post=a.await()
                if(post.isFailure) { b.cancel();_detalle.value=UiState.Error(mensajeDe(post.exceptionOrNull()!!));return@coroutineScope }
                val comentarios=b.await()
                ensureActive()
                comentarios.onSuccess { _detalle.value=UiState.Exito(DetalleUi(post.getOrThrow(),it)) }
                    .onFailure { _detalle.value=UiState.Error(mensajeDe(it)) }
            }
        }
    }
    fun cerrarDetalle() { cargaDetalle?.cancel() }


}
