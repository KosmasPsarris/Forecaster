package com.myapps.forecaster.util

import kotlinx.coroutines.flow.*

// Set class as inline so we make these high order functions more efficient
inline fun <ResultType, RequestType> networkBoundResource(
    // Get data from database. Flow to keep getting updates from db
    crossinline query: () -> Flow<ResultType>,
    // Get new data from the rest API. Network requests are suspend functions, so is fetch
    crossinline fetch: suspend () -> RequestType,
    // Take data from rest API and save it to db
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    // If the cache data is outdated, get updated data
    crossinline shouldFetch: (ResultType) -> Boolean = { true  } // Update automatically
) = flow {

    // This block will be executed whenever we call networkBoundResource
    val data = query().first() // If we have a stream of data, we take a smaller list of it to test

    // Check if it is time to fetch new data
    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data)) // Updated data is loading, show progress bar

        // Try is for the case of no internet or error from the server
        try {
            saveFetchResult(fetch())
            // Save the result to the database
            // Success stops progress bar from loading and means that the
            // Updated was successful then updates UI
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            // Error means that the update was not successful and the
            // UI contains the old data
            query().map { Resource.Error(throwable, it) }
        }
    } else { // Not need to update
        query().map { Resource.Success(it) }
    }
    // Emit the whole stream of the mapped queries (any future data updates as long as
    // we don't cancel this block)
    emitAll(flow)
}