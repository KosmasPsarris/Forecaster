package com.myapps.forecaster.data.network

import androidx.lifecycle.LiveData
import com.myapps.forecaster.data.network.response.CurrentWeatherResponse
import com.myapps.forecaster.data.network.response.WeatherForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        const val API_KEY = "70d13b47585045d2b0a183146212811" // Warning. Not a good practice to keep api key here
        const val BASE_URL = "https://api.weatherapi.com/v1/"
    }

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en"
    ): CurrentWeatherResponse

    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en",
        @Query("days") days: Int
    ): WeatherForecastResponse

}