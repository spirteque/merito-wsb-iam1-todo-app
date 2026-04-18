package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.todoapp.data.repository.AuthRepository
import com.example.todoapp.ui.navigation.AppRoot
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Jedyna Activity w aplikacji (Single Activity Architecture).
 * Cała nawigacja odbywa się przez NavHost wewnątrz AppRoot.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot(authRepository = authRepository)
        }
    }
}