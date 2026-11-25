package com.leon.murocampus.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
@Composable fun PantallaCrear(vm:PostsViewModel) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }
    val estado by vm.creacion.collectAsStateWithLifecycle()
    val enviando=estado==UiState.Cargando
    LaunchedEffect(estado) { if(estado is UiState.Exito) { titulo="";contenido="" } }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(titulo,{ titulo=it },label={ Text("Título") },modifier=Modifier.fillMaxWidth(),enabled=!enviando)
        OutlinedTextField(contenido,{ contenido=it },label={ Text("Contenido") },modifier=Modifier.fillMaxWidth().heightIn(min=150.dp),minLines=5,enabled=!enviando)
        Button(onClick={ vm.publicar(titulo,contenido) },enabled=titulo.isNotBlank() && contenido.isNotBlank() && !enviando,modifier=Modifier.align(Alignment.CenterHorizontally).heightIn(min=48.dp)) { Text("Publicar") }
        when(val actual=estado) {
            UiState.Cargando -> Cargando("Publicando…")
            is UiState.Exito -> Surface(color=MaterialTheme.colorScheme.primaryContainer,modifier=Modifier.fillMaxWidth()) { Text("Publicado con id ${actual.datos.id}",Modifier.padding(16.dp),color=MaterialTheme.colorScheme.onPrimaryContainer) }
            is UiState.Error -> ErrorConReintento(actual.mensaje) { vm.publicar(titulo,contenido) }
            null -> Unit
        }
    }
}
