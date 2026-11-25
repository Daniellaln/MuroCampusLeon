@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.leon.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.isSystemInDarkTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.R
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
import com.leon.murocampus.viewmodel.GuardadosViewModel

@Composable fun MuroCampusApp(vm:PostsViewModel,guardadosVm:GuardadosViewModel?=null) { MotionSettings {
    val scrollMuro=rememberLazyListState()
    val scrollGuardadas=rememberLazyListState()
    val alcance=rememberCoroutineScope()
    var viendoGuardadas by rememberSaveable { mutableStateOf(false) }
    var buscando by rememberSaveable { mutableStateOf(false) }
    var personasAbiertas by rememberSaveable { mutableStateOf(false) }
    var abrirComentarios by rememberSaveable { mutableStateOf(false) }
    val gestorFoco=androidx.compose.ui.platform.LocalFocusManager.current
    val teclado=androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    fun cerrarBusqueda() { vm.buscar("");buscando=false;gestorFoco.clearFocus();teclado?.hide() }
    fun seleccionarTab(guardadas:Boolean) { viendoGuardadas=guardadas;cerrarBusqueda() }
    BackHandler(buscando) { cerrarBusqueda() }
    val autores by vm.autores.collectAsStateWithLifecycle()
    val guardados=guardadosVm?.guardados?.collectAsStateWithLifecycle()?.value.orEmpty()
    val guardadosCargando=guardadosVm?.cargando?.collectAsStateWithLifecycle()?.value ?: false
    val errorGuardados=guardadosVm?.error?.collectAsStateWithLifecycle()?.value
    var borradorModificado by rememberSaveable { mutableStateOf(false) }
    var confirmarSalida by remember { mutableStateOf(false) }
    var ruta by rememberSaveable { mutableStateOf("muro") }
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var menu by remember { mutableStateOf(false) }
    var confirmar by remember { mutableStateOf(false) }
    var info by remember { mutableStateOf(false) }
    val detalle by vm.detalle.collectAsStateWithLifecycle()
    val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    fun salir() {
        borradorModificado=false
        menu=false;confirmar=false
        when(ruta) {
            "editar" -> { vm.cerrarEdicion();ruta="detalle" }
            "crear" -> { vm.cerrarCreacion();ruta="muro" }
            else -> { vm.cerrarDetalle();idSeleccionado=null;ruta="muro" }
        }
    }
    fun volverAlMuro() { vm.cerrarCreacion();vm.cerrarEdicion();vm.cerrarDetalle();borradorModificado=false;idSeleccionado=null;ruta="muro" }
    fun volver() { if(borradorModificado && ruta in listOf("crear","editar"))confirmarSalida=true else salir() }
    BackHandler(ruta!="muro") { volver() }
    if(confirmarSalida)AlertDialog(onDismissRequest={ confirmarSalida=false },title={ Text("¿Descartar el borrador?") },text={ Text("Los cambios que no enviaste se perderán.") },confirmButton={ TextButton(onClick={ confirmarSalida=false;salir() }) { Text("Descartar") } },dismissButton={ TextButton(onClick={ confirmarSalida=false }) { Text("Seguir escribiendo") } })
    LaunchedEffect(idSeleccionado,guardadosCargando) { idSeleccionado?.let { id -> if(!viendoGuardadas)vm.abrir(id) else if(!guardadosCargando && vm.detalle.value !is UiState.Exito) { guardados.firstOrNull { it.id==id }?.let(vm::abrirGuardado) ?: vm.abrir(id) } } }
    val c=MaterialTheme.colorScheme
    if(errorGuardados!=null)AlertDialog(onDismissRequest={ guardadosVm?.limpiarError() },title={ Text("No se pudo guardar") },text={ Text(errorGuardados) },confirmButton={ TextButton(onClick={ guardadosVm?.limpiarError() }) { Text("Entendido") } })
    if(info)AlertDialog(onDismissRequest={ info=false },title={ Text("Sobre el muro") },text={ Text("Publicaciones de práctica de JSONPlaceholder. Los textos de relleno vienen de la API. Crear, editar y eliminar se simulan; al recargar vuelven los datos originales. La copia sin conexión conserva lo que ya descargaste.") },confirmButton={ TextButton(onClick={ info=false }) { Text("Entendido") } })
    if(confirmar)AlertDialog(onDismissRequest={ confirmar=false },icon={ Icon(painterResource(R.drawable.ic_eliminar),null,tint=c.error) },
        title={ Text("¿Eliminar publicación?") },text={ Text("Esta solicitud es una simulación. La publicación seguirá disponible al actualizar el muro.") },
        confirmButton={ TextButton(onClick={ confirmar=false;idSeleccionado?.let(vm::eliminar) }) { Text("Eliminar",color=c.error) } },
        dismissButton={ TextButton(onClick={ confirmar=false }) { Text("Cancelar") } })
    val tecladoVisible=WindowInsets.ime.getBottom(LocalDensity.current)>0
    CompositionLocalProvider(LocalAutores provides autores,LocalGuardados provides guardados,LocalGuardar provides { post -> guardadosVm?.alternar(post) }) {
    Box(Modifier.fillMaxSize().background(c.background)) {
    Scaffold(containerColor=Color.Transparent,contentColor=c.onBackground,modifier=Modifier.semantics { testTagsAsResourceId=true },topBar={
        TopAppBar(expandedHeight=56.dp,title={
            if(ruta=="muro")Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                Image(painterResource(R.drawable.ic_muro),null,Modifier.size(24.dp))
                Text("MuroCampus",style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.Bold,maxLines=1)
            } else Text(when(ruta) { "crear"->"Crear publicación";"editar"->"Editar publicación";else->"Publicación" },style=MaterialTheme.typography.titleMedium)
        },navigationIcon={ if(ruta=="muro")IconButton(onClick={ personasAbiertas=true },enabled=!viendoGuardadas) {
            Icon(painterResource(R.drawable.ic_filtro),"Filtrar por usuario",Modifier.size(22.dp))
        } else Box(Modifier.padding(start=12.dp,end=8.dp)) { GlassIconButton(R.drawable.ic_volver,"Volver",::volver) } },
        actions={
            if(ruta=="muro") {
                IconButton(onClick=vm::cargar) { Icon(painterResource(R.drawable.ic_recargar),"Actualizar muro",Modifier.size(22.dp)) }
                IconButton(onClick={ info=true }) { Icon(painterResource(R.drawable.ic_info),"Sobre los datos",Modifier.size(22.dp)) }
            } else if(ruta=="detalle")Box(Modifier.padding(end=16.dp)) {
                GlassIconButton(R.drawable.ic_mas,"Opciones de publicación",{ menu=true },enabled=detalle is UiState.Exito && eliminacion!=UiState.Cargando)
                DropdownMenu(expanded=menu,onDismissRequest={ menu=false }) {
                    DropdownMenuItem(text={ Text("Editar publicación") },leadingIcon={ Icon(painterResource(R.drawable.ic_editar),null) },onClick={ menu=false;vm.prepararEdicion();ruta="editar" })
                    DropdownMenuItem(text={ Text("Eliminar publicación",color=c.error) },leadingIcon={ Icon(painterResource(R.drawable.ic_eliminar),null,tint=c.error) },onClick={ menu=false;vm.prepararEliminacion();confirmar=true })
                }
            }
        },colors=TopAppBarDefaults.topAppBarColors(containerColor=c.background,titleContentColor=c.onBackground))
    },bottomBar={ if(ruta=="muro" && !tecladoVisible)Box(Modifier.fillMaxWidth(),contentAlignment=Alignment.Center) {
        Box(Modifier.widthIn(max=760.dp)) { NavegacionSocial(if(buscando)1 else if(viendoGuardadas)3 else 0,
            onMuro={ seleccionarTab(false) },
            onBuscar={ buscando=true },
            onPublicar={ vm.prepararCreacion();ruta="crear" },
            onGuardadas={ seleccionarTab(true) }) }
    } }) { margen ->
        Box(Modifier.fillMaxSize().padding(margen).imePadding(),contentAlignment=Alignment.TopCenter) {
            val motion=LocalMotionEnabled.current
            AnimatedContent(targetState=ruta,modifier=Modifier.widthIn(max=760.dp).fillMaxSize(),label="navegación del muro",transitionSpec={
                if(motion)(fadeIn(tween(180))+slideInHorizontally(tween(220)) { if(targetState=="muro")-it/14 else it/14 }) togetherWith fadeOut(tween(100))
                else EnterTransition.None togetherWith ExitTransition.None
            }) { pantalla ->
                when(pantalla) {
                    "crear" -> PantallaCrear(vm,::volverAlMuro) { borradorModificado=it }
                    "editar" -> { val post=(detalle as? UiState.Exito)?.datos?.post;if(post!=null)PantallaEditar(vm,post,::volverAlMuro) { borradorModificado=it } else Cargando("Cargando publicación…") }
                    "detalle" -> PantallaDetalle(vm,idSeleccionado ?: 1,::volverAlMuro,mostrarComentarios=abrirComentarios)
                    else -> PantallaLista(vm,if(viendoGuardadas)scrollGuardadas else scrollMuro,viendoGuardadas,::seleccionarTab,
                        busquedaAbierta=buscando,onCerrarBusqueda=::cerrarBusqueda,
                        personasAbiertas=personasAbiertas,onCerrarPersonas={ personasAbiertas=false },
                        onAbrirComentarios={ id ->
                            if(viendoGuardadas)guardados.firstOrNull { it.id==id }?.let(vm::abrirGuardado)
                            abrirComentarios=true;idSeleccionado=id;ruta="detalle"
                        }) { id ->
                            if(viendoGuardadas)guardados.firstOrNull { it.id==id }?.let(vm::abrirGuardado)
                            abrirComentarios=false;idSeleccionado=id;ruta="detalle"
                        }
                }
            }
        }
    }
} } } }
