# Forecaster
## Description:
An application where you can see the weather of a location.
Currently in this version, the user can see the details of the weather chosen of today.
In the future a forecast of 7 days will be implemented.
The user can also choose between the Metric and Imperial Unit Systems for the display of weather data, 
as well as type the preferred city location or use device location to obtain it's weather data.

The app follows a simple MVVM architecture with dependency injection (Dagger Hilt), single source of truth principle, and separation of concerns.
I am fetching data from a REST API (weatherapi) using Retrofit and caching this data for offline use in an SQLite database using the Room.
For this, i make use of a NetworkBoundResource implementation based on Kotlin Coroutines and Kotlin Flow.
Other libraries used are: Glide, Lifecycle - ViewModel & LiveData and DataStore.
