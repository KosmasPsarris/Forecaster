package com.myapps.forecaster.data.network.response

import com.google.gson.annotations.SerializedName
import com.myapps.forecaster.data.db.entity.WeatherForecastEntry
import com.myapps.forecaster.data.db.entity.Location

data class WeatherForecastResponse (
    val location: Location,
    @SerializedName("forecast")
    val currentWeatherForecastEntry: WeatherForecastEntry
)