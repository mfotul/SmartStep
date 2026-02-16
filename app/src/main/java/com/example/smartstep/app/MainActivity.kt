package com.example.smartstep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartstep.app.navigation.NavigationRoot
import com.example.smartstep.app.navigation.NavigationRoute
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.isLoading
            }
        }

        enableEdgeToEdge()
        setContent {
            SmartStepTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                if (!state.isLoading) {
                    val startRoute = if (state.height == Int.MIN_VALUE)
                        NavigationRoute.ProfileScreen
                    else
                        NavigationRoute.HomeScreen

                    NavigationRoot(
                        homeRoute = startRoute
                    )
                }
            }
        }
    }
}