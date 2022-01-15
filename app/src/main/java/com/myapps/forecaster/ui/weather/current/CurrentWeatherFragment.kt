package com.myapps.forecaster.ui.weather.current

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.myapps.forecaster.R
import com.myapps.forecaster.data.UnitSystem
import com.myapps.forecaster.databinding.CurrentWeatherFragmentBinding
import com.myapps.forecaster.ui.MainActivity
import com.myapps.forecaster.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment(R.layout.current_weather_fragment) {

    private val viewModel by viewModels<CurrentWeatherViewModel>()

    private var _binding : CurrentWeatherFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = CurrentWeatherFragmentBinding.bind(view)


        binding.apply {

            // This gets executed whenever currentWeather changes
            viewModel.currentWeather.observe(viewLifecycleOwner) { result ->

                // Show details from API request of current weather to the fragment
                // (Bases on unit system chosen)
                // If we had more entries (recyclerView) we would use an adapter

                // We launch a coroutine so we display the right unit data
                viewLifecycleOwner.lifecycleScope.launch {
                    if (viewModel.dataStorePreferencesFlow.first().unitSystem == UnitSystem.METRIC) {

                        textViewTemperature.text = getString(R.string.current_metric_temperature, result.data?.currentWeatherEntry?.tempC.toString())
                        textViewWind.text = getString(R.string.current_metric_wind,
                            result.data?.currentWeatherEntry?.windDir, result.data?.currentWeatherEntry?.windKPH.toString())
                        textViewPrecipitation.text = getString(R.string.current_metric_precipitation, result.data?.currentWeatherEntry?.precipMM.toString())
                        textViewFeelsLikeTemperature.text = getString(R.string.current_metric_feelsLikeTemperature, result.data?.currentWeatherEntry?.feelslikeC.toString())
                        textViewVisibility.text = getString(R.string.current_metric_visibility, result.data?.currentWeatherEntry?.visKM.toString())
                    }
                    else{

                        textViewTemperature.text = getString(R.string.current_imperial_temperature, result.data?.currentWeatherEntry?.tempF.toString())
                        textViewWind.text = getString(R.string.current_imperial_wind,
                            result.data?.currentWeatherEntry?.windDir, result.data?.currentWeatherEntry?.windMPH.toString())
                        textViewPrecipitation.text = getString(R.string.current_imperial_precipitation, result.data?.currentWeatherEntry?.precipIN.toString())
                        textViewFeelsLikeTemperature.text = getString(R.string.current_imperial_feelsLikeTemperature, result.data?.currentWeatherEntry?.feelslikeF.toString())
                        textViewVisibility.text = getString(R.string.current_imperial_visibility, result.data?.currentWeatherEntry?.visMiles.toString())
                    }
                }


                textViewCondition.text = result.data?.currentWeatherEntry?.condition?.text

                // Load weather image
                Glide.with(this@CurrentWeatherFragment)
                    .load("https:"+result.data?.currentWeatherEntry?.condition?.icon)
                    .into(imageViewConditionIcon)

                // Change location displayed if api request was successful
                (requireActivity() as MainActivity).supportActionBar?.subtitle = result.data?.location?.name


                // Show progress bar if resource is loading and we don't have cache data
                progressBarLoading.isVisible = result is Resource.Loading && arrayOf(result.data).isNullOrEmpty()
                textViewError.isVisible = result is Resource.Error && arrayOf(result.data).isNullOrEmpty()
                textViewError.text = result.error?.localizedMessage
            }
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
















