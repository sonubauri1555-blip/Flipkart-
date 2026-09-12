package com.example.toonstudio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProjectEntity::class, SceneEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToonStudioDatabase : RoomDatabase() {
    abstract fun toonStudioDao(): ToonStudioDao

    companion object {
        @Volatile
        private var INSTANCE: ToonStudioDatabase? = null

        fun getDatabase(context: Context): ToonStudioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToonStudioDatabase::class.java,
                    "toon_studio_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
