package com.myapps.forecaster.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapps.forecaster.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow

// Used to get data from database (DAO always is interface when we use Room library)

@Dao
interface CurrentWeatherDAO {

    // Because we have only one current weather (constant id) there always will be conflict
    // So we replace the previous current weather entry with a newer one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeatherResponse: CurrentWeatherResponse)

    @Query("DELETE FROM current_weather")
    suspend fun deleteCurrentWeather()

    @Query("SELECT * FROM current_weather")
    fun getWeatherCurrent(): Flow<CurrentWeatherResponse>
}