package com.leon.murocampus.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.leon.murocampus.data.PostRepository
import com.leon.murocampus.data.UsuarioRepository
import com.leon.murocampus.data.mensajeDe
import com.leon.murocampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
class PostsViewModel(private val repo:PostRepository,private val usuariosRepo:UsuarioRepository?=null,private val savedStateHandle:SavedStateHandle=SavedStateHandle()):ViewModel() {
    val sinConexion=repo.sinConexion
    val detalleSinConexion=combine(repo.origenDetalle,repo.origenComentarios) { post,comentarios -> post || comentarios }
        .stateIn(viewModelScope,SharingStarted.Eagerly,repo.origenDetalle.value || repo.origenComentarios.value)
    val avisoCache=repo.avisoCache
    val sinConexionAutores=usuariosRepo?.sinConexion ?: MutableStateFlow(false).asStateFlow()
    val avisoCacheAutores=usuariosRepo?.avisoCache ?: MutableStateFlow<String?>(null).asStateFlow()
    private val _autores=MutableStateFlow<Map<Int,Usuario>>(emptyMap())
    val autores=_autores.asStateFlow()
    private val _estadoAutores=MutableStateFlow<UiState<List<Usuario>>>(if(usuariosRepo==null)UiState.Exito(emptyList()) else UiState.Cargando)
    val estadoAutores=_estadoAutores.asStateFlow()
    private var cargaAutores:Job?=null
    fun recargarAutores() {
        val repositorio=usuariosRepo ?: return
        cargaAutores?.cancel()
        cargaAutores=viewModelScope.launch {
            _estadoAutores.value=UiState.Cargando
            val resultado=repositorio.listar();ensureActive()
            resultado.onSuccess {
                _autores.value=it.associateBy(Usuario::id)
                _estadoAutores.value=UiState.Exito(it)
            }.onFailure { _estadoAutores.value=UiState.Error(mensajeDe(it)) }
        }
    }
    private val _lista=MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista=_lista.asStateFlow()
    private var cargaLista:Job?=null
    private var versionLista=0L
    private val _usuarioFiltro=MutableStateFlow(savedStateHandle.get<Int>(CLAVE_FILTRO)?.takeIf { it in 1..10 })
    val usuarioFiltro=_usuarioFiltro.asStateFlow()
    private val _busqueda=MutableStateFlow(savedStateHandle.get<String>(CLAVE_BUSQUEDA) ?: "")
    val busqueda=_busqueda.asStateFlow()
    val visibles=combine(lista,busqueda) { estado,consulta ->
        when(estado) {
            is UiState.Exito -> UiState.Exito(estado.datos.filter { it.title.contains(consulta.trim(),ignoreCase=true) })
            is UiState.Error -> estado
            UiState.Cargando -> UiState.Cargando
        }
    }.stateIn(viewModelScope,SharingStarted.Eagerly,UiState.Cargando)
    fun buscar(texto:String) { _busqueda.value=texto;savedStateHandle[CLAVE_BUSQUEDA]=texto }
    fun filtrar(userId:Int?) {
        if(userId!=null && userId !in 1..10)return
        if(_usuarioFiltro.value==userId)return
        _usuarioFiltro.value=userId;savedStateHandle[CLAVE_FILTRO]=userId;cargar()
    }
    private var siguienteInicio=0
    private val tamanoPagina=20
    private val _hayMas=MutableStateFlow(false)
    val hayMas=_hayMas.asStateFlow()
    private val _cargandoMas=MutableStateFlow(false)
    val cargandoMas=_cargandoMas.asStateFlow()
    private val _errorMas=MutableStateFlow<String?>(null)
    val errorMas=_errorMas.asStateFlow()

