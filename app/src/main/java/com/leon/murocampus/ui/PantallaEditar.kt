package com.leon.murocampus.ui
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.model.Post
import com.leon.murocampus.viewmodel.PostsViewModel
@Composable fun PantallaEditar(vm:PostsViewModel,post:Post,onVolver:()->Unit={},onDirty:(Boolean)->Unit={}) {
    val estado by vm.edicion.collectAsStateWithLifecycle()
    FormularioPost(post,estado,onVolver,onDirty) { titulo,cuerpo -> vm.editar(post.id,titulo,cuerpo) }
}
