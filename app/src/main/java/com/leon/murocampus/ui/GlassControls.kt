package com.leon.murocampus.ui

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp

val LocalMotionEnabled=staticCompositionLocalOf { true }
@Composable fun MotionSettings(content:@Composable ()->Unit) {
    val resolver=LocalContext.current.contentResolver
    fun enabled()=Settings.Global.getFloat(resolver,Settings.Global.ANIMATOR_DURATION_SCALE,1f)>0f
    var motion by remember { mutableStateOf(enabled()) }
    DisposableEffect(resolver) {
        val observer=object:ContentObserver(Handler(Looper.getMainLooper())) { override fun onChange(selfChange:Boolean) { motion=enabled() } }
        resolver.registerContentObserver(Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE),false,observer)
        onDispose { resolver.unregisterContentObserver(observer) }
    }
    CompositionLocalProvider(LocalMotionEnabled provides motion,content=content)
}

/** Vidrio inspirado: capas transparentes, borde de luz y deformación al tacto.
 * No se desenfoca el texto ni se afirma muestreo/refracción del fondo. */
@Composable fun GlassButton(text:String,onClick:()->Unit,modifier:Modifier=Modifier,icon:Int?=null,primary:Boolean=false,enabled:Boolean=true,loading:Boolean=false) {
    val colors=MaterialTheme.colorScheme
    val interactions=remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val amount by animateFloatAsState(if(pressed)1f else 0f,if(LocalMotionEnabled.current)spring(dampingRatio=.78f,stiffness=680f) else snap(),label="presión del vidrio")
    val shape=RoundedCornerShape(28.dp)
    val light=colors.surface.luminance()>.5f
    val tint=if(primary)colors.primaryContainer else colors.surface
    val brush=Brush.linearGradient(listOf(colors.surface.copy(alpha=if(light).94f else .84f),tint.copy(alpha=.64f+amount*.16f),tint.copy(alpha=.92f)))
    Button(onClick=onClick,enabled=enabled && !loading,interactionSource=interactions,shape=shape,
        colors=ButtonDefaults.buttonColors(containerColor=Color.Transparent,contentColor=if(primary)colors.onPrimaryContainer else colors.primary,disabledContainerColor=colors.surfaceVariant.copy(alpha=.7f),disabledContentColor=colors.onSurfaceVariant),
        border=BorderStroke(1.dp,Brush.linearGradient(listOf(if(light)Color.White else colors.outline,colors.outlineVariant.copy(alpha=.45f),colors.surface))),
        contentPadding=PaddingValues(horizontal=22.dp,vertical=14.dp),
        modifier=modifier.heightIn(min=52.dp).graphicsLayer { scaleX=1f-amount*.035f;scaleY=1f-amount*.065f;rotationZ=amount*.15f }
            .shadow(if(pressed)2.dp else 7.dp,shape,ambientColor=colors.primary.copy(alpha=.09f),spotColor=colors.primary.copy(alpha=.13f))
            .background(brush,shape).semantics { if(loading)stateDescription="Cargando" }) {
        if(loading)CircularProgressIndicator(Modifier.size(20.dp),strokeWidth=2.dp,color=colors.primary)
        else if(icon!=null)Icon(painterResource(icon),null,Modifier.size(21.dp))
        if(icon!=null || loading)Spacer(Modifier.width(10.dp))
        Text(text,style=MaterialTheme.typography.labelLarge)
    }
}

@Composable fun GlassIconButton(icon:Int,description:String,onClick:()->Unit,modifier:Modifier=Modifier,enabled:Boolean=true) {
    val interactions=remember { MutableInteractionSource() };val pressed by interactions.collectIsPressedAsState()
    val scale by animateFloatAsState(if(pressed).91f else 1f,if(LocalMotionEnabled.current)spring(.8f,800f) else snap(),label="presión del icono")
    val c=MaterialTheme.colorScheme
    FilledTonalIconButton(onClick=onClick,enabled=enabled,interactionSource=interactions,
        colors=IconButtonDefaults.filledTonalIconButtonColors(containerColor=c.surface.copy(alpha=.7f),contentColor=c.primary),
        modifier=modifier.size(48.dp).graphicsLayer { scaleX=scale;scaleY=scale }
            .shadow(3.dp,CircleShape,ambientColor=c.primary.copy(alpha=.07f),spotColor=c.primary.copy(alpha=.10f))
            .background(Brush.linearGradient(listOf(c.surface,c.secondaryContainer.copy(alpha=.30f),c.primaryContainer.copy(alpha=.3f))),CircleShape)
            .border(1.dp,Brush.linearGradient(listOf(c.surface,c.outlineVariant.copy(alpha=.35f),c.surface)),CircleShape)) { Icon(painterResource(icon),description,Modifier.size(22.dp)) }
}
