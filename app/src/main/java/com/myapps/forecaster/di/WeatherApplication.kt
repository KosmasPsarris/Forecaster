package com.myapps.forecaster.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Need this to start DI (dagger hilt) process
@HiltAndroidApp
class WeatherApplication : Application()