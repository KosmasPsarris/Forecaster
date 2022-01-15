package com.myapps.forecaster.data.network.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.myapps.forecaster.data.db.entity.CurrentWeatherEntry
import com.myapps.forecaster.data.db.entity.Location

// Because we have only 1 current weather we use a known id to get it
const val CURRENT_WEATHER_ID = 0

// Entities are tables in SQL lite database. (something we store non localized)
// Room works for primitive types only so
// For custom classes such as "Condition" we use Embedded

@Entity(tableName = "current_weather")
data class CurrentWeatherResponse (

    @Embedded(prefix = "location_")
    val location: Location,

    @SerializedName("current")
    @Embedded(prefix = "currentWeatherEntry_")
    val currentWeatherEntry: CurrentWeatherEntry
    ){

    @PrimaryKey(autoGenerate = false) // No need to generate as the id is constant
    var id: Int = CURRENT_WEATHER_ID
}
