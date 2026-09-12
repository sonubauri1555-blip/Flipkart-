package com.example.toto.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.TotoLocale
import com.example.toto.model.DriveProfile
import com.example.toto.model.GearMode
import com.example.toto.model.TotoVehicleState
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
fun CockpitQuickControls(
    state: TotoVehicleState,
    currentLanguage: AppLanguage,
    onToggleIgnition: () -> Unit,
    onSelectGear: (GearMode) -> Unit,
    onSelectDriveProfile: (DriveProfile) -> Unit,
    onTriggerHorn: () -> Unit,
    onInchingForward: (Float) -> Unit,
    onInchingReverse: (Float) -> Unit,
    onStopInching: () -> Unit,
    onToggleLeftIndicator: () -> Unit,
    onToggleRightIndicator: () -> Unit,
    onToggleHazard: () -> Unit,
    onToggleMotorLock: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. MASTER IGNITION & GEAR SELECTOR ROW
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Master Start / Stop Power Button
            val ignitionGlowColor by animateColorAsState(
                targetValue = if (state.isMasterIgnitionOn) EvEmerald else Color(0xFF475569),
                label = "ignition_glow"
            )

            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                if (state.isMasterIgnitionOn) Color(0xFF047857) else Color(0xFF1E293B),
                                if (state.isMasterIgnitionOn) Color(0xFF064E3B) else Color(0xFF0F172A)
                            )
                        )
                    )
                    .border(3.dp, ignitionGlowColor, CircleShape)
                    .clickable { onToggleIgnition() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Ignition",
                        tint = if (state.isMasterIgnitionOn) Color.White else TextMuted,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (state.isMasterIgnitionOn) "STOP" else "START",
                        color = if (state.isMasterIgnitionOn) Color(0xFF6EE7B7) else TextSecondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Gear Selector [ P ] [ R ] [ N ] [ D ]
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(CockpitCard, RoundedCornerShape(18.dp))
                    .border(1.dp, CockpitBorder, RoundedCornerShape(18.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "TRANSMISSION GEAR",
                    fontSize = 10.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GearMode.values().forEach { gear ->
                        val isSelected = state.gearMode == gear
                        val gearColor = when (gear) {
                            GearMode.PARK -> EvRed
                            GearMode.REVERSE -> EvAmber
                            GearMode.NEUTRAL -> Color(0xFF94A3B8)
                            GearMode.DRIVE -> EvEmerald
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) gearColor.copy(alpha = 0.25f)
                                    else CockpitCardSecondary
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) gearColor else Color(0xFF334155),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = state.isMasterIgnitionOn) { onSelectGear(gear) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = gear.shortCode,
                                color = if (isSelected) gearColor else TextMuted,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // 2. TURN SIGNALS & 4-WAY HAZARD FLASHER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CockpitCard, RoundedCornerShape(18.dp))
                .border(1.dp, CockpitBorder, RoundedCornerShape(18.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Indicator
            Box(
                modifier = Modifier
                    .size(54.dp, 44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (state.isLeftIndicatorOn) EvAmber.copy(alpha = 0.3f)
                        else CockpitCardSecondary
                    )
                    .border(
                        1.dp,
                        if (state.isLeftIndicatorOn) EvAmber else Color(0xFF334155),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onToggleLeftIndicator() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Left Indicator",
                    tint = if (state.isLeftIndicatorOn) EvAmber else TextMuted
                )
            }

            // 4-Way Hazard Flasher (Center button)
            Box(
                modifier = Modifier
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (state.isHazardFlashing) EvRed.copy(alpha = 0.35f)
                        else CockpitCardSecondary
                    )
                    .border(
                        1.5.dp,
                        if (state.isHazardFlashing) EvRed else Color(0xFF475569),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onToggleHazard() }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Hazard Flasher",
                        tint = if (state.isHazardFlashing) EvRed else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "HAZARD 4-WAY",
                        color = if (state.isHazardFlashing) EvRed else TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            // Right Indicator
            Box(
                modifier = Modifier
                    .size(54.dp, 44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (state.isRightIndicatorOn) EvAmber.copy(alpha = 0.3f)
                        else CockpitCardSecondary
                    )
                    .border(
                        1.dp,
                        if (state.isRightIndicatorOn) EvAmber else Color(0xFF334155),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onToggleRightIndicator() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Right Indicator",
                    tint = if (state.isRightIndicatorOn) EvAmber else TextMuted
                )
            }
        }

        // 3. GIANT ELECTRIC HORN BUTTON (Hold or Tap to blast!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFD97706), Color(0xFFF59E0B), Color(0xFFD97706))
                    )
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onTriggerHorn()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Horn",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = TotoLocale.hornButton(currentLanguage),
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        // 4. DRIVE MODES (Eco, City, Turbo, Heavy Load)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CockpitCard, RoundedCornerShape(18.dp))
                .border(1.dp, CockpitBorder, RoundedCornerShape(18.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "DRIVE SPEED MODE & TORQUE",
                fontSize = 10.sp,
                color = TextMuted,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DriveProfile.values().forEach { profile ->
                    val isSelected = state.driveProfile == profile
                    val profileColor = when (profile) {
                        DriveProfile.ECO -> EvEmerald
                        DriveProfile.CITY -> EvCyan
                        DriveProfile.TURBO -> Color(0xFF8B5CF6)
                        DriveProfile.HEAVY_LOAD -> EvAmber
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) profileColor.copy(alpha = 0.25f)
                                else CockpitCardSecondary
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) profileColor else Color(0xFF334155),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectDriveProfile(profile) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.BENGALI -> profile.bengaliTitle
                                    else -> profile.title
                                },
                                color = if (isSelected) profileColor else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "${profile.speedLimitKmH} km/h",
                                color = if (isSelected) Color.White else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }

        // 5. REMOTE CRAWLER / INCHING (FOR PASSENGER BOARDING & PARKING)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CockpitCard, RoundedCornerShape(18.dp))
                .border(1.dp, CockpitBorder, RoundedCornerShape(18.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REMOTE INCHING (SLOW CRAWL)",
                    fontSize = 10.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (state.isRemoteParkingInching) "MOVING" else "IDLE",
                    color = if (state.isRemoteParkingInching) EvEmerald else TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Forward Crawl Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (state.gearMode == GearMode.DRIVE && state.isRemoteParkingInching) EvEmerald
                            else Color(0xFF1E3A8A)
                        )
                        .pointerInput(state.isMasterIgnitionOn) {
                            detectTapGestures(
                                onPress = {
                                    onInchingForward(40f)
                                    tryAwaitRelease()
                                    onStopInching()
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▲ " + TotoLocale.crawlerForward(currentLanguage),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Reverse Crawl Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (state.gearMode == GearMode.REVERSE && state.isRemoteParkingInching) EvAmber
                            else Color(0xFF334155)
                        )
                        .pointerInput(state.isMasterIgnitionOn) {
                            detectTapGestures(
                                onPress = {
                                    onInchingReverse(40f)
                                    tryAwaitRelease()
                                    onStopInching()
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▼ " + TotoLocale.crawlerReverse(currentLanguage),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // 6. ANTI-THEFT ELECTRONIC MOTOR BRAKE LOCK
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    if (state.isMotorLocked) EvRed.copy(alpha = 0.2f)
                    else Color(0xFF1E293B)
                )
                .border(
                    1.dp,
                    if (state.isMotorLocked) EvRed else CockpitBorder,
                    RoundedCornerShape(18.dp)
                )
                .clickable { onToggleMotorLock() }
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (state.isMotorLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = "Motor Lock",
                    tint = if (state.isMotorLocked) EvRed else EvEmerald,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (state.isMotorLocked) TotoLocale.motorLocked(currentLanguage)
                        else TotoLocale.motorUnlocked(currentLanguage),
                        color = if (state.isMotorLocked) EvRed else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = if (state.isMotorLocked) "Electronic regenerative brake locked. Wheels cannot rotate."
                        else "Tap to engage wheel anti-theft electronic lock",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
