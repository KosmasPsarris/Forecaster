package com.myapps.forecaster.util

// For compile time safety for exhaustive when statement
val <T> T.exhaustive: T
    get() = this