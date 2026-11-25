package com.leon.murocampus.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.leon.murocampus.R
import com.leon.murocampus.model.Post
import com.leon.murocampus.model.Usuario

val LocalAutores = staticCompositionLocalOf<Map<Int, Usuario>> { emptyMap() }
val LocalGuardados = staticCompositionLocalOf<List<Post>> { emptyList() }
val LocalGuardar = staticCompositionLocalOf<(Post) -> Unit> { {} }

internal fun inicialesAutor(nombre: String?, id: Int): String {
    val palabras = nombre.orEmpty().trim().split(Regex("\\s+"))
        .filter { it.isNotBlank() && it !in listOf("Dr.", "Mrs.", "Mr.", "Ms.") }
    return if (palabras.isEmpty()) id.toString()
    else palabras.take(2).joinToString("") { it.first().uppercaseChar().toString() }
}

@Composable
fun GuardarButton(post: Post, modifier: Modifier = Modifier) {
    val guardado = LocalGuardados.current.any { it.id == post.id }
    val guardar = LocalGuardar.current
    val haptico = LocalHapticFeedback.current
    val interacciones = remember { MutableInteractionSource() }
    val presionado by interacciones.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (presionado) .87f else 1f,
        animationSpec = if (LocalMotionEnabled.current) spring(.72f, 620f) else snap(),
        label = "guardar lectura"
    )
    val c = MaterialTheme.colorScheme
    FilledTonalIconToggleButton(
        checked = guardado,
        onCheckedChange = {
            haptico.performHapticFeedback(if (guardado) HapticFeedbackType.ToggleOff else HapticFeedbackType.ToggleOn)
            guardar(post)
        },
        interactionSource = interacciones,
        colors = IconButtonDefaults.filledTonalIconToggleButtonColors(
            containerColor = Color.Transparent,
            contentColor = c.onSurfaceVariant,
            checkedContainerColor = c.secondaryContainer,
            checkedContentColor = c.onSecondaryContainer
        ),
        modifier = modifier.size(48.dp).graphicsLayer { scaleX = escala; scaleY = escala }
            .semantics {
                contentDescription = if (guardado) "Quitar de guardadas" else "Guardar para leer"
                stateDescription = if (guardado) "Guardada en este dispositivo" else "Sin guardar"
            }
    ) {
        Icon(painterResource(if (guardado) R.drawable.ic_guardado else R.drawable.ic_guardar), null, Modifier.size(23.dp))
    }
}
