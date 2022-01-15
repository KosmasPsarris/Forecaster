package com.myapps.forecaster.util

// We use T so the data input is flexible
sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    // Different cases of the sealed class
    class Success<T>(data: T) : Resource<T>(data) // Hide progress bar
    class Loading<T>(data: T? = null) : Resource<T>(data) // Display progress bar (cache data can be displayed the same time as the progress bar)
                                                        // We can either display empty data or cache data while it is loading
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable) // Get the whole error message (we again can display cache data)
}