package com.example.todoapp.ui.navigation

/**
 * Definicja wszystkich ekranów aplikacji jako sealed class.
 * route to unikalny string identyfikujący ekran w NavHost.
 */
sealed class Destination(val route: String) {
    object Login : Destination("login")
    object Register : Destination("register")
    object Home : Destination("home")
    object Settings : Destination("settings")
    object Contact : Destination("contact")
    object Map : Destination("map")
}