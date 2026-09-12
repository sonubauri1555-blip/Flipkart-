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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.TotoLocale
import com.example.toto.model.BatteryPackVoltage
import com.example.toto.model.BatteryType
import com.example.toto.model.ChargeStatus
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
fun BatteryBmsScreen(
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.BatteryChargingFull,
                    contentDescription = "BMS",
                    tint = EvEmerald,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BMS TELEMETRY & PACK DIAGNOSTICS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
            }

            // Chemistry Badge
            Box(
                modifier = Modifier
                    .background(Color(0xFF064E3B), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = state.batteryType.label,
                    color = EvEmerald,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 1. HERO BATTERY SOC & VOLTAGE CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "STATE OF CHARGE (SoC)",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${state.batteryPercent}",
                                fontSize = 46.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Text(
                                text = "%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = EvEmerald,
                                modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                            )
                        }
                    }

                    // Pack Voltage Pill
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "PACK VOLTAGE",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format("%.1f V", state.livePackVoltage),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFBBF24)
                        )
                        Text(
                            text = "Nominal: ${state.batteryPackVoltage.nominalVolts}V",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { state.batteryPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = if (state.batteryPercent > 25) EvEmerald else EvRed,
                    trackColor = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3 Metrics: Range, SoH, Temp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CockpitCardSecondary, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = TotoLocale.remainingRange(language), fontSize = 10.sp, color = TextMuted)
                        Text(
                            text = "${state.estimatedRemainingRangeKm} km",
                            fontSize = 16.sp,
                            color = EvCyan,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Box(modifier = Modifier.size(1.dp, 24.dp).background(Color(0xFF334155)))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = TotoLocale.healthStatus(language), fontSize = 10.sp, color = TextMuted)
                        Text(
                            text = "${state.stateOfHealthSoH}%",
                            fontSize = 16.sp,
                            color = EvEmerald,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Box(modifier = Modifier.size(1.dp, 24.dp).background(Color(0xFF334155)))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "PACK TEMP", fontSize = 10.sp, color = TextMuted)
                        Text(
                            text = "${state.batteryPackTemperatureC}°C",
                            fontSize = 16.sp,
                            color = Color(0xFFFBBF24),
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        // 2. CHARGING CONTROL & STATUS
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
                        imageVector = Icons.Default.Power,
                        contentDescription = "Charging",
                        tint = if (state.chargeStatus != ChargeStatus.NOT_CHARGING) EvEmerald else TextMuted
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "CHARGING STATUS",
                            fontSize = 10.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when (language) {
                                AppLanguage.BENGALI -> state.chargeStatus.bengaliLabel
                                else -> state.chargeStatus.label
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (state.chargeStatus != ChargeStatus.NOT_CHARGING) EvEmerald else TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E3A8A))
                        .border(1.dp, EvCyan, RoundedCornerShape(12.dp))
                        .clickable { viewModel.toggleChargingStatus() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "TOGGLE CHARGER",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 3. 16S CELL BALANCING VOLTAGES (INDIVIDUAL CELL MONITORING)
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
                    Text(
                        text = TotoLocale.cellBalancing(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Balanced",
                            tint = EvEmerald,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Delta 0.02V OK",
                            color = EvEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Grid of 16 individual cell voltages
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    state.cellVoltages.chunked(4).forEachIndexed { rowIndex, rowCells ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rowCells.forEachIndexed { colIndex, cellVolt ->
                                val cellNum = rowIndex * 4 + colIndex + 1
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(CockpitCardSecondary, RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0xFF2B3D5B), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 6.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "C$cellNum", fontSize = 9.sp, color = TextMuted)
                                        Text(
                                            text = String.format("%.2fV", cellVolt),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EvCyan,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. BATTERY PROTECTION & SAFETY CUTOFF STATUS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CockpitCard),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CockpitBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "BMS HARDWARE SAFETY CHECKS",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold
                )

                SafetyStatusRow(title = "Low Voltage Cut-off Limit", value = "${state.batteryPackVoltage.cutoffVolts} V", isSafe = true)
                SafetyStatusRow(title = "Over-Temperature Limit", value = "65°C Cut-off", isSafe = true)
                SafetyStatusRow(title = "Total Charge Cycles", value = "${state.totalChargeCycles} Cycles", isSafe = true)
                SafetyStatusRow(title = "MOSFET Temperature", value = "38°C (Cool)", isSafe = true)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun SafetyStatusRow(title: String, value: String, isSafe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 12.sp, color = TextSecondary)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSafe) EvEmerald else EvRed
        )
    }
}
