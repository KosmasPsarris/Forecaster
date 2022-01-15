# Forecaster
## Description:
An application where you can see the weather of a location.
Currently in this version, the user can see the details of the weather chosen of today.
In the future a forecast of 7 days will be implemented.
The user can also choose between the Metric and Imperial Unit Systems for the display of weather data, 
as well as type the preferred city location or use device location to obtain it's weather data.



![image](https://user-images.githubusercontent.com/34765932/149614449-d0bfe3b1-f94b-41f2-b980-c20fa69af9dd.png)




![image](https://user-images.githubusercontent.com/34765932/149614434-d6e4b55f-7c90-4820-a81e-c4dccc06d8ca.png)


The app follows a simple MVVM architecture with dependency injection (Dagger Hilt), single source of truth principle, and separation of concerns.
I am fetching data from a REST API (weatherapi) using Retrofit and caching this data for offline use in an SQLite database using the Room.
For this, i make use of a NetworkBoundResource implementation based on Kotlin Coroutines and Kotlin Flow.
Other libraries used are: Glide, Lifecycle - ViewModel & LiveData and DataStore.
