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
    private val _busqueda=MutableStateFlow("")
    val busqueda=_busqueda.asStateFlow()
    val visibles=combine(lista,busqueda) { estado,consulta ->
        when(estado) {
            is UiState.Exito -> UiState.Exito(estado.datos.filter { it.title.contains(consulta.trim(),ignoreCase=true) })
            is UiState.Error -> estado
            UiState.Cargando -> UiState.Cargando
        }
    }.stateIn(viewModelScope,SharingStarted.Eagerly,UiState.Cargando)
    fun buscar(texto:String) { _busqueda.value=texto }
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

    private val _edicion=MutableStateFlow<UiState<Post>?>(null)
    val edicion=_edicion.asStateFlow()
    private var guardado:Job?=null
    fun prepararEdicion() { guardado?.cancel();_edicion.value=null }
    fun cerrarEdicion() { guardado?.cancel();_edicion.value=null }
    fun editar(id:Int,titulo:String,texto:String) {
        if(titulo.isBlank() || texto.isBlank() || _edicion.value==UiState.Cargando)return
        _edicion.value=UiState.Cargando
        guardado=viewModelScope.launch {
            val resultado=repo.editar(id,EditarPost(titulo.trim(),texto.trim()))
            ensureActive()
            resultado.onSuccess { _edicion.value=UiState.Exito(it) }
                .onFailure { _edicion.value=UiState.Error(mensajeDe(it)) }
        }
    }
    private val _creacion=MutableStateFlow<UiState<Post>?>(null)
    val creacion=_creacion.asStateFlow()
    private var envio:Job?=null
    fun prepararCreacion() { envio?.cancel();_creacion.value=null }
    fun cerrarCreacion() { envio?.cancel();_creacion.value=null }
    fun publicar(titulo:String,texto:String) {
        if(titulo.isBlank() || texto.isBlank() || _creacion.value==UiState.Cargando)return
        _creacion.value=UiState.Cargando
        envio=viewModelScope.launch {
            val resultado=repo.crear(NuevoPost(userId=1,title=titulo.trim(),body=texto.trim()))
            ensureActive()
            resultado.onSuccess { _creacion.value=UiState.Exito(it) }
                .onFailure { _creacion.value=UiState.Error(mensajeDe(it)) }
        }
    }

}
