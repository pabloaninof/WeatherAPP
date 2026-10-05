package com.example.myapplication.data.model

enum class WeatherCondition(val description: String, val iconName: String) {
    SUNNY("Soleado", "ic_sunny"),
    CLEAR("Despejado", "ic_clear"),
    PARTLY_CLOUDY("Parcialmente Nublado", "ic_partly_cloudy"),
    CLOUDY("Nublado", "ic_cloudy"),
    RAINY("Lluvia", "ic_rainy"),
    HEAVY_RAIN("Lluvia Intensa", "ic_heavy_rain"),
    THUNDERSTORM("Tormenta", "ic_thunderstorm"),
    SNOWY("Nieve", "ic_snowy"),
    WINDY("Viento", "ic_windy")
}

data class HourlyForecast(
    val time: String,
    val tempC: Double,
    val condition: WeatherCondition,
    val precipitationChance: Int
)

data class DailyForecast(
    val dayName: String,
    val maxTempC: Double,
    val minTempC: Double,
    val condition: WeatherCondition,
    val precipitationChance: Int
)

data class CityWeather(
    val id: Int,
    val cityName: String,
    val country: String,
    val currentTempC: Double,
    val feelsLikeC: Double,
    val highTempC: Double,
    val lowTempC: Double,
    val condition: WeatherCondition,
    val humidity: Int,
    val windSpeedKmH: Double,
    val uvIndex: Int,
    val airPressureHpa: Int,
    val hourlyForecasts: List<HourlyForecast>,
    val dailyForecasts: List<DailyForecast>,
    val isFavorite: Boolean = false
)
