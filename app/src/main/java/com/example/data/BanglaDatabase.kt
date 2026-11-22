package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BanglaNote::class], version = 1, exportSchema = false)
abstract class BanglaDatabase : RoomDatabase() {
    abstract fun banglaNoteDao(): BanglaNoteDao

    companion object {
        @Volatile
        private var INSTANCE: BanglaDatabase? = null

        fun getDatabase(context: Context): BanglaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BanglaDatabase::class.java,
                    "bangla_calendar.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
