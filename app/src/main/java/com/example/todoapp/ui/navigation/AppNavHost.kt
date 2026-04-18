package com.example.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/**
 * NavHost — definiuje graf nawigacji całej aplikacji.
 * darkMode i onDarkModeToggle przekazujemy z AppRoot do SettingsScreen.
 */
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
            // LoginScreen — placeholder
        }

        composable(Destination.Register.route) {
            // RegisterScreen — placeholder
        }

        composable(Destination.Home.route) {
            // HomeScreen — placeholder
        }

        composable(Destination.Settings.route) {
            // SettingsScreen — placeholder
        }

        composable(Destination.Contact.route) {
            // ContactScreen — placeholder
        }

        composable(Destination.Map.route) {
            // MapScreen — placeholder
        }
    }
}