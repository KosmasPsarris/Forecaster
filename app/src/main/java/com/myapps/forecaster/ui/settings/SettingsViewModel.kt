package com.myapps.forecaster.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapps.forecaster.data.DataStorePreferences
import com.myapps.forecaster.data.UnitSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStorePreferences : DataStorePreferences
) : ViewModel() {

    // We use channels for snackBar events for user friendliness (such as don't show snackBar again after screen rotation)
    private val settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvent = settingsEventChannel.receiveAsFlow()

    companion object{ const val LOCATION_REGEX : String = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*\$" }


    val dataStorePreferencesFlow = dataStorePreferences.preferencesFlow

    fun onUnitSystemChanged(unitSystem: UnitSystem) = viewModelScope.launch {
        dataStorePreferences.updateUnitSystem(unitSystem)
    }

    fun onLocationSelectChanged(location: String) = viewModelScope.launch {

        val regex = LOCATION_REGEX.toRegex()
        if(regex.containsMatchIn(location)){// If it is valid so positive message and update weather data

            onLocationSelectionSucceeded("Selected location found!")
            dataStorePreferences.updateLocation(location)
        }
        else{// Else notify user of wrong input
            onLocationSelectionFailed("Wrong city name, try again.")
        }
    }

    fun onLocationCoordinatesChanged(location: String) = viewModelScope.launch {
        dataStorePreferences.updateLocation(location)
    }

    fun onDeviceSwitchChanged(deviceSwitch: Boolean) = viewModelScope.launch {
        dataStorePreferences.updateDeviceSwitch(deviceSwitch)
    }

    // For snackBar to display what went wrong and option for retry regarding the location search process
    fun onLocationSearchFailed(message : String){
        viewModelScope.launch {
            settingsEventChannel.send(SettingsEvent.ShowRetryLocationSearchMessage(message))
        }
    }

    // Notify the user that that location was found successfully
    fun onLocationSearchSucceeded(message : String){
        viewModelScope.launch {
            settingsEventChannel.send(SettingsEvent.ShowSuccessLocationSearchMessage(message))
        }
    }

    // Notify user of successful location selection
    private fun onLocationSelectionSucceeded(message : String){
        viewModelScope.launch {
            settingsEventChannel.send(SettingsEvent.ShowLocationSearchSuccessMessage(message))
        }
    }

    // Notify user of failed location selection
    private fun onLocationSelectionFailed(message : String){
        viewModelScope.launch {
            settingsEventChannel.send(SettingsEvent.ShowLocationSelectErrorMessage(message))
        }
    }

    // Kinds of events (good practice : one channel and multiple events (that can be distinguished)
    // We use sealed class so when we use "when branching" our options are exhaustive
    sealed class SettingsEvent {
        data class ShowRetryLocationSearchMessage(val message: String) : SettingsEvent()
        data class ShowSuccessLocationSearchMessage(val message: String) : SettingsEvent()
        data class ShowLocationSelectErrorMessage(val message: String) : SettingsEvent()
        data class ShowLocationSearchSuccessMessage(val message: String) : SettingsEvent()
    }
}