package com.leon.murocampus.ui
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.leon.murocampus.R
import com.leon.murocampus.viewmodel.PostsViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun MuroCampusApp(vm:PostsViewModel) {
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }
    fun volver() { if(creando) { vm.cerrarCreacion();creando=false } else { vm.cerrarDetalle();idSeleccionado=null } }
    BackHandler(idSeleccionado!=null || creando) { volver() }
    Scaffold(topBar={
        TopAppBar(title={ Text(if(creando)"Crear publicación" else if(idSeleccionado!=null)"Detalle de publicación" else "MuroCampus") },navigationIcon={ if(idSeleccionado!=null || creando)IconButton(::volver) { Icon(painterResource(R.drawable.ic_volver),"Volver") } },colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.primary,titleContentColor=MaterialTheme.colorScheme.onPrimary,navigationIconContentColor=MaterialTheme.colorScheme.onPrimary))
    },floatingActionButton={ if(!creando && idSeleccionado==null)FloatingActionButton(onClick={ vm.prepararCreacion();creando=true }) { Icon(painterResource(R.drawable.ic_agregar),"Crear publicación") } }) { margen ->
        Box(Modifier.fillMaxSize().padding(margen).imePadding()) { if(creando)PantallaCrear(vm) else if(idSeleccionado!=null)PantallaDetalle(vm,idSeleccionado!!) else PantallaLista(vm) { id -> vm.abrir(id);idSeleccionado=id } }
    }
}
