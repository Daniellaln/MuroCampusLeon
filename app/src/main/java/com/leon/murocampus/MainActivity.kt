package com.leon.murocampus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leon.murocampus.data.PostRepositoryRemote
import com.leon.murocampus.data.remote.RetrofitClient
import com.leon.murocampus.viewmodel.*
import com.leon.murocampus.ui.*
import com.leon.murocampus.ui.theme.MuroCampusTheme
class MainActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle=SystemBarStyle.dark(0xFF1D709F.toInt()))
        val factory=PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
        setContent { MuroCampusTheme { val vm:PostsViewModel=viewModel(factory=factory);MuroCampusApp(vm) } }
    }
}
