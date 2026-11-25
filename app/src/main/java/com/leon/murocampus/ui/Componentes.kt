package com.leon.murocampus.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leon.murocampus.R
import com.leon.murocampus.model.Post

@Composable
fun AvatarAutor(usuarioId: Int, modifier: Modifier = Modifier, size: Dp = 40.dp) {
    val c = MaterialTheme.colorScheme
    val autor = LocalAutores.current[usuarioId]
    val rosa = usuarioId % 3 == 0
    Box(
        modifier.size(size).background(if (rosa) c.secondaryContainer else c.primaryContainer, CircleShape)
            .clearAndSetSemantics {},
        contentAlignment = Alignment.Center
    ) {
        Text(inicialesAutor(autor?.name, usuarioId), style = MaterialTheme.typography.labelMedium,
            color = if (rosa) c.onSecondaryContainer else c.onPrimaryContainer)
    }
}

@Composable
fun Autor(usuarioId: Int, modifier: Modifier = Modifier, compacto: Boolean = false) {
    val c = MaterialTheme.colorScheme
    val autor = LocalAutores.current[usuarioId]
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        AvatarAutor(usuarioId, size = if (compacto) 36.dp else 40.dp)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(autor?.name ?: "Usuario $usuarioId", style = MaterialTheme.typography.labelLarge,
                color = c.onSurface, maxLines = 2, overflow = TextOverflow.Ellipsis)
            autor?.username?.takeIf { it.isNotBlank() }?.let {
                Text("@$it", style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun FilaPublicacion(post: Post, onAbrir: () -> Unit, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    val interacciones = remember { MutableInteractionSource() }
    val presionado by interacciones.collectIsPressedAsState()
    val escala by animateFloatAsState(if (presionado) .988f else 1f,
        if (LocalMotionEnabled.current) spring(.88f, 700f) else snap(), label = "abrir lectura")
    Column(modifier.fillMaxWidth().graphicsLayer { scaleX = escala; scaleY = escala }) {
        Row(Modifier.fillMaxWidth().padding(top = 14.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Autor(post.userId, Modifier.weight(1f), compacto = true)
            GuardarButton(post)
        }
        Row(Modifier.fillMaxWidth().clickable(interactionSource = interacciones, indication = null,
            role = Role.Button, onClickLabel = "Abrir publicación", onClick = onAbrir)
            .padding(top = 8.dp, bottom = 22.dp).heightIn(min = 48.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(post.title, style = MaterialTheme.typography.titleLarge, color = c.onSurface,
                maxLines = 3, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
            Icon(painterResource(R.drawable.ic_flecha), null, Modifier.size(20.dp), tint = c.primary)
        }
        HorizontalDivider(color = c.outlineVariant.copy(alpha = .65f))
    }
}

// Compatibilidad con pantallas de la primera fase; el muro actual usa FilaPublicacion.
@Composable
fun TarjetaPost(usuarioId: Int, titulo: String, texto: String, onClick: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Autor(usuarioId)
        Text(titulo, style = MaterialTheme.typography.titleLarge, maxLines = 3, overflow = TextOverflow.Ellipsis)
        HorizontalDivider(color = c.outlineVariant)
    }
}

@Composable
fun Cargando(texto: String = "Cargando publicaciones…") {
    val c = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth().padding(vertical = 20.dp).semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            Text(texto, style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant)
        }
        repeat(2) {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(36.dp).background(c.surfaceVariant, CircleShape))
                    Box(Modifier.width(110.dp).height(12.dp).background(c.surfaceVariant, RoundedCornerShape(4.dp)))
                }
                Box(Modifier.fillMaxWidth(.88f).height(17.dp).background(c.surfaceVariant, RoundedCornerShape(4.dp)))
                Box(Modifier.fillMaxWidth(.60f).height(17.dp).background(c.surfaceVariant, RoundedCornerShape(4.dp)))
                HorizontalDivider(color = c.outlineVariant.copy(alpha = .5f))
            }
        }
    }
}

@Composable
fun Vacio(titulo: String, texto: String, accion: String, onAccion: () -> Unit, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    Column(modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Image(painterResource(R.drawable.conversaciones_vacio), null, Modifier.size(136.dp, 98.dp))
        Text(titulo, style = MaterialTheme.typography.titleLarge, color = c.onSurface, textAlign = TextAlign.Center)
        Text(texto, style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant, textAlign = TextAlign.Center)
        TextButton(onClick = onAccion, modifier = Modifier.heightIn(min = 48.dp)) { Text(accion) }
    }
}

@Composable
fun ErrorConReintento(mensaje: String, reintentar: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth().padding(vertical = 24.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(R.drawable.conversaciones_vacio), null, Modifier.size(128.dp, 90.dp))
        Text("No pudimos cargar esto", style = MaterialTheme.typography.titleLarge, color = c.onSurface, textAlign = TextAlign.Center)
        Text(mensaje, style = MaterialTheme.typography.bodyMedium, color = c.onSurfaceVariant, textAlign = TextAlign.Center)
        GlassButton("Reintentar", reintentar, icon = R.drawable.ic_recargar, primary = true)
    }
}

@Composable
fun ErrorEnLinea(mensaje: String, modifier: Modifier = Modifier, reintentar: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Column(modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(mensaje, style = MaterialTheme.typography.bodyMedium, color = c.error)
        TextButton(onClick = reintentar, modifier = Modifier.heightIn(min = 48.dp), contentPadding = PaddingValues(0.dp)) {
            Icon(painterResource(R.drawable.ic_recargar), null, Modifier.size(19.dp))
            Spacer(Modifier.width(8.dp)); Text("Reintentar")
        }
    }
}

@Composable
fun Aviso(texto: String, icon: Int = R.drawable.ic_info, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    Row(modifier.fillMaxWidth().background(c.surfaceContainer, RoundedCornerShape(12.dp)).padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
        Icon(painterResource(icon), null, Modifier.size(19.dp), tint = c.primary)
        Text(texto, style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
    }
}

@Composable
fun Resultado(titulo: String, texto: String) {
    val c = MaterialTheme.colorScheme
    Column(Modifier.fillMaxWidth().background(c.primaryContainer.copy(alpha = .55f), RoundedCornerShape(16.dp))
        .padding(18.dp).semantics { liveRegion = LiveRegionMode.Polite }, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(painterResource(R.drawable.ic_check), null, Modifier.size(24.dp), tint = c.primary)
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = c.onPrimaryContainer)
        }
        Text(texto, style = MaterialTheme.typography.bodyMedium, color = c.onPrimaryContainer)
        Text("Simulación · no se guarda en el servidor.", style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
    }
}
