package com.myapps.forecaster.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "DataStorePreferences"

enum class UnitSystem { METRIC, IMPERIAL }

data class FilterPreferences(val unitSystem: UnitSystem, val location: String, val deviceSwitch: Boolean)

@Singleton
class DataStorePreferences @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_preferences") // Name of our dataStore

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val unitSystem = UnitSystem.valueOf(
                preferences[PreferencesKeys.UNIT_SYSTEM] ?: UnitSystem.METRIC.name // Default unit system is metric
            )
            val location = preferences[PreferencesKeys.LOCATION] ?: "Athens" // Default weather location is Athens

            val deviceSwitch = preferences[PreferencesKeys.DEVICE_SWITCH] ?: false

            FilterPreferences(unitSystem, location, deviceSwitch)
        }

    suspend fun updateUnitSystem(unitSystem: UnitSystem) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.UNIT_SYSTEM] = unitSystem.name
        }
    }

    suspend fun updateLocation(location: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOCATION] = location
        }
    }

    suspend fun updateDeviceSwitch(deviceSwitch: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEVICE_SWITCH] = deviceSwitch
        }
    }

    private object PreferencesKeys {
        val UNIT_SYSTEM = preferencesKey<String>("unit_system")
        val LOCATION = preferencesKey<String>("location")
        val DEVICE_SWITCH = preferencesKey<Boolean>("device_switch")
    }
}