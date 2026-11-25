package com.leon.murocampus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leon.murocampus.data.*
import com.leon.murocampus.data.PostRepositoryConCache
import com.leon.murocampus.data.local.MuroDatabase
import com.leon.murocampus.data.remote.RetrofitClient
import com.leon.murocampus.viewmodel.*
import com.leon.murocampus.ui.*
import com.leon.murocampus.ui.theme.MuroCampusTheme
class MainActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val bd=MuroDatabase.obtener(applicationContext)
        val factory=PostsViewModelFactory(PostRepositoryConCache(PostRepositoryRemote(RetrofitClient.api),bd),UsuarioRepositoryConCache(UsuarioRepositoryRemote(RetrofitClient.usuarioApi),bd))
        val guardadosFactory=GuardadosViewModelFactory(GuardadosPreferencesRepository(applicationContext))
        setContent { MuroCampusTheme { val vm:PostsViewModel=viewModel(factory=factory);MuroCampusApp(vm,viewModel(factory=guardadosFactory)) } }
    }
}
