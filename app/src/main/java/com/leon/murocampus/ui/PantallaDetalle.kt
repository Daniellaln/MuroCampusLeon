package com.leon.murocampus.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.ActivityNotFoundException
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
@Composable fun PantallaDetalle(vm:PostsViewModel,id:Int,onEditar:(Post)->Unit={}) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
        val contexto=LocalContext.current
        var errorCompartir by remember { mutableStateOf(false) }
    when(val actual=estado) {
        UiState.Cargando -> Cargando("Cargando publicación…")
        is UiState.Error -> ErrorConReintento(actual.mensaje) { vm.abrir(id) }
        is UiState.Exito -> LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
            item {
                Column(Modifier.fillMaxWidth().padding(vertical=8.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                    Text(actual.datos.post.title,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
                    OutlinedButton(onClick={ onEditar(actual.datos.post) }) { Text("Editar publicación") }
                    Text(actual.datos.post.body,style=MaterialTheme.typography.bodyLarge)
                        OutlinedButton(onClick={
                            try { compartirPublicacion(contexto,actual.datos.post);errorCompartir=false }
                            catch(e:ActivityNotFoundException) { errorCompartir=true }
                        },modifier=Modifier.heightIn(min=48.dp)) { Text("Compartir publicación") }
                        if(errorCompartir)Text("No hay una aplicación disponible para compartir",color=MaterialTheme.colorScheme.error)
    
                }
            }
            item { Text("Comentarios (${actual.datos.comentarios.size})",style=MaterialTheme.typography.titleMedium,color=MaterialTheme.colorScheme.primary,fontWeight=FontWeight.Bold) }
            items(actual.datos.comentarios,key={ it.id }) { comentario ->
                Surface(Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),color=MaterialTheme.colorScheme.surfaceVariant) {
                    Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        Text(comentario.email,color=MaterialTheme.colorScheme.primary,style=MaterialTheme.typography.titleSmall)
                        Text(comentario.body,style=MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
