package com.myapps.forecaster.ui.weather.future.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapps.forecaster.R
import com.myapps.forecaster.data.UnitSystem
import com.myapps.forecaster.data.db.entity.Day
import com.myapps.forecaster.databinding.FutureListWeatherFragmentBinding
import com.myapps.forecaster.ui.MainActivity
import com.myapps.forecaster.util.Resource
import com.myapps.forecaster.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FutureListWeatherFragment : Fragment(R.layout.future_list_weather_fragment), FutureListWeatherAdapter.OnItemClickListener  {

    private val viewModel by viewModels<FutureListWeatherViewModel>()

    private var _binding : FutureListWeatherFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FutureListWeatherFragmentBinding.bind(view)

        val forecastItemsAdapter = FutureListWeatherAdapter(this)

        binding.apply{
            recyclerView.apply{
                adapter = forecastItemsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            viewModel.weatherForecast.observe(this@FutureListWeatherFragment) { result ->

                viewLifecycleOwner.lifecycleScope.launch {
                    if (viewModel.dataStorePreferencesFlow.first().unitSystem == UnitSystem.METRIC)
                        forecastItemsAdapter.unitSystem = UnitSystem.METRIC
                    else
                        forecastItemsAdapter.unitSystem = UnitSystem.IMPERIAL
                }
                forecastItemsAdapter.submitList(result.data?.forecast?.forecastEntries)

                (requireActivity() as MainActivity).supportActionBar?.subtitle = result.data?.location?.name

                progressBarLoading.isVisible = result is Resource.Loading && listOf(result.data).isNullOrEmpty()
                textViewError.isVisible = result is Resource.Error && listOf(result.data).isNullOrEmpty()
                textViewError.text = result.error?.localizedMessage
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.daysEvent.collect { event ->
                when(event){
                    is FutureListWeatherViewModel.DaysEvent.NavigateToForecastDayDetailsScreen ->{
                        // Get day from adapter item onClick and with proper viewModel communication, navigate to details fragment
                        // With the correct day details
                        val action =
                            FutureListWeatherFragmentDirections.actionFutureListWeatherFragmentToFutureDetailWeatherFragment(
                                event.day, event.date)
                        findNavController().navigate(action)
                    }
                }.exhaustive

            } // End of collect
        } // End of launch

    }// End of onViewCreated

    override fun onItemClick(day: Day, date: String) {
        viewModel.onDaySelected(day, date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}