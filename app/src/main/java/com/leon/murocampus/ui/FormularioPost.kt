package com.leon.murocampus.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.leon.murocampus.R
import com.leon.murocampus.model.*

@Composable fun FormularioPost(post:Post?,estado:UiState<Post>?,onVolver:()->Unit={},onDirty:(Boolean)->Unit={},enviar:(String,String)->Unit) {
    var titulo by rememberSaveable(post?.id) { mutableStateOf(post?.title ?: "") };var contenido by rememberSaveable(post?.id) { mutableStateOf(post?.body ?: "") }
    LaunchedEffect(titulo,contenido,estado) { onDirty(estado !is UiState.Exito && (titulo!=(post?.title ?: "") || contenido!=(post?.body ?: ""))) }
    val editando=post!=null;val cargando=estado==UiState.Cargando;val c=MaterialTheme.colorScheme
    LaunchedEffect(estado) { if(estado is UiState.Exito) { if(!editando) { titulo="";contenido="" } else { titulo=estado.datos.title;contenido=estado.datos.body } } }
    Box(Modifier.fillMaxSize()) {
        Image(painterResource(R.drawable.conversacion_rosa),null,Modifier.fillMaxWidth().height(170.dp),contentScale=ContentScale.Crop,alpha=.25f)
        Box(Modifier.fillMaxWidth().height(200.dp).background(Brush.verticalGradient(listOf(c.background.copy(alpha=.45f),c.background))))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=24.dp,vertical=20.dp),verticalArrangement=Arrangement.spacedBy(22.dp)) {
            Column(verticalArrangement=Arrangement.spacedBy(6.dp)) {
                Text("Autor de ejemplo",style=MaterialTheme.typography.labelSmall,color=c.onSurfaceVariant)
                Autor(post?.userId ?: 1)
            }
            OutlinedTextField(titulo,{ titulo=it },label={ Text("Título") },modifier=Modifier.fillMaxWidth(),enabled=!cargando,minLines=2,maxLines=5,shape=RoundedCornerShape(16.dp),
                keyboardOptions=KeyboardOptions(capitalization=KeyboardCapitalization.Sentences),colors=OutlinedTextFieldDefaults.colors(unfocusedContainerColor=c.surface,focusedContainerColor=c.surface))
            OutlinedTextField(contenido,{ contenido=it },label={ Text("Contenido") },modifier=Modifier.fillMaxWidth(),enabled=!cargando,minLines=6,maxLines=12,shape=RoundedCornerShape(16.dp),
                keyboardOptions=KeyboardOptions(capitalization=KeyboardCapitalization.Sentences),colors=OutlinedTextFieldDefaults.colors(unfocusedContainerColor=c.surface,focusedContainerColor=c.surface))
            GlassButton(if(cargando) { if(editando)"Guardando…" else "Publicando…" } else if(estado is UiState.Error)"Reintentar" else if(editando)"Guardar cambios" else "Publicar",{ enviar(titulo,contenido) },
                modifier=Modifier.fillMaxWidth(),icon=if(editando)R.drawable.ic_check else R.drawable.ic_enviar,primary=true,enabled=titulo.isNotBlank() && contenido.isNotBlank(),loading=cargando)
            if(estado is UiState.Error)Aviso(estado.mensaje)
            AnimatedVisibility(estado is UiState.Exito,enter=if(LocalMotionEnabled.current)fadeIn(tween(180))+expandVertically() else EnterTransition.None,exit=if(LocalMotionEnabled.current)fadeOut() else ExitTransition.None) {
                val respuesta=(estado as? UiState.Exito)?.datos
                if(respuesta!=null)Resultado(if(editando)"Cambios recibidos · id ${respuesta.id}" else "Publicado con id ${respuesta.id}",respuesta.title)
            }
            if(estado is UiState.Exito)GlassButton("Volver al muro",onVolver,modifier=Modifier.fillMaxWidth(),icon=R.drawable.ic_volver)
            if(estado !is UiState.Exito)Text("Práctica · no se guarda en el servidor",style=MaterialTheme.typography.bodySmall,color=c.onSurfaceVariant)
        }
    }
}
