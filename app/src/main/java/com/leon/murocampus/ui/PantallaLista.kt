package com.leon.murocampus.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
@Composable fun PantallaLista(vm:PostsViewModel,onAbrir:(Int)->Unit) {
    val hayMas by vm.hayMas.collectAsStateWithLifecycle()
    val cargandoMas by vm.cargandoMas.collectAsStateWithLifecycle()
    val errorMas by vm.errorMas.collectAsStateWithLifecycle()
    val descargados by vm.lista.collectAsStateWithLifecycle()
    val estado by vm.visibles.collectAsStateWithLifecycle()
    val offline by vm.sinConexion.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val consulta by vm.busqueda.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        if(offline)Text("Sin conexión · leyendo la copia guardada",Modifier.padding(16.dp))
        OutlinedTextField(value=consulta,onValueChange=vm::buscar,label={ Text("Buscar por título") },singleLine=true,modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp))
        if(consulta.isNotBlank())TextButton(onClick={ vm.buscar("") }) { Text("Limpiar búsqueda") }
        Text("Buscar solo en ${(descargados as? UiState.Exito)?.datos?.size ?: 0} publicaciones descargadas",Modifier.padding(horizontal=16.dp))
        LazyRow(Modifier.fillMaxWidth(),contentPadding=PaddingValues(horizontal=16.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            item { FilterChip(selected=filtro==null,onClick={ vm.filtrar(null) },label={ Text("Todos") }) }
            items((1..10).toList()) { id -> FilterChip(selected=filtro==id,onClick={ vm.filtrar(id) },label={ Text("Usuario $id") }) }
        }
        when(val actual=estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(actual.mensaje,vm::cargar)
            is UiState.Exito -> if(actual.datos.isEmpty())Column(Modifier.padding(24.dp)) { Text("No hay coincidencias en lo descargado");if(hayMas)TextButton(vm::cargarMas,enabled=!cargandoMas) { Text("Cargar más") } } else LazyColumn(Modifier.fillMaxSize().testTag("lista_publicaciones"),contentPadding=PaddingValues(start=16.dp,end=16.dp,top=12.dp,bottom=88.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                items(actual.datos,key={ it.id }) { post -> TarjetaPost(post.userId,post.title,post.body) { onAbrir(post.id) } }
                item {
                    if(errorMas!=null)Text(errorMas!!,color=MaterialTheme.colorScheme.error)
                    if(hayMas)OutlinedButton(onClick=vm::cargarMas,enabled=!cargandoMas) { Text(if(cargandoMas)"Cargando…" else if(errorMas!=null)"Reintentar carga" else "Cargar más") }
                    else Text("Llegaste al final")
                }
            }
        }
    }
}
