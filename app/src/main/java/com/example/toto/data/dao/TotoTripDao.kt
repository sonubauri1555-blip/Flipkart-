package com.example.toto.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.toto.data.model.TotoTripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TotoTripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TotoTripEntity): Long

    @Query("SELECT * FROM toto_trips ORDER BY timestamp DESC LIMIT 50")
    fun getAllTrips(): Flow<List<TotoTripEntity>>

    @Query("SELECT SUM(fareRs) FROM toto_trips")
    fun getTotalEarnings(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM toto_trips")
    fun getTripsCount(): Flow<Int>

    @Query("DELETE FROM toto_trips")
    suspend fun clearAllTrips()
}
