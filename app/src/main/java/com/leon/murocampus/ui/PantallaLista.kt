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
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        LazyRow(Modifier.fillMaxWidth(),contentPadding=PaddingValues(horizontal=16.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            item { FilterChip(selected=filtro==null,onClick={ vm.filtrar(null) },label={ Text("Todos") }) }
            items((1..10).toList()) { id -> FilterChip(selected=filtro==id,onClick={ vm.filtrar(id) },label={ Text("Usuario $id") }) }
        }
        when(val actual=estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(actual.mensaje,vm::cargar)
            is UiState.Exito -> if(actual.datos.isEmpty())Text("Sin publicaciones",Modifier.padding(24.dp)) else LazyColumn(Modifier.fillMaxSize().testTag("lista_publicaciones"),contentPadding=PaddingValues(start=16.dp,end=16.dp,top=12.dp,bottom=88.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                items(actual.datos,key={ it.id }) { post -> TarjetaPost(post.userId,post.title,post.body) { onAbrir(post.id) } }
            }
        }
    }
}
