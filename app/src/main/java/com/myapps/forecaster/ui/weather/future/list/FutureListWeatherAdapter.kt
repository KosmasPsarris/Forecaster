package com.myapps.forecaster.ui.weather.future.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapps.forecaster.data.db.entity.WeatherForecastEntry
import com.myapps.forecaster.databinding.ItemFutureWeatherBinding

class FutureListWeatherAdapter : ListAdapter<WeatherForecastEntry, FutureListWeatherAdapter.FutureListWeatherViewHolder>(FutureListWeatherComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureListWeatherViewHolder {
        val binding =
            ItemFutureWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FutureListWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FutureListWeatherViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class FutureListWeatherViewHolder(private val binding: ItemFutureWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecastEntities: WeatherForecastEntry) {
            /*binding.apply {
                Glide.with(itemView)
                    .load(restaurant.logo)
                    .into(imageViewLogo)

                textViewName.text = restaurant.name
                textViewType.text = restaurant.type
                textViewAddress.text = restaurant.address
            }
             */
        }
    }

    class FutureListWeatherComparator : DiffUtil.ItemCallback<WeatherForecastEntry>() {
        override fun areItemsTheSame(oldItem: WeatherForecastEntry, newItem: WeatherForecastEntry) =
            oldItem.tempC == newItem.tempC // was name

        override fun areContentsTheSame(oldItem: WeatherForecastEntry, newItem: WeatherForecastEntry) =
            oldItem == newItem
    }
}