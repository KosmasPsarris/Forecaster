package com.myapps.forecaster.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.myapps.forecaster.data.DataStorePreferences
import com.myapps.forecaster.data.WeatherRepository
import com.myapps.forecaster.data.db.entity.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FutureListWeatherViewModel  @Inject constructor(
    weatherRepository: WeatherRepository,
    dataStorePreferences : DataStorePreferences
) : ViewModel() {

    private val daysEventChannel = Channel<DaysEvent>()
    val daysEvent = daysEventChannel.receiveAsFlow()

    val dataStorePreferencesFlow = dataStorePreferences.preferencesFlow

    val weatherForecast = weatherRepository.getFutureWeatherForecast().asLiveData()


    fun onDaySelected(day: Day, date: String) = viewModelScope.launch {
        daysEventChannel.send(DaysEvent.NavigateToForecastDayDetailsScreen(day, date))
    }

    sealed class DaysEvent {
        data class NavigateToForecastDayDetailsScreen(val day: Day, val date: String) : DaysEvent()
    }
}