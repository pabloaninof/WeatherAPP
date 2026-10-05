package com.example.myapplication.data.repository

import com.example.myapplication.data.local.FavoriteCityDao
import com.example.myapplication.data.local.FavoriteCityEntity
import com.example.myapplication.data.model.CityWeather
import com.example.myapplication.data.model.DailyForecast
import com.example.myapplication.data.model.HourlyForecast
import com.example.myapplication.data.model.WeatherCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface WeatherRepository {
    fun getWeatherForCity(cityName: String): Flow<CityWeather?>
    fun getFeaturedCities(): Flow<List<CityWeather>>
    suspend fun searchCities(query: String): List<CityWeather>
    fun getFavoriteCities(): Flow<List<FavoriteCityEntity>>
    suspend fun toggleFavoriteCity(cityName: String)
}

class WeatherRepositoryImpl(
    private val favoriteCityDao: FavoriteCityDao
) : WeatherRepository {

    private val mockCities = listOf(
        CityWeather(
            id = 1,
            cityName = "Madrid",
            country = "España",
            currentTempC = 22.0,
            feelsLikeC = 21.5,
            highTempC = 25.0,
            lowTempC = 14.0,
            condition = WeatherCondition.SUNNY,
            humidity = 45,
            windSpeedKmH = 12.0,
            uvIndex = 6,
            airPressureHpa = 1015,
            hourlyForecasts = listOf(
                HourlyForecast("10:00", 20.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("13:00", 24.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("16:00", 25.0, WeatherCondition.PARTLY_CLOUDY, 10),
                HourlyForecast("19:00", 22.0, WeatherCondition.CLEAR, 0),
                HourlyForecast("22:00", 18.0, WeatherCondition.CLEAR, 0)
            ),
            dailyForecasts = listOf(
                DailyForecast("Hoy", 25.0, 14.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Mañana", 24.0, 13.0, WeatherCondition.PARTLY_CLOUDY, 10),
                DailyForecast("Miércoles", 22.0, 12.0, WeatherCondition.RAINY, 60),
                DailyForecast("Jueves", 20.0, 11.0, WeatherCondition.CLOUDY, 20),
                DailyForecast("Viernes", 23.0, 13.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Sábado", 26.0, 15.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Domingo", 27.0, 16.0, WeatherCondition.SUNNY, 0)
            )
        ),
        CityWeather(
            id = 2,
            cityName = "Barcelona",
            country = "España",
            currentTempC = 20.0,
            feelsLikeC = 20.0,
            highTempC = 23.0,
            lowTempC = 16.0,
            condition = WeatherCondition.PARTLY_CLOUDY,
            humidity = 65,
            windSpeedKmH = 18.0,
            uvIndex = 5,
            airPressureHpa = 1013,
            hourlyForecasts = listOf(
                HourlyForecast("10:00", 19.0, WeatherCondition.PARTLY_CLOUDY, 10),
                HourlyForecast("13:00", 22.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("16:00", 23.0, WeatherCondition.PARTLY_CLOUDY, 15),
                HourlyForecast("19:00", 21.0, WeatherCondition.CLEAR, 0),
                HourlyForecast("22:00", 18.0, WeatherCondition.CLEAR, 0)
            ),
            dailyForecasts = listOf(
                DailyForecast("Hoy", 23.0, 16.0, WeatherCondition.PARTLY_CLOUDY, 15),
                DailyForecast("Mañana", 22.0, 15.0, WeatherCondition.RAINY, 50),
                DailyForecast("Miércoles", 21.0, 14.0, WeatherCondition.CLOUDY, 30),
                DailyForecast("Jueves", 23.0, 15.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Viernes", 24.0, 16.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Sábado", 25.0, 17.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Domingo", 24.0, 16.0, WeatherCondition.PARTLY_CLOUDY, 10)
            )
        ),
        CityWeather(
            id = 3,
            cityName = "Sevilla",
            country = "España",
            currentTempC = 28.0,
            feelsLikeC = 29.0,
            highTempC = 31.0,
            lowTempC = 18.0,
            condition = WeatherCondition.SUNNY,
            humidity = 35,
            windSpeedKmH = 10.0,
            uvIndex = 8,
            airPressureHpa = 1018,
            hourlyForecasts = listOf(
                HourlyForecast("10:00", 24.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("13:00", 29.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("16:00", 31.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("19:00", 28.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("22:00", 23.0, WeatherCondition.CLEAR, 0)
            ),
            dailyForecasts = listOf(
                DailyForecast("Hoy", 31.0, 18.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Mañana", 32.0, 19.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Miércoles", 30.0, 18.0, WeatherCondition.PARTLY_CLOUDY, 0),
                DailyForecast("Jueves", 29.0, 17.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Viernes", 31.0, 18.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Sábado", 33.0, 20.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Domingo", 34.0, 21.0, WeatherCondition.SUNNY, 0)
            )
        ),
        CityWeather(
            id = 4,
            cityName = "Londres",
            country = "Reino Unido",
            currentTempC = 14.0,
            feelsLikeC = 13.0,
            highTempC = 16.0,
            lowTempC = 9.0,
            condition = WeatherCondition.RAINY,
            humidity = 80,
            windSpeedKmH = 22.0,
            uvIndex = 3,
            airPressureHpa = 1008,
            hourlyForecasts = listOf(
                HourlyForecast("10:00", 12.0, WeatherCondition.RAINY, 80),
                HourlyForecast("13:00", 15.0, WeatherCondition.CLOUDY, 40),
                HourlyForecast("16:00", 16.0, WeatherCondition.RAINY, 70),
                HourlyForecast("19:00", 14.0, WeatherCondition.CLOUDY, 30),
                HourlyForecast("22:00", 11.0, WeatherCondition.CLOUDY, 20)
            ),
            dailyForecasts = listOf(
                DailyForecast("Hoy", 16.0, 9.0, WeatherCondition.RAINY, 80),
                DailyForecast("Mañana", 15.0, 8.0, WeatherCondition.CLOUDY, 30),
                DailyForecast("Miércoles", 17.0, 10.0, WeatherCondition.PARTLY_CLOUDY, 20),
                DailyForecast("Jueves", 14.0, 7.0, WeatherCondition.HEAVY_RAIN, 90),
                DailyForecast("Viernes", 15.0, 8.0, WeatherCondition.RAINY, 60),
                DailyForecast("Sábado", 16.0, 9.0, WeatherCondition.PARTLY_CLOUDY, 10),
                DailyForecast("Domingo", 18.0, 11.0, WeatherCondition.SUNNY, 0)
            )
        ),
        CityWeather(
            id = 5,
            cityName = "Nueva York",
            country = "EE. UU.",
            currentTempC = 18.0,
            feelsLikeC = 17.5,
            highTempC = 21.0,
            lowTempC = 12.0,
            condition = WeatherCondition.PARTLY_CLOUDY,
            humidity = 55,
            windSpeedKmH = 15.0,
            uvIndex = 5,
            airPressureHpa = 1016,
            hourlyForecasts = listOf(
                HourlyForecast("10:00", 16.0, WeatherCondition.PARTLY_CLOUDY, 10),
                HourlyForecast("13:00", 20.0, WeatherCondition.SUNNY, 0),
                HourlyForecast("16:00", 21.0, WeatherCondition.PARTLY_CLOUDY, 20),
                HourlyForecast("19:00", 18.0, WeatherCondition.CLEAR, 0),
                HourlyForecast("22:00", 15.0, WeatherCondition.CLEAR, 0)
            ),
            dailyForecasts = listOf(
                DailyForecast("Hoy", 21.0, 12.0, WeatherCondition.PARTLY_CLOUDY, 20),
                DailyForecast("Mañana", 22.0, 13.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Miércoles", 19.0, 11.0, WeatherCondition.THUNDERSTORM, 75),
                DailyForecast("Jueves", 18.0, 10.0, WeatherCondition.RAINY, 50),
                DailyForecast("Viernes", 20.0, 12.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Sábado", 23.0, 14.0, WeatherCondition.SUNNY, 0),
                DailyForecast("Domingo", 24.0, 15.0, WeatherCondition.PARTLY_CLOUDY, 10)
            )
        )
    )

    override fun getWeatherForCity(cityName: String): Flow<CityWeather?> {
        val city = mockCities.find { it.cityName.equals(cityName, ignoreCase = true) }
            ?: mockCities.first()

        return favoriteCityDao.isCityFavorite(city.cityName).map { isFav ->
            city.copy(isFavorite = isFav)
        }
    }

    override fun getFeaturedCities(): Flow<List<CityWeather>> {
        return favoriteCityDao.getAllFavoriteCities().map { favList ->
            val favNames = favList.map { it.cityName }.toSet()
            mockCities.map { city ->
                city.copy(isFavorite = favNames.contains(city.cityName))
            }
        }
    }

    override suspend fun searchCities(query: String): List<CityWeather> {
        if (query.isBlank()) return mockCities
        return mockCities.filter {
            it.cityName.contains(query, ignoreCase = true) ||
                    it.country.contains(query, ignoreCase = true)
        }
    }

    override fun getFavoriteCities(): Flow<List<FavoriteCityEntity>> {
        return favoriteCityDao.getAllFavoriteCities()
    }

    override suspend fun toggleFavoriteCity(cityName: String) {
        val city = mockCities.find { it.cityName.equals(cityName, ignoreCase = true) } ?: return
        val existingFavs = favoriteCityDao.getAllFavoriteCities()
        // Delete or insert
        favoriteCityDao.deleteFavoriteCityByName(city.cityName)
        // If it wasn't favorite, insert it
        favoriteCityDao.insertFavoriteCity(
            FavoriteCityEntity(
                cityName = city.cityName,
                country = city.country,
                temperatureC = city.currentTempC,
                condition = city.condition.description,
                iconRes = city.condition.iconName,
                isFavorite = true
            )
        )
    }
}
