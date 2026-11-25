package com.leon.murocampus.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel

@Composable fun PantallaEditar(vm:PostsViewModel,post:Post) {
    var titulo by rememberSaveable(post.id) { mutableStateOf(post.title) }
    var cuerpo by rememberSaveable(post.id) { mutableStateOf(post.body) }
    val estado by vm.edicion.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(titulo,{ titulo=it },label={ Text("Título") },enabled=estado!=UiState.Cargando,modifier=Modifier.fillMaxWidth())
        OutlinedTextField(cuerpo,{ cuerpo=it },label={ Text("Contenido") },enabled=estado!=UiState.Cargando,modifier=Modifier.fillMaxWidth(),minLines=4)
        Button(onClick={ vm.editar(post.id,titulo,cuerpo) },enabled=titulo.isNotBlank() && cuerpo.isNotBlank() && estado!=UiState.Cargando,modifier=Modifier.fillMaxWidth().heightIn(min=48.dp)) { Text(if(estado==UiState.Cargando)"Guardando…" else "Guardar cambios") }
        when(val e=estado) {
            is UiState.Exito -> { Text("Cambios recibidos · id ${e.datos.id}");Text(e.datos.title);Text(e.datos.body);Text("Simulación: al recargar vuelve el contenido original.") }
            is UiState.Error -> ErrorConReintento(e.mensaje) { vm.editar(post.id,titulo,cuerpo) }
            else -> Unit
        }
    }
}
