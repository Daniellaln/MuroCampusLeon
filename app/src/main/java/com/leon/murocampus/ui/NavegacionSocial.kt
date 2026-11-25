package com.leon.murocampus.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.leon.murocampus.R

@Composable
fun NavegacionSocial(seleccion: Int, onMuro: () -> Unit, onBuscar: () -> Unit, onPublicar: () -> Unit, onGuardadas: () -> Unit) {
    val c = MaterialTheme.colorScheme
    Surface(modifier = Modifier.navigationBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(30.dp), color = c.surface.copy(alpha = .94f), contentColor = c.onSurface,
        shadowElevation = 7.dp, border = BorderStroke(1.dp, c.outlineVariant.copy(alpha = .45f))) {
        Row(Modifier.padding(5.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            val nombres = listOf("Muro", "Buscar", "Publicar", "Guardadas")
            val iconos = listOf(R.drawable.ic_inicio, R.drawable.ic_buscar, R.drawable.ic_agregar, R.drawable.ic_guardar)
            val acciones = listOf(onMuro, onBuscar, onPublicar, onGuardadas)
            nombres.forEachIndexed { i, nombre ->
                val seleccionado = seleccion == i
                val interacciones = remember { MutableInteractionSource() }
                val presionado by interacciones.collectIsPressedAsState()
                val escala by animateFloatAsState(if (presionado) .94f else 1f,
                    if (LocalMotionEnabled.current) spring(.9f, 750f) else snap(), label = "navegación social")
                Column(Modifier.weight(1f).heightIn(min = 56.dp).graphicsLayer { scaleX = escala; scaleY = escala }
                    .clip(RoundedCornerShape(25.dp)).background(if (seleccionado) c.primaryContainer.copy(alpha = .7f) else Color.Transparent)
                    .selectable(selected = seleccionado, onClick = acciones[i], role = if (i == 2) Role.Button else Role.Tab,
                        interactionSource = interacciones, indication = null)
                    .semantics { contentDescription = if (i == 1) "Buscar publicaciones" else nombre }
                    .padding(horizontal = 2.dp, vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(painterResource(iconos[i]), null, Modifier.size(23.dp),
                        tint = if (seleccionado || i == 2) c.primary else c.onSurfaceVariant)
                    Text(nombre, style = MaterialTheme.typography.labelSmall, maxLines = 1,
                        color = if (seleccionado || i == 2) c.primary else c.onSurfaceVariant)
                }
            }
        }
    }
}
