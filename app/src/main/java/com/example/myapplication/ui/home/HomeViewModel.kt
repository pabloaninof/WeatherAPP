package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CityWeather
import com.example.myapplication.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val searchQuery: String = "",
    val featuredCities: List<CityWeather> = emptyList(),
    val searchResults: List<CityWeather> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCity: CityWeather? = null
)

class HomeViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadFeaturedCities()
    }

    private fun loadFeaturedCities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            weatherRepository.getFeaturedCities().collect { cities ->
                _uiState.update {
                    it.copy(
                        featuredCities = cities,
                        searchResults = if (it.searchQuery.isBlank()) cities else it.searchResults,
                        selectedCity = it.selectedCity ?: cities.firstOrNull(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            val results = weatherRepository.searchCities(query)
            _uiState.update { it.copy(searchResults = results) }
        }
    }

    fun toggleFavorite(cityName: String) {
        viewModelScope.launch {
            weatherRepository.toggleFavoriteCity(cityName)
        }
    }
}
