@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)
package com.leon.murocampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import com.leon.murocampus.R

private val Manrope=FontFamily(
    Font(R.font.manrope,FontWeight.Normal,variationSettings=FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.manrope,FontWeight.Medium,variationSettings=FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.manrope,FontWeight.SemiBold,variationSettings=FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.manrope,FontWeight.Bold,variationSettings=FontVariation.Settings(FontVariation.weight(700))),
    Font(R.font.manrope,FontWeight.ExtraBold,variationSettings=FontVariation.Settings(FontVariation.weight(800)))
)
private fun tipo(size:Int,line:Int,weight:FontWeight=FontWeight.Normal)=TextStyle(fontFamily=Manrope,fontSize=size.sp,lineHeight=line.sp,fontWeight=weight,letterSpacing=if(size>=24)(-0.65).sp else 0.sp)
private val Tipografia=Typography(
    displaySmall=tipo(34,40,FontWeight.ExtraBold),headlineLarge=tipo(30,37,FontWeight.Bold),
    headlineMedium=tipo(26,33,FontWeight.Bold),headlineSmall=tipo(24,31,FontWeight.Bold),
    titleLarge=tipo(21,28,FontWeight.Bold),titleMedium=tipo(17,25,FontWeight.Bold),titleSmall=tipo(15,23,FontWeight.SemiBold),
    bodyLarge=tipo(16,26),bodyMedium=tipo(14,23),bodySmall=tipo(12,19),
    labelLarge=tipo(15,22,FontWeight.SemiBold),labelMedium=tipo(13,20,FontWeight.SemiBold),labelSmall=tipo(11,17,FontWeight.SemiBold)
)
private val Claro=lightColorScheme(
    primary=Color(0xFF28634F),onPrimary=Color.White,primaryContainer=Color(0xFFD8EBE0),onPrimaryContainer=Color(0xFF173E32),
    secondary=Color(0xFF93526F),onSecondary=Color.White,secondaryContainer=Color(0xFFF5E5ED),onSecondaryContainer=Color(0xFF713C55),
    tertiary=Color(0xFF536C75),background=Color(0xFFF4F8F6),onBackground=Color(0xFF203D34),
    surface=Color.White,onSurface=Color(0xFF203D34),surfaceVariant=Color(0xFFE8F0EB),onSurfaceVariant=Color(0xFF53675D),
    surfaceContainer=Color(0xFFEDF4EF),surfaceContainerLow=Color(0xFFF8FBF9),surfaceContainerHigh=Color(0xFFE4EEE7),
    outline=Color(0xFF7D9387),outlineVariant=Color(0xFFD7E3DA),
    error=Color(0xFFA63446),onError=Color.White,errorContainer=Color(0xFFFBE8EB),onErrorContainer=Color(0xFF7E2432)
)
private val Oscuro=darkColorScheme(
    primary=Color(0xFFA7D9BF),onPrimary=Color(0xFF153C2C),primaryContainer=Color(0xFF244F3B),onPrimaryContainer=Color(0xFFCDEBD9),
    secondary=Color(0xFFE7AEC7),onSecondary=Color(0xFF4D2237),secondaryContainer=Color(0xFF503545),onSecondaryContainer=Color(0xFFF8DCEA),
    tertiary=Color(0xFFB4CFD8),background=Color(0xFF101D18),onBackground=Color(0xFFE0EDE4),
    surface=Color(0xFF192A22),onSurface=Color(0xFFE0EDE4),surfaceVariant=Color(0xFF293D31),onSurfaceVariant=Color(0xFFB6C9BC),
    surfaceContainer=Color(0xFF1D3126),surfaceContainerLow=Color(0xFF15251C),surfaceContainerHigh=Color(0xFF2B4033),
    outline=Color(0xFF88A190),outlineVariant=Color(0xFF3B5342),
    error=Color(0xFFFFB0BA),onError=Color(0xFF590F20),errorContainer=Color(0xFF5C2934),onErrorContainer=Color(0xFFFFDDE2)
)
@Composable fun MuroCampusTheme(content:@Composable ()->Unit) {
    MaterialTheme(colorScheme=if(isSystemInDarkTheme())Oscuro else Claro,typography=Tipografia,content=content)
}
