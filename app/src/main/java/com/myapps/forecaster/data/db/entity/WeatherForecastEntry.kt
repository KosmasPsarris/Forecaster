package com.myapps.forecaster.data.db.entity

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "current_weather")
data class WeatherForecastEntry(
    @SerializedName("temp_c")
    val tempC: Double
)