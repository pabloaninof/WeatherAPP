package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.data.repository.WeatherRepositoryImpl
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.ui.navigation.AppNavigation
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val authRepository = AuthRepositoryImpl(database.userDao())
        val authViewModel = AuthViewModel(authRepository)
        val weatherRepository = WeatherRepositoryImpl(database.favoriteCityDao())

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        authViewModel = authViewModel,
                        authRepository = authRepository,
                        weatherRepository = weatherRepository
                    )
                }
            }
        }
    }
}
