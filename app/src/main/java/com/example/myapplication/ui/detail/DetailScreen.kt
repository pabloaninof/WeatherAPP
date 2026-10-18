package com.example.myapplication.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.DailyForecast
import com.example.myapplication.ui.home.getWeatherIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cityName: String,
    viewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(cityName) {
        viewModel.loadCityWeather(cityName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.cityName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Temperature Unit Toggle
                    TextButton(onClick = { viewModel.toggleTempUnit() }) {
                        Text("°${uiState.tempUnit}", fontWeight = FontWeight.Bold)
                    }
                    // Favorite Toggle
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        val isFav = uiState.cityWeather?.isFavorite == true
                        Icon(
                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading || uiState.cityWeather == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val weather = uiState.cityWeather!!
            val tempMultiplier = if (uiState.tempUnit == "F") 1.8 else 1.0
            val tempOffset = if (uiState.tempUnit == "F") 32.0 else 0.0

            fun formatTemp(celsius: Double) = "${(celsius * tempMultiplier + tempOffset).toInt()}°${uiState.tempUnit}"

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main weather card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = getWeatherIcon(weather.condition),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = formatTemp(weather.currentTempC),
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = weather.condition.description,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Sensación térmica: ${formatTemp(weather.feelsLikeC)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Máx: ${formatTemp(weather.highTempC)} | Mín: ${formatTemp(weather.lowTempC)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Details Grid (Humidity, Wind, UV, Pressure)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WeatherDetailCard(
                            modifier = Modifier.weight(1f),
                            title = "Humedad",
                            value = "${weather.humidity}%",
                            icon = Icons.Default.WaterDrop
                        )
                        WeatherDetailCard(
                            modifier = Modifier.weight(1f),
                            title = "Viento",
                            value = "${weather.windSpeedKmH} km/h",
                            icon = Icons.Default.Air
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WeatherDetailCard(
                            modifier = Modifier.weight(1f),
                            title = "Índice UV",
                            value = "${weather.uvIndex}",
                            icon = Icons.Default.WbSunny
                        )
                        WeatherDetailCard(
                            modifier = Modifier.weight(1f),
                            title = "Presión",
                            value = "${weather.airPressureHpa} hPa",
                            icon = Icons.Default.Speed
                        )
                    }
                }

                // Daily Forecast
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pronóstico de los próximos días",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(weather.dailyForecasts) { daily ->
                    DailyForecastItem(daily = daily, formatTemp = ::formatTemp)
                }
            }
        }
    }
}

@Composable
fun WeatherDetailCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DailyForecastItem(daily: DailyForecast, formatTemp: (Double) -> String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = daily.dayName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = getWeatherIcon(daily.condition), contentDescription = null, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
                Text(text = "${daily.precipitationChance}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = formatTemp(daily.maxTempC), fontWeight = FontWeight.Bold)
                Text(text = formatTemp(daily.minTempC), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
