package com.myapps.forecaster.ui.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.myapps.forecaster.R
import com.myapps.forecaster.data.UnitSystem
import com.myapps.forecaster.databinding.SettingsFragmentBinding
import com.myapps.forecaster.util.exhaustive
import com.myapps.forecaster.util.onQueryTextSubmitted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.settings_fragment){

    private val viewModel by viewModels<SettingsViewModel>()

    private var _binding : SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private var locationManager : LocationManager? = null
    private var coordinates : String? = null
    companion object{ const val  LOCATION_REQUEST_CODE = 101 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = SettingsFragmentBinding.bind(view)

        // Create persistent LocationManager reference
        locationManager =
            requireActivity().applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        (activity as AppCompatActivity?)!!.supportActionBar!!.subtitle = null // Remove location subtitle

        binding.apply {

            switchDeviceLocation.setOnCheckedChangeListener { _, isChecked ->
                // When switch button gets checked use device location for api request and hide other option
                if (isChecked) {
                    textviewLocation.visibility = View.INVISIBLE
                    searchViewLocation.visibility = View.INVISIBLE

                    getLocation() // Start device location search process

                    viewModel.onDeviceSwitchChanged(true)
                }
                // When switch button gets unchecked use previously selected location and show other option
                else {
                    textviewLocation.visibility = View.VISIBLE
                    searchViewLocation.visibility = View.VISIBLE

                    viewModel.onDeviceSwitchChanged(false)
                }
            }


            radioGroupUnitSystem.setOnCheckedChangeListener { _, checkedId ->
                // Save unit system selected and change UI details of weather
                when (checkedId) {
                    R.id.radio_button_metric -> {

                        viewModel.onUnitSystemChanged(UnitSystem.METRIC)
                    }
                    R.id.radio_button_imperial -> {

                        viewModel.onUnitSystemChanged(UnitSystem.IMPERIAL)
                    }
                }
            }

            // When user inputs a location for a new weather location, find weather forecast through api request
            // If successful it becomes the new selected location (closest to what the user typed, autocorrection is done in api)
            // And UI weather details change, else notify user
            searchViewLocation.onQueryTextSubmitted {

                // Sends what was typed in search for location. ViewModel will do the validation
                viewModel.onLocationSelectChanged(searchViewLocation.query.toString())
            }

            // Get the search close button image view
            val closeButton: ImageView =
                searchViewLocation.findViewById(R.id.search_close_btn) as ImageView

            // Set on click listener
            closeButton.setOnClickListener {
                // Clear query
                searchViewLocation.setQuery("", false)
                // Clear focus
                searchViewLocation.clearFocus()
                //Collapse the action view
                searchViewLocation.onActionViewCollapsed()
            }


            // Prepare UI - user preferences based on user saved dataStore preferences
            viewLifecycleOwner.lifecycleScope.launch {

                // Check the preferred radio button
                if (viewModel.dataStorePreferencesFlow.first().unitSystem == UnitSystem.IMPERIAL && !radioButtonImperial.isChecked) {
                    radioButtonImperial.isChecked = true
                } else if (viewModel.dataStorePreferencesFlow.first().unitSystem == UnitSystem.METRIC && !radioButtonMetric.isChecked)
                    radioButtonMetric.isChecked = true


                // Set if weather location is based on device location
                // If preference is checked (device location) and switch is not checked, then check it
                // If preference is not checked and switch is checked, then uncheck it
                // We do these checks so we won't run the location search process everytime this fragment is created
                if (viewModel.dataStorePreferencesFlow.first().deviceSwitch && !switchDeviceLocation.isChecked)
                    switchDeviceLocation.isChecked = true
                else if (!viewModel.dataStorePreferencesFlow.first().deviceSwitch && switchDeviceLocation.isChecked)
                    switchDeviceLocation.isChecked = false

            }

        } // End of binding apply


        // Use WhenStart cause we don't care for events while we don't see the fragment
        // Here we control this fragment's events
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.settingsEvent.collect { event ->
                when (event) {
                    is SettingsViewModel.SettingsEvent.ShowRetryLocationSearchMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG)
                            .setAction("RETRY") {
                                getLocation() // Retrying means starting location search again
                                            // It is generally a better practice to pass the action to ViewModel when possible
                                            // And not use an action from the Fragment
                            }.show()
                    }
                    is SettingsViewModel.SettingsEvent.ShowSuccessLocationSearchMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is SettingsViewModel.SettingsEvent.ShowLocationSearchSuccessMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is SettingsViewModel.SettingsEvent.ShowLocationSelectErrorMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive // End of when
            } // End of collect
        } // End of launchWhenStarted

    } // End of onViewCreated

    private fun getLocation() {

        coordinates = null // Reset

        // In order to collect location data we need permission of user so we can access their device location
        if (ActivityCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                { // Permission is not granted
            // Ask permission from user
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
        else { // Permission already granted
            getCoordinates()
        }

    }

    // Check permission results
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       if (requestCode == LOCATION_REQUEST_CODE && grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           // Permission granted
           getCoordinates()
       } else {
           // Permission denied
           // Switch the switch view back to unchecked
           binding.switchDeviceLocation.isChecked = false
       }
    }

    private fun getCoordinates(){

        try {
            // Request location updates
            // It is more user friendly to allow the user to choose which PROVIDER they want
            // However for efficiency i use GPS
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )

        } catch (ex: SecurityException) {
            // Show snackBar that notifies the user and ability to retry
            viewModel.onLocationSearchFailed("Security Exception, no location available.")
            Log.e("Location", "Security Exception, no location available")
        }
    }

    // Define the listener and override functions
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            coordinates = "${location.latitude},${location.longitude}"
            Log.e("Location", coordinates.toString())

            if(coordinates != null){

                viewModel.onLocationCoordinatesChanged(coordinates!!)
                viewModel.onLocationSearchSucceeded("Device location found!")
            }
            else{
                // Show snackBar that notifies the user and ability to retry
                viewModel.onLocationSearchFailed("No location found.")
                Log.e("Location", "No location found")
            }

            locationManager?.removeUpdates(this) // Stop location updates
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {return}
        override fun onProviderEnabled(provider: String) {return}
        override fun onProviderDisabled(provider: String) {return}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}