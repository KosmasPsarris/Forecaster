package com.myapps.forecaster.ui.weather.future.detail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.myapps.forecaster.data.DataStorePreferences
import com.myapps.forecaster.data.db.entity.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FutureDetailWeatherViewModel @Inject constructor(
    dataStorePreferences : DataStorePreferences,
    state: SavedStateHandle // Here data(to recreate UI, navigation arguments sent over) survives process death
) : ViewModel() {

    val dataStorePreferencesFlow = dataStorePreferences.preferencesFlow

    val day = state.get<Day>("day") // Name of argument sent in navigation
    val date = state.get<String>("date")

}