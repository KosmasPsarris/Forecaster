# Forecaster
## Description:
An application where you can see the weather of a location.
The user can see the details of today's weather based on location chosen.
They can also see a 3 day forecast ( free api plan allows only 3 days and less ) of the chosen location and also click on a day 
of that forecast to see more details.
The user can also choose between the Metric and Imperial Unit Systems for the display of weather data, 
as well as type the preferred city location or use device location to obtain it's weather data.


<h3>Current Weather</h3>

![image](https://user-images.githubusercontent.com/34765932/149614449-d0bfe3b1-f94b-41f2-b980-c20fa69af9dd.png)

<h3>Weather Forecast</h3>

![image](https://user-images.githubusercontent.com/34765932/150500470-9eab013b-7411-418c-a440-ee1b0cc3be99.png)

<h3>Day Details</h3>

![image](https://user-images.githubusercontent.com/34765932/150500533-814866ad-728a-4b50-8113-d3be592ad727.png)

<h3>Settings</h3>

![image](https://user-images.githubusercontent.com/34765932/149614434-d6e4b55f-7c90-4820-a81e-c4dccc06d8ca.png)


The app follows a simple MVVM architecture with dependency injection (Dagger Hilt), single source of truth principle, and separation of concerns.
I am fetching data from a REST API (weatherapi) using Retrofit and caching this data for offline use in an SQLite database using the Room.
For this, i make use of a NetworkBoundResource implementation based on Kotlin Coroutines and Kotlin Flow.
Other libraries used are: Glide, Lifecycle - ViewModel & LiveData and DataStore.
