package com.example.myapplication.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.FavoriteCityEntity
import com.example.myapplication.data.local.UserEntity
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: UserEntity? = null,
    val favoriteCities: List<FavoriteCityEntity> = emptyList(),
    val isDarkMode: Boolean = false,
    val tempUnit: String = "C", // "C" or "F"
    val notificationsEnabled: Boolean = true
)

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            authRepository.getCurrentUser(userId).collect { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        tempUnit = user?.preferredTempUnit ?: "C"
                    )
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            weatherRepository.getFavoriteCities().collect { favorites ->
                _uiState.update { it.copy(favoriteCities = favorites) }
            }
        }
    }

    fun toggleTempUnit() {
        val newUnit = if (_uiState.value.tempUnit == "C") "F" else "C"
        _uiState.update { it.copy(tempUnit = newUnit) }
        val currentUser = _uiState.value.user
        if (currentUser != null) {
            viewModelScope.launch {
                authRepository.updateUserPreferences(currentUser.copy(preferredTempUnit = newUnit))
            }
        }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleNotifications() {
        _uiState.update { it.copy(notificationsEnabled = !it.notificationsEnabled) }
    }

    fun removeFavorite(cityName: String) {
        viewModelScope.launch {
            weatherRepository.toggleFavoriteCity(cityName)
        }
    }
}
