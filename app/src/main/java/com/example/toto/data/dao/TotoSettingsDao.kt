package com.example.toto.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.toto.data.model.TotoSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TotoSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: TotoSettingsEntity)

    @Query("SELECT * FROM toto_settings WHERE id = 1")
    fun getSettings(): Flow<TotoSettingsEntity?>
}
