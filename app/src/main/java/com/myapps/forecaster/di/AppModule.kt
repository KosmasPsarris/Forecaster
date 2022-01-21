package com.myapps.forecaster.di

import android.app.Application
import androidx.room.Room
import com.myapps.forecaster.data.db.ForecastDatabase
import com.myapps.forecaster.data.network.WeatherApi
import com.myapps.forecaster.data.network.WeatherApi.Companion.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val httpClient = OkHttpClient.Builder()
        val okHttpClient = httpClient.addInterceptor { chain ->

            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .header("key", API_KEY) // Add api key to all api requests

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        return Retrofit.Builder()
                .baseUrl(WeatherApi.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    /*
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
         Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
*/

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : ForecastDatabase =
        Room.databaseBuilder(app, ForecastDatabase::class.java, "Forecast_database")
            .fallbackToDestructiveMigration()
            .build()

}