package com.myapps.forecaster.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.myapps.forecaster.data.DataStorePreferences

data class CurrentWeatherEntry(
    @SerializedName("temp_c")
    val tempC: Double,
    @SerializedName("temp_f")
    val tempF: Double,
    @SerializedName("is_day")
    val isDay: Int,
    @Embedded(prefix = "condition_")
    val condition: Condition,
    @SerializedName("wind_mph")
    val windMPH: Double,
    @SerializedName("wind_kph")
    val windKPH: Double,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("precip_mm")
    val precipMM: Double,
    @SerializedName("precip_in")
    val precipIN: Double,
    @SerializedName("feelslike_c")
    val feelslikeC: Double,
    @SerializedName("feelslike_f")
    val feelslikeF: Double,
    @SerializedName("vis_km")
    val visKM: Double,
    @SerializedName("vis_miles")
    val visMiles: Double
)















