package com.example.toto.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toto_trips")
data class TotoTripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val distanceKm: Float,
    val durationSeconds: Long,
    val passengers: Int,
    val fareRs: Double,
    val startBatteryPercent: Int,
    val endBatteryPercent: Int
)
