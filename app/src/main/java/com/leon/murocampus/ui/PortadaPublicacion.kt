package com.leon.murocampus.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.leon.murocampus.R
import com.leon.murocampus.model.Post

fun portada(id:Int)=if(id%4==0)R.drawable.conversacion_rosa else R.drawable.conversacion_verde
@Composable fun PortadaPublicacion(post:Post,onClick:()->Unit) {
    val c=MaterialTheme.colorScheme;val interactions=remember { MutableInteractionSource() };val pressed by interactions.collectIsPressedAsState()
    val scale by animateFloatAsState(if(pressed).965f else 1f,if(LocalMotionEnabled.current)spring(.8f,650f) else snap(),label="abrir conversación")
    Card(onClick=onClick,interactionSource=interactions,shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=c.surface),
        modifier=Modifier.fillMaxWidth().graphicsLayer { scaleX=scale;scaleY=scale },elevation=CardDefaults.cardElevation(defaultElevation=1.dp)) {
        Box(Modifier.fillMaxWidth().height(104.dp)) {
            Image(painterResource(portada(post.id)),null,Modifier.matchParentSize(),contentScale=ContentScale.Crop)
            Box(Modifier.align(Alignment.BottomEnd).padding(9.dp).size(30.dp).background(Color.White.copy(alpha=.86f),CircleShape),contentAlignment=Alignment.Center) {
                Icon(painterResource(R.drawable.ic_flecha),null,Modifier.size(17.dp),tint=Color(0xFF28634F))
            }
        }
        Column(Modifier.padding(horizontal=14.dp,vertical=14.dp),verticalArrangement=Arrangement.spacedBy(9.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                Box(Modifier.size(7.dp).background(if(post.id%4==0)c.secondary else c.primary,CircleShape))
                Text("Usuario ${post.userId}",style=MaterialTheme.typography.labelSmall,color=c.onSurfaceVariant)
            }
            Text(post.title,style=MaterialTheme.typography.titleSmall,maxLines=2,overflow=TextOverflow.Ellipsis,modifier=Modifier.heightIn(min=46.dp))
        }
    }
}
