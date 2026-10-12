package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Iniciar Sesión")
    object Register : Screen("register", "Registro")
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Detail : Screen("detail/{cityName}", "Detalle Clima") {
        fun createRoute(cityName: String) = "detail/$cityName"
    }
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
    object Advanced : Screen("advanced", "Avanzado", Icons.Default.Science)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Profile,
    Screen.Advanced
)