    init { cargar();recargarAutores() }
    fun cargar() {
        versionLista++
        cargaLista?.cancel()
        val filtro=_usuarioFiltro.value
        siguienteInicio=0;_hayMas.value=false;_cargandoMas.value=false;_errorMas.value=null
        cargaLista=viewModelScope.launch {
            _lista.value=UiState.Cargando
            val resultado=repo.pagina(0,tamanoPagina,filtro)
            ensureActive()
            resultado.onSuccess {
                siguienteInicio=it.size;_hayMas.value=it.size==tamanoPagina
                _lista.value=UiState.Exito(it.distinctBy(Post::id))
            }.onFailure { _lista.value=UiState.Error(mensajeDe(it)) }
        }
    }
    fun cargarMas() {
        if(!_hayMas.value || _cargandoMas.value)return
        val anteriores=(_lista.value as? UiState.Exito)?.datos ?: return
        val filtro=_usuarioFiltro.value;val inicio=siguienteInicio
        val version=versionLista
        _cargandoMas.value=true;_errorMas.value=null
        cargaLista=viewModelScope.launch {
            try {
                val resultado=repo.pagina(inicio,tamanoPagina,filtro);ensureActive()
                resultado.onSuccess {
                    siguienteInicio=inicio+it.size;_hayMas.value=it.size==tamanoPagina
                    _lista.value=UiState.Exito((anteriores+it).distinctBy(Post::id))
                }.onFailure { _errorMas.value=mensajeDe(it) }
            } finally { if(version==versionLista)_cargandoMas.value=false }
        }
    }
    private val _detalle=MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle=_detalle.asStateFlow()
    private var cargaDetalle:Job?=null
    private var cargaComentarios:Job?=null
    private var versionDetalle=0L
    private val _cargandoComentarios=MutableStateFlow(false)
    val cargandoComentarios=_cargandoComentarios.asStateFlow()
    private val _errorComentarios=MutableStateFlow<String?>(null)
    val errorComentarios=_errorComentarios.asStateFlow()
    fun abrir(id:Int) {
        versionDetalle++
        val version=versionDetalle
        cargaDetalle?.cancel();cargaComentarios?.cancel()
        _detalle.value=UiState.Cargando
        _cargandoComentarios.value=true;_errorComentarios.value=null
        cargaDetalle=viewModelScope.launch {
            try {
            coroutineScope {
                val b=async { repo.comentarios(id) }
                val post=repo.detalle(id);ensureActive()
                if(post.isFailure) { b.cancel();_detalle.value=UiState.Error(mensajeDe(post.exceptionOrNull()!!));return@coroutineScope }
                _detalle.value=UiState.Exito(DetalleUi(post.getOrThrow(),emptyList()))
                val comentarios=b.await()
                ensureActive()
                mostrarComentarios(id,comentarios)
            }
            } finally { if(version==versionDetalle)_cargandoComentarios.value=false }
        }
    }
    /** Una lectura guardada pertenece al dispositivo y se abre aunque no exista conexión. */
    fun abrirGuardado(post:Post) {
        versionDetalle++;cargaDetalle?.cancel();cargaComentarios?.cancel()
        cerrarEliminacion()
        _detalle.value=UiState.Exito(DetalleUi(post,emptyList()))
        _cargandoComentarios.value=false
        _errorComentarios.value="Los comentarios necesitan conexión"
    }
    private fun mostrarComentarios(id:Int,resultado:Result<List<Comentario>>) {
        val actual=(_detalle.value as? UiState.Exito)?.datos ?: return
        if(actual.post.id!=id)return
        resultado.onSuccess { _detalle.value=UiState.Exito(actual.copy(comentarios=it));_errorComentarios.value=null }
            .onFailure { _errorComentarios.value=mensajeDe(it) }
    }
    fun reintentarComentarios(id:Int) {
        if(_cargandoComentarios.value || (_detalle.value as? UiState.Exito)?.datos?.post?.id!=id)return
        cargaComentarios?.cancel()
        val version=versionDetalle
        _cargandoComentarios.value=true;_errorComentarios.value=null
        cargaComentarios=viewModelScope.launch {
            try {
                val resultado=repo.comentarios(id);ensureActive()
                mostrarComentarios(id,resultado)
            } finally { if(version==versionDetalle)_cargandoComentarios.value=false }
        }
    }
    fun cerrarDetalle() {
        versionDetalle++;cargaDetalle?.cancel();cargaComentarios?.cancel()
        _cargandoComentarios.value=false;_errorComentarios.value=null;cerrarEliminacion()
    }

    private val _eliminacion=MutableStateFlow<UiState<Int>?>(null)
    val eliminacion=_eliminacion.asStateFlow()
    private var borrado:Job?=null
    fun prepararEliminacion() { borrado?.cancel();_eliminacion.value=null }
    fun cerrarEliminacion() { borrado?.cancel();_eliminacion.value=null }
    fun eliminar(id:Int) {
        if(_eliminacion.value==UiState.Cargando)return
        _eliminacion.value=UiState.Cargando
        borrado=viewModelScope.launch {
            val resultado=repo.eliminar(id);ensureActive()
            resultado.onSuccess { _eliminacion.value=UiState.Exito(it) }
                .onFailure { _eliminacion.value=UiState.Error(mensajeDe(it)) }
        }
    }
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
    private companion object {
        const val CLAVE_FILTRO="usuarioFiltro"
        const val CLAVE_BUSQUEDA="busqueda"
    }
}
