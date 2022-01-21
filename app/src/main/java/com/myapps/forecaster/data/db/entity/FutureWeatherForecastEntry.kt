package com.myapps.forecaster.data.db.entity

import androidx.room.Embedded
import java.text.SimpleDateFormat
import java.util.*

data class FutureWeatherForecastEntry(

    val date: String,
    @Embedded(prefix = "day_")
    val day: Day
) {
    fun getFormattedDate(): String {

        // Format our date how we want
        val outputFormat = SimpleDateFormat("dd/MMM", Locale.getDefault()) // Date format we want to display
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Date format we get from the api request field

        val dateFormatted = inputFormat.parse(date)

        return outputFormat.format(dateFormatted!!)
    }
}