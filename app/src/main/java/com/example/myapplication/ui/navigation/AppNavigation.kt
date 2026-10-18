package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.data.repository.WeatherRepository
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.ui.auth.LoginContent
import com.example.myapplication.ui.auth.RegisterContent
import com.example.myapplication.ui.detail.DetailScreen
import com.example.myapplication.ui.detail.DetailViewModel
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.home.HomeViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    weatherRepository: WeatherRepository
) {
    val authUiState by authViewModel.uiState.collectAsState()

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
                LoginContent(
                    uiState = authUiState,
                    onEmailChange = authViewModel::onEmailChange,
                    onPasswordChange = authViewModel::onPasswordChange,
                    onLoginClick = {
                        authViewModel.login()
                        if (authUiState.isLoggedIn) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            // 2. Destination: Register
            composable(Screen.Register.route) {
                RegisterContent(
                    uiState = authUiState,
                    onUsernameChange = authViewModel::onUsernameChange,
                    onEmailChange = authViewModel::onEmailChange,
                    onPasswordChange = authViewModel::onPasswordChange,
                    onRegisterClick = {
                        authViewModel.register()
                        if (authUiState.isLoggedIn) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // 3. Destination: Home
            composable(Screen.Home.route) {
                val homeViewModel = remember { HomeViewModel(weatherRepository) }
                HomeScreen(
                    viewModel = homeViewModel,
                    onCityClick = { cityName ->
                        navController.navigate(Screen.Detail.createRoute(cityName))
                    }
                )
            }

            // 4. Destination: Detail (passing parameter: cityName)
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("cityName") { type = NavType.StringType })
            ) { backStackEntry ->
                val cityName = backStackEntry.arguments?.getString("cityName") ?: "Madrid"
                val detailViewModel = remember { DetailViewModel(weatherRepository) }
                DetailScreen(
                    cityName = cityName,
                    viewModel = detailViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 5. Destination: Profile
            composable(Screen.Profile.route) {
                // Profile placeholder until Commit 8
            }

            // 6. Destination: Advanced
            composable(Screen.Advanced.route) {
                // Advanced placeholder until Commit 8
            }
        }
    }
}
