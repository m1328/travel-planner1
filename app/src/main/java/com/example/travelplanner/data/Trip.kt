package com.example.travelplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: String,
    val city: String,
    val country: String
)