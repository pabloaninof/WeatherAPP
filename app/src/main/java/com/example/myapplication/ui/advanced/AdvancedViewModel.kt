package com.example.myapplication.ui.advanced

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class WeatherStation(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val currentTempC: Double,
    val humidity: Int,
    val isOnline: Boolean = true
)

data class TemperatureHistory(
    val month: String,
    val avgTempC: Float,
    val rainfallMm: Float
)

data class AdvancedUiState(
    val isLocationPermissionGranted: Boolean = false,
    val selectedStation: WeatherStation? = null,
    val stations: List<WeatherStation> = emptyList(),
    val historicalData: List<TemperatureHistory> = emptyList(),
    val simulatedBarometricPressureHpa: Int = 1013,
    val simulatedAltitudeMeters: Int = 650
)

class AdvancedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AdvancedUiState())
    val uiState: StateFlow<AdvancedUiState> = _uiState.asStateFlow()

    init {
        loadStations()
        loadHistoricalData()
    }

    private fun loadStations() {
        val mockStations = listOf(
            WeatherStation(1, "Estación Madrid Retiro", 40.4168, -3.7038, 22.5, 45),
            WeatherStation(2, "Estación Barcelona Puerto", 41.3851, 2.1734, 20.2, 65),
            WeatherStation(3, "Estación Sevilla Giralda", 37.3891, -5.9845, 28.1, 35),
            WeatherStation(4, "Estación Valencia Costa", 39.4699, -0.3763, 21.0, 60),
            WeatherStation(5, "Estación Bilbao Ria", 43.2630, -2.9350, 17.8, 70)
        )
        _uiState.update {
            it.copy(
                stations = mockStations,
                selectedStation = mockStations.first()
            )
        }
    }

    private fun loadHistoricalData() {
        val history = listOf(
            TemperatureHistory("Ene", 10f, 45f),
            TemperatureHistory("Feb", 12f, 35f),
            TemperatureHistory("Mar", 15f, 30f),
            TemperatureHistory("Abr", 18f, 50f),
            TemperatureHistory("May", 22f, 25f),
            TemperatureHistory("Jun", 27f, 10f),
            TemperatureHistory("Jul", 31f, 5f),
            TemperatureHistory("Ago", 30f, 8f),
            TemperatureHistory("Sep", 25f, 20f),
            TemperatureHistory("Oct", 20f, 40f),
            TemperatureHistory("Nov", 14f, 55f),
            TemperatureHistory("Dic", 11f, 50f)
        )
        _uiState.update { it.copy(historicalData = history) }
    }

    fun onLocationPermissionResult(isGranted: Boolean) {
        _uiState.update { it.copy(isLocationPermissionGranted = isGranted) }
    }

    fun selectStation(station: WeatherStation) {
        _uiState.update { it.copy(selectedStation = station) }
    }
}
