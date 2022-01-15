package com.myapps.forecaster.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapps.forecaster.data.network.response.CurrentWeatherResponse


@Database(
    entities = [CurrentWeatherResponse::class],
    version = 5
)
abstract class ForecastDatabase : RoomDatabase(){
    // The forecast database class will create the interface implementation of DAO interface
    abstract fun currentWeatherDao(): CurrentWeatherDAO
}