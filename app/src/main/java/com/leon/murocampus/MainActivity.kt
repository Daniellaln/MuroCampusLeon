package com.leon.murocampus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.material3.*
import com.leon.murocampus.ui.theme.MuroCampusTheme
class MainActivity:ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle=SystemBarStyle.dark(0xFF1D709F.toInt()))
        setContent { MuroCampusTheme { Scaffold(topBar={ TopAppBar(title={ Text("MuroCampus") },colors=TopAppBarDefaults.topAppBarColors(containerColor=MaterialTheme.colorScheme.primary,titleContentColor=MaterialTheme.colorScheme.onPrimary)) }) { _ -> } } }
    }
}
