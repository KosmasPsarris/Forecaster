package com.myapps.forecaster.util

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView

inline fun SearchView.onQueryTextSubmitted(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {

            clearFocus()
            listener(query.orEmpty())
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    })
}