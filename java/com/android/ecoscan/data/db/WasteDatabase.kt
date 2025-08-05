package com.android.ecoscan.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Waste::class], version = 1, exportSchema = false)
abstract class WasteDatabase : RoomDatabase() {

    abstract fun wasteDao(): WasteDao

    companion object {
        @Volatile
        private var INSTANCE: WasteDatabase? = null

        fun getDatabase(context: Context): WasteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WasteDatabase::class.java,
                    "waste_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
