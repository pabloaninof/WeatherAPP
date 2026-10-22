package com.example.myapplication.ui.advanced

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

@Composable
fun AdvancedScreen(
    viewModel: AdvancedViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Text(
                    text = "Panel Avanzado y Estaciones",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Monitoreo meteorológico avanzado y estaciones de red",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Location Permission Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Column {
                                Text(text = "Permiso de Ubicación", fontWeight = FontWeight.Bold)
                                Text(
                                    text = if (uiState.isLocationPermissionGranted) "Concedido (GPS activo)" else "No concedido (Usando ubicación simulada)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Switch(
                            checked = uiState.isLocationPermissionGranted,
                            onCheckedChange = { viewModel.onLocationPermissionResult(it) }
                        )
                    }
                }
            }

            // Selected Station Overview
            if (uiState.selectedStation != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = uiState.selectedStation!!.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Badge(containerColor = if (uiState.selectedStation!!.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error) {
                                    Text(text = if (uiState.selectedStation!!.isOnline) "ONLINE" else "OFFLINE")
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Temperatura: ${uiState.selectedStation!!.currentTempC}°C")
                                Text("Humedad: ${uiState.selectedStation!!.humidity}%")
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Coordenadas: ${uiState.selectedStation!!.latitude}, ${uiState.selectedStation!!.longitude}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Stations List
            item {
                Text(
                    text = "Estaciones Meteorológicas Cercanas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(uiState.stations) { station ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { viewModel.selectStation(station) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = station.name, fontWeight = FontWeight.Bold)
                            Text(text = "Temp: ${station.currentTempC}°C | Humedad: ${station.humidity}%", style = MaterialTheme.typography.bodySmall)
                        }
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }

            // Historical Charts Data
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Historial Anual de Temperaturas y Lluvias",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.historicalData.forEach { history ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = history.month, fontWeight = FontWeight.Bold, modifier = Modifier.width(40.dp))
                                LinearProgressIndicator(
                                    progress = { history.avgTempC / 40f },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .padding(horizontal = 8.dp),
                                )
                                Text(text = "${history.avgTempC.toInt()}°C", modifier = Modifier.width(50.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
