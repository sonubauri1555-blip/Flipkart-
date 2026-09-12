package com.example.toto.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.localization.AppLanguage
import com.example.toto.audio.TotoSoundEngine
import com.example.toto.data.TotoDatabase
import com.example.toto.data.TotoRepository
import com.example.toto.data.model.TotoSettingsEntity
import com.example.toto.data.model.TotoTripEntity
import com.example.toto.model.BatteryPackVoltage
import com.example.toto.model.BatteryType
import com.example.toto.model.ChargeStatus
import com.example.toto.model.ConnectionStatus
import com.example.toto.model.DriveProfile
import com.example.toto.model.GearMode
import com.example.toto.model.HeadlightMode
import com.example.toto.model.RegenLevel
import com.example.toto.model.TotoTab
import com.example.toto.model.TotoVehicleState
import com.example.toto.model.WiperMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class TotoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TotoRepository

    private val _vehicleState = MutableStateFlow(TotoVehicleState())
    val vehicleState: StateFlow<TotoVehicleState> = _vehicleState.asStateFlow()

    private val _currentTab = MutableStateFlow(TotoTab.COCKPIT)
    val currentTab: StateFlow<TotoTab> = _currentTab.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(AppLanguage.BENGALI)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    // Database Flows
    val pastTrips: StateFlow<List<TotoTripEntity>>
    val dbTotalEarnings: StateFlow<Double?>
    val dbTripsCount: StateFlow<Int>

    // Loop jobs
    private var telemetryLoopJob: Job? = null
    private var flasherBlinkJob: Job? = null
    private var fareMeterJob: Job? = null

    init {
        val db = TotoDatabase.getDatabase(application)
        repository = TotoRepository(db.tripDao(), db.settingsDao())

        pastTrips = repository.allTrips.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        dbTotalEarnings = repository.totalEarnings.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )
        dbTripsCount = repository.totalTripsCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        // Load saved settings if any
        viewModelScope.launch {
            repository.settings.collect { saved ->
                saved?.let { s ->
                    val lang = AppLanguage.values().find { it.code == s.languageCode } ?: AppLanguage.BENGALI
                    _selectedLanguage.value = lang
                    _vehicleState.update { current ->
                        current.copy(
                            speedGovernorLimitKmH = s.speedLimitKmH,
                            regenBraking = RegenLevel.values().find { it.name == s.regenLevel } ?: RegenLevel.MEDIUM,
                            driveProfile = DriveProfile.values().find { it.name == s.driveProfile } ?: DriveProfile.CITY,
                            baseFareRs = s.baseFareRs,
                            ratePerKmRs = s.perKmFareRs,
                            batteryPackVoltage = if (s.batteryPackVolts == 60) BatteryPackVoltage.VOLT_60 else BatteryPackVoltage.VOLT_48,
                            batteryType = if (s.batteryChemistry.contains("LEAD")) BatteryType.LEAD_ACID_TUBULAR else BatteryType.LIFEPO4_LITHIUM
                        )
                    }
                }
            }
        }

        startTelemetryLoop()
        startFlasherBlinkerLoop()
    }

    fun setTab(tab: TotoTab) {
        _currentTab.value = tab
    }

    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
        persistCurrentSettings()
    }

    // ==========================================
    // Master Ignition & Security Controls
    // ==========================================

    fun toggleMasterIgnition() {
        val newState = !_vehicleState.value.isMasterIgnitionOn
        if (newState) {
            // Cannot start if motor is anti-theft locked
            if (_vehicleState.value.isMotorLocked) {
                return
            }
            TotoSoundEngine.playPowerStartupChime()
            _vehicleState.update { it.copy(isMasterIgnitionOn = true, gearMode = GearMode.PARK) }
        } else {
            TotoSoundEngine.playPowerOffChime()
            TotoSoundEngine.stopReverseBuzzer()
            _vehicleState.update {
                it.copy(
                    isMasterIgnitionOn = false,
                    gearMode = GearMode.PARK,
                    currentSpeedKmH = 0f,
                    motorRpm = 0,
                    motorCurrentAmps = 0f,
                    remoteCrawlerThrottlePercent = 0f,
                    isRemoteParkingInching = false
                )
            }
        }
    }

    fun toggleMotorAntiTheftLock() {
        val currentlyLocked = _vehicleState.value.isMotorLocked
        val newLock = !currentlyLocked
        _vehicleState.update {
            it.copy(
                isMotorLocked = newLock,
                isMasterIgnitionOn = if (newLock) false else it.isMasterIgnitionOn,
                gearMode = if (newLock) GearMode.PARK else it.gearMode,
                currentSpeedKmH = if (newLock) 0f else it.currentSpeedKmH
            )
        }
        if (newLock) {
            TotoSoundEngine.playHorn(durationMs = 120)
        } else {
            TotoSoundEngine.playHorn(durationMs = 60)
        }
    }

    fun toggleAlarmSiren() {
        val isRinging = _vehicleState.value.isSirenRinging
        if (isRinging) {
            TotoSoundEngine.stopAlarmSiren()
            _vehicleState.update { it.copy(isSirenRinging = false, isAlarmArmed = false) }
        } else {
            TotoSoundEngine.startAlarmSiren()
            _vehicleState.update { it.copy(isSirenRinging = true, isAlarmArmed = true, isHazardFlashing = true) }
        }
    }

    fun toggleEmergencySos() {
        val active = !_vehicleState.value.isEmergencySos
        _vehicleState.update {
            it.copy(
                isEmergencySos = active,
                isHazardFlashing = active || it.isHazardFlashing
            )
        }
        if (active) {
            TotoSoundEngine.startAlarmSiren()
        } else {
            TotoSoundEngine.stopAlarmSiren()
        }
    }

    fun triggerLocateToto() {
        viewModelScope.launch {
            repeat(3) {
                TotoSoundEngine.playHorn(durationMs = 150)
                _vehicleState.update { it.copy(headlightMode = HeadlightMode.HIGH_BEAM, isHazardFlashing = true) }
                delay(300)
                _vehicleState.update { it.copy(headlightMode = HeadlightMode.OFF, isHazardFlashing = false) }
                delay(200)
            }
        }
    }

    // ==========================================
    // Transmission & Motion Controls
    // ==========================================

    fun selectGear(gear: GearMode) {
        if (!_vehicleState.value.isMasterIgnitionOn) return
        if (_vehicleState.value.isMotorLocked) return

        _vehicleState.update { it.copy(gearMode = gear) }
        if (gear == GearMode.REVERSE) {
            if (_vehicleState.value.isReverseBuzzerEnabled) {
                TotoSoundEngine.startReverseBuzzer()
            }
        } else {
            TotoSoundEngine.stopReverseBuzzer()
        }
    }

    fun selectDriveProfile(profile: DriveProfile) {
        _vehicleState.update { it.copy(driveProfile = profile) }
        persistCurrentSettings()
    }

    fun setRegenBraking(regen: RegenLevel) {
        _vehicleState.update { it.copy(regenBraking = regen) }
        persistCurrentSettings()
    }

    fun setSpeedGovernorLimit(limit: Int) {
        _vehicleState.update { it.copy(speedGovernorLimitKmH = limit) }
        persistCurrentSettings()
    }

    fun setRemoteInching(throttlePercent: Float, isForward: Boolean) {
        if (!_vehicleState.value.isMasterIgnitionOn || _vehicleState.value.isMotorLocked) return
        val targetGear = if (isForward) GearMode.DRIVE else GearMode.REVERSE
        selectGear(targetGear)
        _vehicleState.update {
            it.copy(
                remoteCrawlerThrottlePercent = throttlePercent,
                isRemoteParkingInching = throttlePercent > 0f
            )
        }
    }

    fun stopRemoteInching() {
        _vehicleState.update {
            it.copy(
                remoteCrawlerThrottlePercent = 0f,
                isRemoteParkingInching = false
            )
        }
    }

    // ==========================================
    // Lighting & Aux Switches
    // ==========================================

    fun cycleHeadlight() {
        val nextMode = when (_vehicleState.value.headlightMode) {
            HeadlightMode.OFF -> HeadlightMode.PARKING
            HeadlightMode.PARKING -> HeadlightMode.LOW_BEAM
            HeadlightMode.LOW_BEAM -> HeadlightMode.HIGH_BEAM
            HeadlightMode.HIGH_BEAM -> HeadlightMode.OFF
        }
        _vehicleState.update { it.copy(headlightMode = nextMode) }
    }

    fun setHeadlightMode(mode: HeadlightMode) {
        _vehicleState.update { it.copy(headlightMode = mode) }
    }

    fun toggleCabinLight() {
        _vehicleState.update { it.copy(isCabinLightOn = !it.isCabinLightOn) }
    }

    fun toggleRoofAmbient() {
        _vehicleState.update { it.copy(isRoofAmbientStripOn = !it.isRoofAmbientStripOn) }
    }

    fun toggleFogLamps() {
        _vehicleState.update { it.copy(isFogLampsOn = !it.isFogLampsOn) }
    }

    fun toggleLeftIndicator() {
        val next = !_vehicleState.value.isLeftIndicatorOn
        _vehicleState.update { it.copy(isLeftIndicatorOn = next, isRightIndicatorOn = false) }
    }

    fun toggleRightIndicator() {
        val next = !_vehicleState.value.isRightIndicatorOn
        _vehicleState.update { it.copy(isRightIndicatorOn = next, isLeftIndicatorOn = false) }
    }

    fun toggleHazardFlasher() {
        val next = !_vehicleState.value.isHazardFlashing
        _vehicleState.update { it.copy(isHazardFlashing = next) }
    }

    fun cycleWiperMode() {
        val next = when (_vehicleState.value.wiperMode) {
            WiperMode.OFF -> WiperMode.SLOW
            WiperMode.SLOW -> WiperMode.FAST
            WiperMode.FAST -> WiperMode.OFF
        }
        _vehicleState.update { it.copy(wiperMode = next) }
    }

    fun toggleUsbCharging() {
        _vehicleState.update { it.copy(isPassengerUsbChargingOn = !it.isPassengerUsbChargingOn) }
    }

    fun triggerHorn() {
        TotoSoundEngine.playHorn(durationMs = 350)
    }

    fun toggleReverseBuzzerSetting() {
        val next = !_vehicleState.value.isReverseBuzzerEnabled
        _vehicleState.update { it.copy(isReverseBuzzerEnabled = next) }
        if (!next) {
            TotoSoundEngine.stopReverseBuzzer()
        } else if (_vehicleState.value.gearMode == GearMode.REVERSE) {
            TotoSoundEngine.startReverseBuzzer()
        }
    }

    // ==========================================
    // BMS & Battery Controls
    // ==========================================

    fun setBatteryPackVoltage(voltage: BatteryPackVoltage) {
        _vehicleState.update {
            it.copy(
                batteryPackVoltage = voltage,
                livePackVoltage = if (voltage == BatteryPackVoltage.VOLT_60) 65.4f else 53.2f
            )
        }
        persistCurrentSettings()
    }

    fun setBatteryType(type: BatteryType) {
        _vehicleState.update { it.copy(batteryType = type) }
        persistCurrentSettings()
    }

    fun toggleChargingStatus() {
        val next = when (_vehicleState.value.chargeStatus) {
            ChargeStatus.NOT_CHARGING -> ChargeStatus.AC_NORMAL_CHARGING
            ChargeStatus.AC_NORMAL_CHARGING -> ChargeStatus.FAST_DC_CHARGING
            ChargeStatus.FAST_DC_CHARGING -> ChargeStatus.NOT_CHARGING
        }
        _vehicleState.update { it.copy(chargeStatus = next) }
    }

    // ==========================================
    // Commercial Fare Meter & Trip Management
    // ==========================================

    fun toggleFareMeter() {
        val isRunning = _vehicleState.value.isFareMeterRunning
        if (!isRunning) {
            // Start trip
            _vehicleState.update {
                it.copy(
                    isFareMeterRunning = true,
                    activeTripDistanceKm = 0f,
                    activeTripElapsedSeconds = 0L,
                    activeTripFareRs = it.baseFareRs
                )
            }
            startFareMeterTicker()
        } else {
            // Stop trip and save record
            stopFareMeterAndSave()
        }
    }

    private fun startFareMeterTicker() {
        fareMeterJob?.cancel()
        fareMeterJob = viewModelScope.launch {
            while (_vehicleState.value.isFareMeterRunning) {
                delay(1000)
                _vehicleState.update { s ->
                    val newSeconds = s.activeTripElapsedSeconds + 1
                    // If moving, add distance
                    val addedDist = (s.currentSpeedKmH / 3600f)
                    val newDist = s.activeTripDistanceKm + addedDist
                    val newFare = s.baseFareRs + (newDist * s.ratePerKmRs)
                    s.copy(
                        activeTripElapsedSeconds = newSeconds,
                        activeTripDistanceKm = newDist,
                        activeTripFareRs = max(s.baseFareRs, newFare)
                    )
                }
            }
        }
    }

    private fun stopFareMeterAndSave() {
        fareMeterJob?.cancel()
        val s = _vehicleState.value
        val finalFare = s.activeTripFareRs
        val finalDist = s.activeTripDistanceKm
        val duration = s.activeTripElapsedSeconds
        val passengers = s.passengerCount

        viewModelScope.launch {
            repository.recordTrip(
                TotoTripEntity(
                    distanceKm = finalDist,
                    durationSeconds = duration,
                    passengers = passengers,
                    fareRs = finalFare,
                    startBatteryPercent = min(100, s.batteryPercent + 1),
                    endBatteryPercent = s.batteryPercent
                )
            )
        }

        _vehicleState.update {
            it.copy(
                isFareMeterRunning = false,
                todaysTotalEarningsRs = it.todaysTotalEarningsRs + finalFare,
                todaysCompletedTripsCount = it.todaysCompletedTripsCount + 1
            )
        }
    }

    fun updatePassengerCount(count: Int) {
        val valid = count.coerceIn(1, 6)
        _vehicleState.update { it.copy(passengerCount = valid) }
    }

    fun clearTripHistory() {
        viewModelScope.launch {
            repository.clearTrips()
            _vehicleState.update {
                it.copy(
                    todaysTotalEarningsRs = 0.0,
                    todaysCompletedTripsCount = 0
                )
            }
        }
    }

    // ==========================================
    // Connectivity & Diagnostics
    // ==========================================

    fun toggleBleConnection() {
        when (_vehicleState.value.connectionStatus) {
            ConnectionStatus.CONNECTED -> {
                _vehicleState.update { it.copy(connectionStatus = ConnectionStatus.DISCONNECTED) }
            }
            ConnectionStatus.DISCONNECTED -> {
                _vehicleState.update { it.copy(connectionStatus = ConnectionStatus.CONNECTING) }
                viewModelScope.launch {
                    delay(1200)
                    _vehicleState.update {
                        it.copy(
                            connectionStatus = ConnectionStatus.CONNECTED,
                            connectedDeviceName = "TOTO_BLE_ECU_48V",
                            signalRssi = -54
                        )
                    }
                }
            }
            ConnectionStatus.CONNECTING -> {
                _vehicleState.update { it.copy(connectionStatus = ConnectionStatus.DISCONNECTED) }
            }
        }
    }

    fun runFullDiagnosticSelfCheck() {
        viewModelScope.launch {
            _vehicleState.update { it.copy(isDiagnosticsAllPassed = false) }
            delay(500)
            _vehicleState.update { it.copy(motorHallSensorOk = true) }
            delay(400)
            _vehicleState.update { it.copy(throttleSignalOk = true) }
            delay(400)
            _vehicleState.update { it.copy(brakeCutoffSwitchOk = true) }
            delay(400)
            _vehicleState.update { it.copy(bmsCommunicationOk = true, isDiagnosticsAllPassed = true) }
            TotoSoundEngine.playHorn(durationMs = 80)
        }
    }

    // ==========================================
    // Internal Telemetry Engine Simulation
    // ==========================================

    private fun startTelemetryLoop() {
        telemetryLoopJob?.cancel()
        telemetryLoopJob = viewModelScope.launch {
            while (isActive) {
                delay(200) // 5Hz telemetry refresh
                updateTelemetryTick()
            }
        }
    }

    private fun updateTelemetryTick() {
        val s = _vehicleState.value
        if (!s.isMasterIgnitionOn || s.isMotorLocked) {
            // Speed slowly drops to 0
            if (s.currentSpeedKmH > 0f) {
                val newSpeed = max(0f, s.currentSpeedKmH - 1.5f)
                _vehicleState.update {
                    it.copy(
                        currentSpeedKmH = newSpeed,
                        motorRpm = (newSpeed * 58).toInt(),
                        motorCurrentAmps = 0f
                    )
                }
            }
            return
        }

        // Determine target speed based on Gear and Inching Throttle
        val maxAllowed = min(s.driveProfile.speedLimitKmH, s.speedGovernorLimitKmH).toFloat()
        var targetSpeed = 0f

        when (s.gearMode) {
            GearMode.PARK, GearMode.NEUTRAL -> {
                targetSpeed = 0f
            }
            GearMode.DRIVE -> {
                if (s.remoteCrawlerThrottlePercent > 0f) {
                    targetSpeed = (s.remoteCrawlerThrottlePercent / 100f) * maxAllowed
                } else {
                    // Simulating smooth cruising/traffic variation
                    targetSpeed = max(0f, s.currentSpeedKmH)
                }
            }
            GearMode.REVERSE -> {
                val maxReverse = 8f
                if (s.remoteCrawlerThrottlePercent > 0f) {
                    targetSpeed = (s.remoteCrawlerThrottlePercent / 100f) * maxReverse
                }
            }
        }

        val step = if (targetSpeed > s.currentSpeedKmH) 1.2f else 1.8f
        val current = s.currentSpeedKmH
        val newSpeed = if (targetSpeed > current) {
            min(targetSpeed, current + step)
        } else {
            max(targetSpeed, current - step)
        }

        val rpm = (newSpeed * 62).toInt()
        val amps = if (newSpeed > 0f) (newSpeed * 0.85f + 3.2f) else 0.8f
        val throttleVolts = if (newSpeed > 0f) (1.1f + (newSpeed / maxAllowed) * 3.1f) else 1.1f
        val distanceDelta = (newSpeed / 3600f) * 0.2f

        _vehicleState.update {
            it.copy(
                currentSpeedKmH = newSpeed,
                motorRpm = rpm,
                motorCurrentAmps = amps,
                throttleHallVoltage = throttleVolts,
                totalOdometerKm = it.totalOdometerKm + distanceDelta,
                tripDistanceKm = it.tripDistanceKm + distanceDelta,
                estimatedRemainingRangeKm = max(5, (it.batteryPercent * 0.9).toInt())
            )
        }
    }

    private fun startFlasherBlinkerLoop() {
        flasherBlinkJob?.cancel()
        flasherBlinkJob = viewModelScope.launch {
            var blinkState = false
            while (isActive) {
                delay(450)
                val s = _vehicleState.value
                val isBlinking = s.isLeftIndicatorOn || s.isRightIndicatorOn || s.isHazardFlashing
                if (isBlinking) {
                    blinkState = !blinkState
                    if (blinkState) {
                        TotoSoundEngine.playIndicatorClick()
                    }
                }
            }
        }
    }

    private fun persistCurrentSettings() {
        val s = _vehicleState.value
        viewModelScope.launch {
            repository.updateSettings(
                TotoSettingsEntity(
                    id = 1,
                    speedLimitKmH = s.speedGovernorLimitKmH,
                    regenLevel = s.regenBraking.name,
                    driveProfile = s.driveProfile.name,
                    batteryPackVolts = s.batteryPackVoltage.nominalVolts,
                    batteryChemistry = s.batteryType.name,
                    baseFareRs = s.baseFareRs,
                    perKmFareRs = s.ratePerKmRs,
                    languageCode = _selectedLanguage.value.code
                )
            )
        }
    }
}
