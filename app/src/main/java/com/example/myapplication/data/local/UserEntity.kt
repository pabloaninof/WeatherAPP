package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val passwordHash: String,
    val preferredTempUnit: String = "C" // "C" for Celsius, "F" for Fahrenheit
)
