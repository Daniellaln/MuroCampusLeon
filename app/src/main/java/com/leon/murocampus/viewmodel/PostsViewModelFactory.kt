package com.leon.murocampus.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.leon.murocampus.data.PostRepository
import com.leon.murocampus.data.UsuarioRepository
class PostsViewModelFactory(private val repo:PostRepository,private val usuariosRepo:UsuarioRepository?=null):ViewModelProvider.Factory {
    override fun <T:ViewModel> create(modelClass:Class<T>,extras:CreationExtras):T {
        require(modelClass.isAssignableFrom(PostsViewModel::class.java))
        @Suppress("UNCHECKED_CAST")
        return PostsViewModel(repo,usuariosRepo,extras.createSavedStateHandle()) as T
    }
    override fun <T:ViewModel> create(modelClass:Class<T>):T {
        require(modelClass.isAssignableFrom(PostsViewModel::class.java))
        @Suppress("UNCHECKED_CAST")
        return PostsViewModel(repo,usuariosRepo) as T
    }
}
