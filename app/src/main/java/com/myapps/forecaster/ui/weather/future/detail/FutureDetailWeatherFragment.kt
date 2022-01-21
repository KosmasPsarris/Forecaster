package com.myapps.forecaster.ui.weather.future.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.myapps.forecaster.R
import com.myapps.forecaster.data.UnitSystem
import com.myapps.forecaster.databinding.FutureDetailWeatherFragmentBinding
import com.myapps.forecaster.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FutureDetailWeatherFragment : Fragment(R.layout.future_detail_weather_fragment) {

    private val viewModel by viewModels<FutureDetailWeatherViewModel>()

    private var _binding : FutureDetailWeatherFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FutureDetailWeatherFragmentBinding.bind(view)


        binding.apply {

            viewLifecycleOwner.lifecycleScope.launch {

                if (viewModel.dataStorePreferencesFlow.first().unitSystem == UnitSystem.METRIC) {

                    textViewTemperature.text = getString(R.string.future_details_metric_temperature, viewModel.day?.avgtempC.toString())
                    textViewMinMaxTemperature.text = getString(R.string.future_details_metric_min_max_temperature,
                        viewModel.day?.mintempC.toString(), viewModel.day?.maxtempC.toString())
                    textViewWind.text = getString(R.string.future_details_metric_wind, viewModel.day?.maxwindKph.toString())
                    textViewPrecipitation.text = getString(R.string.future_details_metric_precipitation, viewModel.day?.totalprecipMm.toString())
                    textViewVisibility.text = getString(R.string.future_details_metric_visibility, viewModel.day?.avgvisKm.toString())
                }
                else{

                    textViewTemperature.text = getString(R.string.future_details_imperial_temperature, viewModel.day?.avgtempF.toString())
                    textViewMinMaxTemperature.text = getString(R.string.future_details_imperial_min_max_temperature,
                        viewModel.day?.mintempF.toString(), viewModel.day?.maxtempF.toString())
                    textViewWind.text = getString(R.string.future_details_imperial_wind, viewModel.day?.maxwindMph.toString())
                    textViewPrecipitation.text = getString(R.string.future_details_imperial_precipitation, viewModel.day?.totalprecipIn.toString())
                    textViewVisibility.text = getString(R.string.future_details_imperial_visibility, viewModel.day?.avgvisMiles.toString())
                }

                textViewUv.text = getString(R.string.future_details_uv, viewModel.day?.uv.toString())
                textViewHumidity.text = getString(R.string.future_details_humidity, viewModel.day?.avghumidity.toString())

                textViewCondition.text = viewModel.day?.condition?.text

                // Change location displayed if api request was successful
                (requireActivity() as MainActivity).supportActionBar?.subtitle =
                    "${(requireActivity() as MainActivity).supportActionBar?.subtitle} ${viewModel.date}"

                // Load weather image
                Glide.with(this@FutureDetailWeatherFragment)
                    .load("https:"+viewModel.day?.condition?.icon)
                    .into(imageViewConditionIcon)

            }

        } // End of binding.apply

    }// End of onViewCreated


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}