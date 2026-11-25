package com.leon.murocampus.ui
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.viewmodel.PostsViewModel
@Composable fun PantallaCrear(vm:PostsViewModel,onVolver:()->Unit={},onDirty:(Boolean)->Unit={}) {
    val estado by vm.creacion.collectAsStateWithLifecycle()
    FormularioPost(null,estado,onVolver,onDirty,vm::publicar)
}
