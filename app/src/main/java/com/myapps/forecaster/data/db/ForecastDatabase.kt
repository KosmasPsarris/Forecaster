package com.myapps.forecaster.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myapps.forecaster.data.network.response.CurrentWeatherResponse
import com.myapps.forecaster.data.network.response.FutureWeatherForecastResponse
import com.myapps.forecaster.util.ListTypeConverter


@Database(
    entities = [CurrentWeatherResponse::class, FutureWeatherForecastResponse::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(ListTypeConverter::class)
abstract class ForecastDatabase : RoomDatabase(){
    // The forecast database class will create the interface implementation of DAO interface
    abstract fun currentWeatherDao(): CurrentWeatherDAO
    abstract fun futureWeatherForecastDAO(): FutureWeatherForecastDAO
}