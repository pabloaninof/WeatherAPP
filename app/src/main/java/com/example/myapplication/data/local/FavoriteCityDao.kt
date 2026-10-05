package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCityDao {

    @Query("SELECT * FROM favorite_cities ORDER BY cityName ASC")
    fun getAllFavoriteCities(): Flow<List<FavoriteCityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCity(city: FavoriteCityEntity): Long

    @Delete
    suspend fun deleteFavoriteCity(city: FavoriteCityEntity)

    @Query("DELETE FROM favorite_cities WHERE cityName = :cityName")
    suspend fun deleteFavoriteCityByName(cityName: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_cities WHERE cityName = :cityName LIMIT 1)")
    fun isCityFavorite(cityName: String): Flow<Boolean>
}
