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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.TotoLocale
import com.example.toto.model.HeadlightMode
import com.example.toto.model.WiperMode
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
fun SwitchesScreen(
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
                imageVector = Icons.Default.Tune,
                contentDescription = "Switches",
                tint = EvCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ELECTRICAL RELAY SWITCHBOARD",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 1.sp
            )
        }

        // 1. HEADLIGHT 4-STAGE SELECTOR
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Highlight,
                            contentDescription = "Headlight",
                            tint = if (state.headlightMode != HeadlightMode.OFF) EvCyan else TextMuted
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = TotoLocale.headlight(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = state.headlightMode.label,
                        color = if (state.headlightMode != HeadlightMode.OFF) EvCyan else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HeadlightMode.values().forEach { mode ->
                        val isSelected = state.headlightMode == mode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) EvCyan.copy(alpha = 0.25f)
                                    else CockpitCardSecondary
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) EvCyan else Color(0xFF334155),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.setHeadlightMode(mode) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (language) {
                                    AppLanguage.BENGALI -> mode.bengaliLabel
                                    else -> mode.label
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) EvCyan else TextMuted
                            )
                        }
                    }
                }
            }
        }

        // 2. GRID OF INTERACTIVE SWITCHES
        // Cabin Light & Roof Ambient LED
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SwitchToggleTile(
                title = TotoLocale.cabinLight(language),
                subtitle = "Interior 12V White LED",
                icon = Icons.Default.LightMode,
                isActive = state.isCabinLightOn,
                activeColor = EvEmerald,
                onToggle = { viewModel.toggleCabinLight() },
                modifier = Modifier.weight(1f)
            )

            SwitchToggleTile(
                title = TotoLocale.roofAmbient(language),
                subtitle = "Canopy RGB Strip",
                icon = Icons.Default.NightlightRound,
                isActive = state.isRoofAmbientStripOn,
                activeColor = Color(0xFF8B5CF6),
                onToggle = { viewModel.toggleRoofAmbient() },
                modifier = Modifier.weight(1f)
            )
        }

        // Fog Lamps & Passenger USB Charger
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SwitchToggleTile(
                title = TotoLocale.fogLamps(language),
                subtitle = "Front Amber Auxiliary",
                icon = Icons.Default.FlashlightOn,
                isActive = state.isFogLampsOn,
                activeColor = EvAmber,
                onToggle = { viewModel.toggleFogLamps() },
                modifier = Modifier.weight(1f)
            )

            SwitchToggleTile(
                title = TotoLocale.usbCharger(language),
                subtitle = "5V 2.4A Passenger Ports",
                icon = Icons.Default.Usb,
                isActive = state.isPassengerUsbChargingOn,
                activeColor = EvCyan,
                onToggle = { viewModel.toggleUsbCharging() },
                modifier = Modifier.weight(1f)
            )
        }

        // 3. WIPER MOTOR MULTI-SPEED CONTROLLER
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
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Wiper",
                        tint = if (state.wiperMode != WiperMode.OFF) EvCyan else TextMuted
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = TotoLocale.wiper(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Mode: ${state.wiperMode.label}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (state.wiperMode != WiperMode.OFF) EvCyan.copy(alpha = 0.2f)
                            else CockpitCardSecondary
                        )
                        .border(
                            1.dp,
                            if (state.wiperMode != WiperMode.OFF) EvCyan else Color(0xFF334155),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.cycleWiperMode() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "CHANGE MODE",
                        color = if (state.wiperMode != WiperMode.OFF) EvCyan else TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 4. SECURITY SIREN & REVERSE BUZZER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SwitchToggleTile(
                title = TotoLocale.alarmSiren(language),
                subtitle = "Loud Anti-Theft Siren",
                icon = Icons.Default.CrisisAlert,
                isActive = state.isSirenRinging,
                activeColor = EvRed,
                onToggle = { viewModel.toggleAlarmSiren() },
                modifier = Modifier.weight(1f)
            )

            SwitchToggleTile(
                title = "Reverse Buzzer",
                subtitle = "Pulsed Audio Warning",
                icon = Icons.Default.Campaign,
                isActive = state.isReverseBuzzerEnabled,
                activeColor = EvAmber,
                onToggle = { viewModel.toggleReverseBuzzerSetting() },
                modifier = Modifier.weight(1f)
            )
        }

        // 5. LOCATE MY TOTO (BEEP & FLASH)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF1E3A8A).copy(alpha = 0.4f))
                .border(1.dp, EvCyan, RoundedCornerShape(18.dp))
                .clickable { viewModel.triggerLocateToto() }
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Locate",
                    tint = EvCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = TotoLocale.locateVehicle(language),
                    color = EvCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun SwitchToggleTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    activeColor: Color,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(containerColor = CockpitCard),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isActive) 1.5.dp else 1.dp,
            color = if (isActive) activeColor else CockpitBorder
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isActive) activeColor.copy(alpha = 0.2f) else CockpitCardSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isActive) activeColor else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Switch(
                    checked = isActive,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = activeColor,
                        uncheckedThumbColor = Color(0xFF64748B),
                        uncheckedTrackColor = Color(0xFF1E293B)
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isActive) TextPrimary else TextSecondary,
                maxLines = 1
            )
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}
