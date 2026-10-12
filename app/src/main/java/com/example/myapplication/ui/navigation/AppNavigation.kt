package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    onLoginSuccess: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    onCityClick: (String) -> Unit = {}
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Destination: Login
            composable(Screen.Login.route) {
                // Placeholder until Commit 6 implementation
                onLoginSuccess()
            }

            // 2. Destination: Register
            composable(Screen.Register.route) {
                // Placeholder until Commit 6 implementation
                onRegisterSuccess()
            }

            // 3. Destination: Home
            composable(Screen.Home.route) {
                // Placeholder until Commit 7 implementation
            }

            // 4. Destination: Detail (passing parameter: cityName)
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("cityName") { type = NavType.StringType })
            ) { backStackEntry ->
                val cityName = backStackEntry.arguments?.getString("cityName") ?: "Madrid"
                // Placeholder until Commit 7 implementation
            }

            // 5. Destination: Profile
            composable(Screen.Profile.route) {
                // Placeholder until Commit 8 implementation
            }

            // 6. Destination: Advanced
            composable(Screen.Advanced.route) {
                // Placeholder until Commit 8 implementation
            }
        }
    }
}
