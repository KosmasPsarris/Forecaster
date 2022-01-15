package com.myapps.forecaster.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.myapps.forecaster.data.DataStorePreferences
import com.myapps.forecaster.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    dataStorePreferences : DataStorePreferences
) : ViewModel() {

    val dataStorePreferencesFlow = dataStorePreferences.preferencesFlow

    // We prefer liveData for ViewModel - UI interactions
    val currentWeather = weatherRepository.getCurrentWeather().asLiveData()
}