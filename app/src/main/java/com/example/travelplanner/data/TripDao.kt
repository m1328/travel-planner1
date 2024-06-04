package com.example.travelplanner.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getAllTrips(): LiveData<List<Trip>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Update
    suspend fun update(trip: Trip)
}
