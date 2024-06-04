package com.example.travelplanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trip::class], version = 2, exportSchema = false)
abstract class TripDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var INSTANCE: TripDatabase? = null

        fun getDatabase(context: Context): TripDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripDatabase::class.java,
                    "trip_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
