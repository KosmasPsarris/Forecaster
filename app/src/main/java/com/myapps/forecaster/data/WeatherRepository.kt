package com.myapps.forecaster.data


import androidx.room.withTransaction
import com.myapps.forecaster.data.db.ForecastDatabase
import com.myapps.forecaster.data.network.WeatherApi
import com.myapps.forecaster.util.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val db: ForecastDatabase,
    private val dataStorePreferences : DataStorePreferences
) {
    private val currentWeatherDao = db.currentWeatherDao()

    fun getCurrentWeather() = networkBoundResource(
        query = {
            currentWeatherDao.getWeatherCurrent()
        },
        fetch = {
            delay(2000) // To see loading. DON'T INCLUDE IN REAL APPS

            // Change location and other details based on dataStorePreferences
            val location = dataStorePreferences.preferencesFlow.first().location
            val languageCode = "en"

            /*
            // Get new weather data
            val newCurrentWeatherApiData = api.getCurrentWeather(location = location, languageCode = languageCode)

            // Save new weather data
            dataStorePreferences.updateLocation(newCurrentWeatherApiData.location.name)

            newCurrentWeatherApiData // Pass it below to be saved into database

             */

            // Get new weather data and pass it below to be saved into database
            api.getCurrentWeather(location = location, languageCode = languageCode)
        },
        saveFetchResult = { currentWeatherResponse ->
            // withTransaction is used for multiple DAO operations
            db.withTransaction {
                currentWeatherDao.deleteCurrentWeather()
                currentWeatherDao.insertCurrentWeather(currentWeatherResponse)
            }
        }
    )

}