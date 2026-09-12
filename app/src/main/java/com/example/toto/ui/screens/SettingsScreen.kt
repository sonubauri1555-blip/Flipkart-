package com.example.toto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.TotoLocale
import com.example.toto.model.BatteryPackVoltage
import com.example.toto.model.BatteryType
import com.example.toto.model.ConnectionStatus
import com.example.toto.model.RegenLevel
import com.example.toto.ui.TotoViewModel
import com.example.ui.theme.CockpitBackground
import com.example.ui.theme.CockpitBorder
import com.example.ui.theme.CockpitCard
import com.example.ui.theme.CockpitCardSecondary
import com.example.ui.theme.EvAmber
import com.example.ui.theme.EvCyan
import com.example.ui.theme.EvEmerald
import com.example.ui.theme.EvRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
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
        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Settings",
                tint = EvCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "VEHICLE ECU & CONTROLLER SETTINGS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 1.sp
            )
        }

        // 1. SPEED GOVERNOR LIMIT (COMMERCIAL TRANSPORT REGULATION)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SPEED GOVERNOR LIMIT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Motor electronic limit (RTO / Safety regulation)",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Text(
                        text = "${state.speedGovernorLimitKmH} km/h",
                        color = EvCyan,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = state.speedGovernorLimitKmH.toFloat(),
                    onValueChange = { viewModel.setSpeedGovernorLimit(it.toInt()) },
                    valueRange = 15f..45f,
                    steps = 29,
                    colors = SliderDefaults.colors(
                        thumbColor = EvCyan,
                        activeTrackColor = EvCyan,
                        inactiveTrackColor = Color(0xFF1E293B)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "15 km/h (Slow)", fontSize = 10.sp, color = TextMuted)
                    Text(text = "25 km/h (Standard)", fontSize = 10.sp, color = TextMuted)
                    Text(text = "45 km/h (Max)", fontSize = 10.sp, color = TextMuted)
                }
            }
        }

        // 2. REGENERATIVE BRAKING STRENGTH
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "REGENERATIVE BRAKING ENERGY HARVEST",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Charges battery pack when releasing throttle or applying brakes",
                    fontSize = 10.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RegenLevel.values().forEach { regen ->
                        val isSelected = state.regenBraking == regen
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) EvEmerald.copy(alpha = 0.25f)
                                    else CockpitCardSecondary
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) EvEmerald else Color(0xFF334155),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.setRegenBraking(regen) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = regen.label,
                                color = if (isSelected) EvEmerald else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 3. BATTERY PACK VOLTAGE ARCHITECTURE (48V vs 60V)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "BATTERY VOLTAGE ARCHITECTURE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Select 48V (Standard 4-battery) or 60V (High-speed 5-battery)",
                    fontSize = 10.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BatteryPackVoltage.values().forEach { pack ->
                        val isSelected = state.batteryPackVoltage == pack
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) Color(0xFF1E3A8A)
                                    else CockpitCardSecondary
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) EvCyan else Color(0xFF334155),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.setBatteryPackVoltage(pack) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = pack.title,
                                    color = if (isSelected) EvCyan else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Cutoff: ${pack.cutoffVolts}V",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. BATTERY CHEMISTRY SELECTION
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "BATTERY CHEMISTRY TYPE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BatteryType.values().forEach { chem ->
                        val isSelected = state.batteryType == chem
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) Color(0xFF064E3B)
                                    else CockpitCardSecondary
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) EvEmerald else Color(0xFF334155),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.setBatteryType(chem) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chem.label,
                                color = if (isSelected) EvEmerald else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 5. BLUETOOTH BLE HARDWARE CONNECTION
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bluetooth,
                        contentDescription = "BLE",
                        tint = EvCyan
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "BLE HARDWARE INTERFACE",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${state.connectedDeviceName} (${state.connectionStatus.name})",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (state.connectionStatus == ConnectionStatus.CONNECTED) EvRed.copy(alpha = 0.2f) else EvEmerald.copy(alpha = 0.2f))
                        .border(1.dp, if (state.connectionStatus == ConnectionStatus.CONNECTED) EvRed else EvEmerald, RoundedCornerShape(12.dp))
                        .clickable { viewModel.toggleBleConnection() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (state.connectionStatus == ConnectionStatus.CONNECTED) "DISCONNECT" else "CONNECT",
                        color = if (state.connectionStatus == ConnectionStatus.CONNECTED) EvRed else EvEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 6. RUN FULL DIAGNOSTIC SELF-CHECK
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ECU SELF-DIAGNOSTIC TEST",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Validates motor hall sensors, throttle & brake switches",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E3A8A))
                            .clickable { viewModel.runFullDiagnosticSelfCheck() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "RUN TEST", color = EvCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                DiagnosticItem(title = "BLDC Motor Hall Sensors", isOk = state.motorHallSensorOk)
                DiagnosticItem(title = "Throttle 0-5V Linear Voltage", isOk = state.throttleSignalOk)
                DiagnosticItem(title = "Brake Cut-off Safety Switch", isOk = state.brakeCutoffSwitchOk)
                DiagnosticItem(title = "BMS CAN/UART Data Bus", isOk = state.bmsCommunicationOk)
            }
        }

        // 7. LANGUAGE SELECTION
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "APP DISPLAY LANGUAGE (ভাষা)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppLanguage.values().forEach { lang ->
                        val isSelected = language == lang
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) EvEmerald.copy(alpha = 0.2f) else CockpitCardSecondary)
                                .border(1.dp, if (isSelected) EvEmerald else Color(0xFF334155), RoundedCornerShape(10.dp))
                                .clickable { viewModel.setLanguage(lang) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${lang.flag} ${lang.displayName}",
                                color = if (isSelected) EvEmerald else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun DiagnosticItem(title: String, isOk: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 12.sp, color = TextSecondary)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isOk) Icons.Default.CheckCircle else Icons.Default.Error,
                contentDescription = null,
                tint = if (isOk) EvEmerald else EvRed,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isOk) "PASS" else "FAIL",
                color = if (isOk) EvEmerald else EvRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
