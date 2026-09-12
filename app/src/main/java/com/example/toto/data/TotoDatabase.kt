package com.example.toto.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.toto.data.dao.TotoSettingsDao
import com.example.toto.data.dao.TotoTripDao
import com.example.toto.data.model.TotoSettingsEntity
import com.example.toto.data.model.TotoTripEntity

@Database(
    entities = [
        TotoTripEntity::class,
        TotoSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TotoDatabase : RoomDatabase() {
    abstract fun tripDao(): TotoTripDao
    abstract fun settingsDao(): TotoSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: TotoDatabase? = null

        fun getDatabase(context: Context): TotoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TotoDatabase::class.java,
                    "toto_control_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
