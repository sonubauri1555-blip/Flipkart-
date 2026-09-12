package com.example.toto.model

data class TotoVehicleState(
    // Security & Master Contactor
    val isMasterIgnitionOn: Boolean = false,
    val isMotorLocked: Boolean = false, // Electronic anti-theft brake lock
    val isAlarmArmed: Boolean = false,
    val isSirenRinging: Boolean = false,
    val isEmergencySos: Boolean = false,

    // Transmission & Motion
    val gearMode: GearMode = GearMode.PARK,
    val driveProfile: DriveProfile = DriveProfile.CITY,
    val regenBraking: RegenLevel = RegenLevel.MEDIUM,
    val speedGovernorLimitKmH: Int = 26,
    val isReverseBuzzerEnabled: Boolean = true,
    val remoteCrawlerThrottlePercent: Float = 0f, // Remote crawl inching
    val isRemoteParkingInching: Boolean = false,

    // Lighting & Aux Electronics
    val headlightMode: HeadlightMode = HeadlightMode.OFF,
    val isCabinLightOn: Boolean = false, // Passenger cabin roof LED
    val isRoofAmbientStripOn: Boolean = false, // Fancy passenger canopy LED strip
    val isFogLampsOn: Boolean = false,
    val isLeftIndicatorOn: Boolean = false,
    val isRightIndicatorOn: Boolean = false,
    val isHazardFlashing: Boolean = false,
    val isHornActive: Boolean = false,
    val wiperMode: WiperMode = WiperMode.OFF,
    val isPassengerUsbChargingOn: Boolean = true, // 5V 2.4A USB relay

    // Dynamic Telemetry & Drive Metrics
    val currentSpeedKmH: Float = 0f,
    val motorRpm: Int = 0,
    val motorCurrentAmps: Float = 0f,
    val motorTemperatureC: Int = 38,
    val controllerTemperatureC: Int = 42,
    val throttleHallVoltage: Float = 1.1f, // 1.1V idle to 4.2V max
    val totalOdometerKm: Float = 1428.5f,
    val tripDistanceKm: Float = 0f,

    // BMS (Battery Management System) - 48V/60V Pack
    val batteryType: BatteryType = BatteryType.LIFEPO4_LITHIUM,
    val batteryPackVoltage: BatteryPackVoltage = BatteryPackVoltage.VOLT_48,
    val livePackVoltage: Float = 53.2f, // e.g. 53.2V for full 48V LiFePO4
    val batteryPercent: Int = 85,
    val stateOfHealthSoH: Int = 97, // Battery health %
    val batteryPackTemperatureC: Int = 32,
    val estimatedRemainingRangeKm: Int = 76,
    val totalChargeCycles: Int = 214,
    val chargeStatus: ChargeStatus = ChargeStatus.NOT_CHARGING,
    val cellVoltages: List<Float> = listOf(
        3.33f, 3.32f, 3.33f, 3.34f,
        3.32f, 3.33f, 3.33f, 3.32f,
        3.34f, 3.33f, 3.32f, 3.33f,
        3.33f, 3.34f, 3.33f, 3.32f
    ),

    // Commercial Passenger Fare Meter
    val isFareMeterRunning: Boolean = false,
    val activeTripDistanceKm: Float = 0f,
    val activeTripElapsedSeconds: Long = 0L,
    val passengerCount: Int = 4, // Up to 6 passengers
    val baseFareRs: Double = 10.0,
    val ratePerKmRs: Double = 6.0,
    val activeTripFareRs: Double = 10.0,
    val todaysTotalEarningsRs: Double = 680.0,
    val todaysCompletedTripsCount: Int = 14,

    // ECU Connectivity & Diagnostics
    val connectionStatus: ConnectionStatus = ConnectionStatus.CONNECTED,
    val connectedDeviceName: String = "TOTO_BLE_ECU_48V",
    val signalRssi: Int = -58,
    val isDiagnosticsAllPassed: Boolean = true,
    val motorHallSensorOk: Boolean = true,
    val throttleSignalOk: Boolean = true,
    val brakeCutoffSwitchOk: Boolean = true,
    val bmsCommunicationOk: Boolean = true,
    val gpsLatLong: String = "22.5726° N, 88.3639° E (Kolkata Hub)"
)
