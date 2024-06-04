package com.example.travelplanner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.travelplanner.data.Trip
import com.example.travelplanner.data.TripDatabase
import com.example.travelplanner.data.TripRepository
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TripRepository
    val trips: LiveData<List<Trip>>

    init {
        val tripDao = TripDatabase.getDatabase(application).tripDao()
        repository = TripRepository(tripDao)
        trips = repository.allTrips
    }

    fun addTrip(name: String, date: String, city: String, country: String) {
        viewModelScope.launch {
            repository.insert(Trip(name = name, date = date, city = city, country = country))
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            repository.delete(trip)
        }
    }
    fun updateTrip(trip: Trip) {
        viewModelScope.launch {
            repository.updateTrip(trip)
        }
    }
}
