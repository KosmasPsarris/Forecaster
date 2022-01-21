package com.myapps.forecaster.data.db.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize // Day contains Condition so in order to parcelize it we need to make Condition Parcelable as well
data class Condition (
    val text: String,
    val icon: String,
    val code: Int
        ) : Parcelable
