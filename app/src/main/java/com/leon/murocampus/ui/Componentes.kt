package com.leon.murocampus.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
@Composable fun TarjetaPost(usuarioId:Int,titulo:String,texto:String,onClick:()->Unit) {
    Card(onClick=onClick,modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
            Surface(color=MaterialTheme.colorScheme.primaryContainer,shape=RoundedCornerShape(10.dp)) {
                Text("Usuario $usuarioId",Modifier.padding(horizontal=9.dp,vertical=2.dp),style=MaterialTheme.typography.labelMedium,color=MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Text(titulo,style=MaterialTheme.typography.titleSmall,fontWeight=FontWeight.Bold,maxLines=2,overflow=TextOverflow.Ellipsis)
            Text(texto,style=MaterialTheme.typography.bodyMedium,maxLines=2,overflow=TextOverflow.Ellipsis,color=MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
@Composable fun Cargando(texto:String="Cargando publicaciones…") {
    Column(Modifier.fillMaxWidth().padding(32.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(12.dp)) {
        CircularProgressIndicator();Text(texto,style=MaterialTheme.typography.bodyMedium)
    }
}
@Composable fun ErrorConReintento(mensaje:String,reintentar:()->Unit) {
    Column(Modifier.fillMaxWidth().padding(16.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Surface(color=MaterialTheme.colorScheme.errorContainer,shape=RoundedCornerShape(12.dp)) {
            Text(mensaje,Modifier.fillMaxWidth().padding(16.dp),color=MaterialTheme.colorScheme.onErrorContainer)
        }
        Button(reintentar,Modifier.heightIn(min=48.dp)) { Text("Reintentar") }
    }
}
