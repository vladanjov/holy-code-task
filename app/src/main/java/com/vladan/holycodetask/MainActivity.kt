package com.vladan.holycodetask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.vladan.holycodetask.core.navigation.HolyCodeTaskNavHost
import com.vladan.holycodetask.core.designsystem.HolyCodeTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HolyCodeTaskTheme {
                val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()
                val navController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize()) {
                    HolyCodeTaskNavHost(
                        navController = navController,
                        isOffline = isOffline,
                    )
                }
            }
        }
    }
}
