package com.example.myapplication.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CityWeather
import com.example.myapplication.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val cityName: String = "",
    val cityWeather: CityWeather? = null,
    val tempUnit: String = "C", // "C" or "F"
    val isLoading: Boolean = false
)

class DetailViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadCityWeather(cityName: String) {
        _uiState.update { it.copy(cityName = cityName, isLoading = true) }
        viewModelScope.launch {
            weatherRepository.getWeatherForCity(cityName).collect { weather ->
                _uiState.update {
                    it.copy(cityWeather = weather, isLoading = false)
                }
            }
        }
    }

    fun toggleTempUnit() {
        _uiState.update {
            it.copy(tempUnit = if (it.tempUnit == "C") "F" else "C")
        }
    }

    fun toggleFavorite() {
        val cityName = _uiState.value.cityName
        if (cityName.isNotBlank()) {
            viewModelScope.launch {
                weatherRepository.toggleFavoriteCity(cityName)
            }
        }
    }
}
