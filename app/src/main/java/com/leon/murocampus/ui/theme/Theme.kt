package com.leon.murocampus.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val Colores=lightColorScheme(primary=Color(0xFF1D709F),onPrimary=Color.White,primaryContainer=Color(0xFFD8EDF8),onPrimaryContainer=Color(0xFF124360),background=Color(0xFFFAFAFE),surface=Color.White,onSurface=Color(0xFF20242B),onSurfaceVariant=Color(0xFF505B68),surfaceVariant=Color(0xFFF0F3F8),error=Color(0xFFAE2535),errorContainer=Color(0xFFFFE7EB),onErrorContainer=Color(0xFF7B1B29))
@Composable fun MuroCampusTheme(content:@Composable ()->Unit) { MaterialTheme(colorScheme=Colores,typography=Typography(),content=content) }
