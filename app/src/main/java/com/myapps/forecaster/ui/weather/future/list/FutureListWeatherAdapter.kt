package com.myapps.forecaster.ui.weather.future.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapps.forecaster.R
import com.myapps.forecaster.data.UnitSystem
import com.myapps.forecaster.data.db.entity.Day
import com.myapps.forecaster.data.db.entity.FutureWeatherForecastEntry
import com.myapps.forecaster.databinding.ItemFutureWeatherBinding

class FutureListWeatherAdapter(private val listener: OnItemClickListener)
    : ListAdapter<FutureWeatherForecastEntry, FutureListWeatherAdapter.FutureListWeatherViewHolder>(FutureListWeatherComparator()) {

    var unitSystem : UnitSystem = UnitSystem.METRIC

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

    inner class FutureListWeatherViewHolder(private val binding: ItemFutureWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: FutureWeatherForecastEntry) {
            binding.apply {
                Glide.with(itemView)
                    .load("https:"+entry.day.condition.icon)
                    .into(imageViewConditionIcon)


                textViewDate.text = entry.getFormattedDate()

                if(unitSystem == UnitSystem.METRIC) // viewHolder -> itemView -> View -> context
                    textViewTemperature.text =  itemView.context.getString(R.string.future_weather_list_adapter_metric_temperature, entry.day.avgtempC.toString())
                else
                    textViewTemperature.text = itemView.context.getString(R.string.future_weather_list_adapter_imperial_temperature, entry.day.avgtempF.toString())

                textViewCondition.text = entry.day.condition.text


                // Set on click listeners for each item
                root.setOnClickListener {

                    // Get item clicked by position save it's day details so we can pass it through to details fragment
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val day = getItem(position).day
                        val date = getItem(position).getFormattedDate()
                        listener.onItemClick(day,date)
                    }
                }
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(day: Day, date: String) // So we sent data when we click a forecast weather day
    }

    class FutureListWeatherComparator : DiffUtil.ItemCallback<FutureWeatherForecastEntry>() {
        override fun areItemsTheSame(oldItem: FutureWeatherForecastEntry, newItem: FutureWeatherForecastEntry) =
            oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: FutureWeatherForecastEntry, newItem: FutureWeatherForecastEntry) =
            oldItem == newItem
    }
}