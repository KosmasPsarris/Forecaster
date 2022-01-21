package com.myapps.forecaster.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myapps.forecaster.data.db.entity.FutureWeatherForecastEntry
import java.lang.reflect.Type


class ListTypeConverter {
    @TypeConverter
    fun stringToMeasurements(json: String?): List<FutureWeatherForecastEntry>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<FutureWeatherForecastEntry?>?>() {}.type
        return gson.fromJson<List<FutureWeatherForecastEntry>>(json, type)
    }

    @TypeConverter
    fun measurementsToString(list: List<FutureWeatherForecastEntry?>?): String? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<FutureWeatherForecastEntry?>?>() {}.type
        return gson.toJson(list, type)
    }
}