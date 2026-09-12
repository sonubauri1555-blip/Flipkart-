package com.example.toto.data

import com.example.toto.data.dao.TotoSettingsDao
import com.example.toto.data.dao.TotoTripDao
import com.example.toto.data.model.TotoSettingsEntity
import com.example.toto.data.model.TotoTripEntity
import kotlinx.coroutines.flow.Flow

class TotoRepository(
    private val tripDao: TotoTripDao,
    private val settingsDao: TotoSettingsDao
) {
    val allTrips: Flow<List<TotoTripEntity>> = tripDao.getAllTrips()
    val totalEarnings: Flow<Double?> = tripDao.getTotalEarnings()
    val totalTripsCount: Flow<Int> = tripDao.getTripsCount()
    val settings: Flow<TotoSettingsEntity?> = settingsDao.getSettings()

    suspend fun recordTrip(trip: TotoTripEntity): Long {
        return tripDao.insertTrip(trip)
    }

    suspend fun clearTrips() {
        tripDao.clearAllTrips()
    }

    suspend fun updateSettings(settings: TotoSettingsEntity) {
        settingsDao.saveSettings(settings)
    }
}
