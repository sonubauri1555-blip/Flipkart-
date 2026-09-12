package com.example.toto.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toto_settings")
data class TotoSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val speedLimitKmH: Int = 26,
    val regenLevel: String = "MEDIUM",
    val driveProfile: String = "CITY",
    val batteryPackVolts: Int = 48,
    val batteryChemistry: String = "LIFEPO4_LITHIUM",
    val baseFareRs: Double = 10.0,
    val perKmFareRs: Double = 6.0,
    val languageCode: String = "bn" // Default Bengali as requested
)
