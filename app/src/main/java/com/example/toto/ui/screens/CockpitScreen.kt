package com.example.toto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.TotoLocale
import com.example.toto.ui.TotoViewModel
import com.example.toto.ui.components.CockpitQuickControls
import com.example.toto.ui.components.SpeedometerGauge
import com.example.ui.theme.CockpitBackground
import com.example.ui.theme.CockpitCard
import com.example.ui.theme.EvCyan
import com.example.ui.theme.EvEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun CockpitScreen(
    viewModel: TotoViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.vehicleState.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CockpitBackground)
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // High-Tech Digital Speedometer & Arc Gauge
        SpeedometerGauge(state = state)

        // Controller & Motor Temperatures Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "MOTOR TEMP: ", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${state.motorTemperatureC}°C",
                        fontSize = 12.sp,
                        color = if (state.motorTemperatureC > 65) Color(0xFFEF4444) else EvEmerald,
                        fontWeight = FontWeight.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "ECU TEMP: ", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${state.controllerTemperatureC}°C",
                        fontSize = 12.sp,
                        color = if (state.controllerTemperatureC > 60) Color(0xFFEF4444) else EvCyan,
                        fontWeight = FontWeight.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "THROTTLE: ", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                    Text(
                        text = String.format("%.1fV", state.throttleHallVoltage),
                        fontSize = 12.sp,
                        color = Color(0xFFFBBF24),
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Quick Drive Controls: Ignition, Gears, Modes, Horn, Inching, Locks
        CockpitQuickControls(
            state = state,
            currentLanguage = language,
            onToggleIgnition = { viewModel.toggleMasterIgnition() },
            onSelectGear = { viewModel.selectGear(it) },
            onSelectDriveProfile = { viewModel.selectDriveProfile(it) },
            onTriggerHorn = { viewModel.triggerHorn() },
            onInchingForward = { viewModel.setRemoteInching(it, isForward = true) },
            onInchingReverse = { viewModel.setRemoteInching(it, isForward = false) },
            onStopInching = { viewModel.stopRemoteInching() },
            onToggleLeftIndicator = { viewModel.toggleLeftIndicator() },
            onToggleRightIndicator = { viewModel.toggleRightIndicator() },
            onToggleHazard = { viewModel.toggleHazardFlasher() },
            onToggleMotorLock = { viewModel.toggleMotorAntiTheftLock() }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}
