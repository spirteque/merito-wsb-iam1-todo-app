package com.example.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoapp.ui.auth.LoginScreen
import com.example.todoapp.ui.auth.RegisterScreen
import com.example.todoapp.ui.contact.ContactScreen
import com.example.todoapp.ui.home.HomeScreen
import com.example.todoapp.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    darkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Destination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Destination.Register.route)
                }
            )
        }

        composable(Destination.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Destination.Home.route) {
            HomeScreen()
        }

        composable(Destination.Settings.route) {
            SettingsScreen(
                darkMode = darkMode,
                onDarkModeToggle = onDarkModeToggle,
                onSignOut = {
                    navController.navigate(Destination.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Destination.Contact.route) {
            ContactScreen()
        }

        composable(Destination.Map.route) {
            // placeholder
        }
    }
}