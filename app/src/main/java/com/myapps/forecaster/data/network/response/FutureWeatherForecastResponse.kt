package com.myapps.forecaster.data.network.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapps.forecaster.data.db.entity.ForecastDaysContainer
import com.myapps.forecaster.data.db.entity.Location

const val FUTURE_WEATHER_ID = 0

@Entity(tableName = "future_weather")
data class FutureWeatherForecastResponse (

    @Embedded(prefix = "location_")
    val location: Location,

    @Embedded(prefix = "forecast_")
    val forecast: ForecastDaysContainer
){
    @PrimaryKey(autoGenerate = false) // No need to generate as the id is constant
    var id: Int = FUTURE_WEATHER_ID
}

