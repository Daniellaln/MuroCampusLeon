package com.leon.murocampus.ui

import android.content.ActivityNotFoundException
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.R
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, onVolver: () -> Unit = {}, mostrarComentarios: Boolean = false) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    val offline by vm.detalleSinConexion.collectAsStateWithLifecycle()
    val cargandoComentarios by vm.cargandoComentarios.collectAsStateWithLifecycle()
    val errorComentarios by vm.errorComentarios.collectAsStateWithLifecycle()
    val avisoCache by vm.avisoCache.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val c = MaterialTheme.colorScheme
    var errorCompartir by remember(id) { mutableStateOf(false) }
    var comentariosAbiertos by rememberSaveable(id, mostrarComentarios) { mutableStateOf(mostrarComentarios) }
    when (val actual = estado) {
        UiState.Cargando -> Box(Modifier.padding(horizontal = 24.dp)) { Cargando("Cargando publicación…") }
        is UiState.Error -> Box(Modifier.padding(horizontal = 20.dp)) { ErrorConReintento(actual.mensaje) { vm.abrir(id) } }
        is UiState.Exito -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(key = "autor") {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Autor(actual.datos.post.userId, Modifier.weight(1f))
                    GuardarButton(actual.datos.post)
                }
            }
            if (offline) item(key = "offline") { Aviso("Sin conexión · copia disponible", R.drawable.ic_nube) }
            if (avisoCache != null) item(key = "aviso_cache") { Aviso(avisoCache!!) }
            item(key = "lectura") {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text(actual.datos.post.title, style = MaterialTheme.typography.headlineSmall, color = c.onSurface,
                        modifier = Modifier.semantics { heading() })
                    SelectionContainer {
                        Text(actual.datos.post.body, style = MaterialTheme.typography.bodyLarge, color = c.onSurface)
                    }
                }
            }
            item(key = "compartir") {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    TextButton(onClick = {
                        try { compartirPublicacion(contexto, actual.datos.post); errorCompartir = false }
                        catch (e: ActivityNotFoundException) { errorCompartir = true }
                    }, modifier = Modifier.heightIn(min = 48.dp), contentPadding = PaddingValues(horizontal = 0.dp, vertical = 12.dp)) {
                        Icon(painterResource(R.drawable.ic_compartir), null, Modifier.size(21.dp))
                        Spacer(Modifier.width(10.dp)); Text("Compartir publicación")
                    }
                    if (errorCompartir) Text("No hay una aplicación para compartir", style = MaterialTheme.typography.bodySmall,
                        color = c.error, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                }
            }
            when (val e = eliminacion) {
                is UiState.Exito -> item(key = "eliminacion") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Resultado("Eliminación recibida", actual.datos.post.title)
                        TextButton(onClick = onVolver, modifier = Modifier.heightIn(min = 48.dp)) { Text("Volver al muro") }
                    }
                }
                is UiState.Error -> item(key = "error_eliminacion") { ErrorEnLinea(e.mensaje) { vm.eliminar(id) } }
                UiState.Cargando -> item(key = "eliminando") { LinearProgressIndicator(Modifier.fillMaxWidth()) }
                null -> Unit
            }
            item(key = "comentarios_cabecera") {
                Column {
                    HorizontalDivider(color = c.outlineVariant)
                    TextButton(onClick = { comentariosAbiertos = !comentariosAbiertos },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp).semantics {
                            stateDescription = if (comentariosAbiertos) "Comentarios expandidos" else "Comentarios contraídos"
                        }, contentPadding = PaddingValues(vertical = 12.dp)) {
                        Icon(painterResource(R.drawable.ic_comentar), null, Modifier.size(21.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(if (cargandoComentarios || errorComentarios != null) "Comentarios" else "Comentarios (${actual.datos.comentarios.size})",
                            style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        Icon(painterResource(if (comentariosAbiertos) R.drawable.ic_cerrar else R.drawable.ic_desplegar),
                            if (comentariosAbiertos) "Ocultar comentarios" else "Ver comentarios", Modifier.size(20.dp))
                    }
                }
            }
            if (comentariosAbiertos) {
                if (cargandoComentarios) item(key = "comentarios_cargando") {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                        Text("Cargando comentarios…", style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant)
                    }
                }
                if (errorComentarios != null) item(key = "comentarios_error") {
                    ErrorEnLinea(errorComentarios!!) { vm.reintentarComentarios(id) }
                }
                if (!cargandoComentarios && errorComentarios == null && actual.datos.comentarios.isEmpty()) {
                    item(key = "comentarios_vacio") {
                        Text("Todavía no hay comentarios.", style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant)
                    }
                }
                items(actual.datos.comentarios, key = { "comentario_${it.id}" }) { comentario ->
                    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        Text(comentario.email, style = MaterialTheme.typography.titleSmall, color = c.primary)
                        if (comentario.name.isNotBlank()) Text(comentario.name,
                            style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = c.onSurface)
                        Text(comentario.body, style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant)
                        HorizontalDivider(Modifier.padding(top = 12.dp), color = c.outlineVariant.copy(alpha = .6f))
                    }
                }
            }
        }
    }
}
