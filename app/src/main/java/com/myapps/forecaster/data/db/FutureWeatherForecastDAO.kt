package com.myapps.forecaster.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapps.forecaster.data.network.response.FutureWeatherForecastResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface FutureWeatherForecastDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFutureWeatherForecast(futureWeatherForecastResponse: FutureWeatherForecastResponse)

    @Query("DELETE FROM future_weather")
    suspend fun deleteFutureWeatherForecast()

    @Query("SELECT * FROM future_weather")
    fun getWeatherForecastFuture(): Flow<FutureWeatherForecastResponse>
}