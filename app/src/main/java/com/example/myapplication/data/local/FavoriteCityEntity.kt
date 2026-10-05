package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String,
    val country: String,
    val temperatureC: Double,
    val condition: String,
    val iconRes: String,
    val isFavorite: Boolean = true
)
