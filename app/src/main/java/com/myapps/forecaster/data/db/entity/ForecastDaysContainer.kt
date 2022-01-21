package com.myapps.forecaster.data.db.entity

import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.myapps.forecaster.util.ListTypeConverter

data class ForecastDaysContainer (

    @SerializedName("forecastday")
    @TypeConverters(ListTypeConverter::class)
    val forecastEntries: List<FutureWeatherForecastEntry>
)