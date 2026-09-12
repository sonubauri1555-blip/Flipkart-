package com.example.toto.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toto.model.GearMode
import com.example.toto.model.TotoVehicleState
import com.example.ui.theme.CockpitCard
import com.example.ui.theme.EvCyan
import com.example.ui.theme.EvEmerald
import com.example.ui.theme.EvRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerGauge(
    state: TotoVehicleState,
    modifier: Modifier = Modifier
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = state.currentSpeedKmH,
        animationSpec = tween(durationMillis = 200),
        label = "speed_anim"
    )

    val maxSpeedGauge = 50f
    val sweepFraction = (animatedSpeed / maxSpeedGauge).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CockpitCard, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFF243247), RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top status pill (Gear & Drive Profile)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gear badge
            val gearBgColor = when (state.gearMode) {
                GearMode.PARK -> Color(0xFFEF4444)
                GearMode.REVERSE -> Color(0xFFF59E0B)
                GearMode.NEUTRAL -> Color(0xFF64748B)
                GearMode.DRIVE -> Color(0xFF10B981)
            }
            Box(
                modifier = Modifier
                    .background(gearBgColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .border(1.dp, gearBgColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.gearMode.shortCode,
                        color = gearBgColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = state.gearMode.label,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            // Power / Ready Status
            Box(
                modifier = Modifier
                    .background(
                        if (state.isMasterIgnitionOn) Color(0xFF10B981).copy(alpha = 0.15f)
                        else Color(0xFFEF4444).copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        if (state.isMasterIgnitionOn) Color(0xFF10B981) else Color(0xFFEF4444),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (state.isMasterIgnitionOn) "● READY TO DRIVE" else "○ IGNITION OFF",
                    color = if (state.isMasterIgnitionOn) Color(0xFF10B981) else Color(0xFFEF4444),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Circular Speedometer Canvas with Digital Speed in Center
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(230.dp)) {
                val strokeWidth = 16.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2f
                val centerOffset = Offset(size.width / 2f, size.height / 2f)

                val startAngle = 135f
                val totalSweep = 270f

                // Background track arc
                drawArc(
                    color = Color(0xFF1E293B),
                    startAngle = startAngle,
                    sweepAngle = totalSweep,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Active speed sweep arc with gradient
                val gradientBrush = Brush.sweepGradient(
                    0.0f to EvCyan,
                    0.5f to EvEmerald,
                    0.85f to Color(0xFFF59E0B),
                    1.0f to EvRed
                )

                if (sweepFraction > 0.01f) {
                    drawArc(
                        brush = gradientBrush,
                        startAngle = startAngle,
                        sweepAngle = totalSweep * sweepFraction,
                        useCenter = false,
                        topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Speed scale tick marks (0, 10, 20, 30, 40, 50)
                val numTicks = 5
                for (i in 0..numTicks) {
                    val angleDeg = startAngle + (totalSweep / numTicks) * i
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val tickInnerRadius = radius - 18.dp.toPx()
                    val tickOuterRadius = radius - 8.dp.toPx()

                    val p1 = Offset(
                        centerOffset.x + (tickInnerRadius * cos(angleRad)).toFloat(),
                        centerOffset.y + (tickInnerRadius * sin(angleRad)).toFloat()
                    )
                    val p2 = Offset(
                        centerOffset.x + (tickOuterRadius * cos(angleRad)).toFloat(),
                        centerOffset.y + (tickOuterRadius * sin(angleRad)).toFloat()
                    )

                    val isHighlighted = (i.toFloat() / numTicks) <= sweepFraction
                    drawLine(
                        color = if (isHighlighted) Color(0xFF67E8F9) else Color(0xFF475569),
                        start = p1,
                        end = p2,
                        strokeWidth = if (i % 2 == 0) 3.dp.toPx() else 1.5.dp.toPx()
                    )
                }
            }

            // Central Speed Display
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%.0f", animatedSpeed),
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = if (state.isMasterIgnitionOn) TextPrimary else TextMuted
                )
                Text(
                    text = "KM / H",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EvCyan,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Mode indicator
                Text(
                    text = state.driveProfile.title,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Telemetry strip: Motor RPM, Current Draw (A), Voltage (V)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A), RoundedCornerShape(16.dp))
                .padding(vertical = 10.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // RPM
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "MOTOR RPM", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                Text(text = "${state.motorRpm}", fontSize = 15.sp, color = EvCyan, fontWeight = FontWeight.Bold)
            }

            Box(modifier = Modifier.size(1.dp, 24.dp).background(Color(0xFF334155)))

            // Motor Current
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "CURRENT (A)", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                Text(
                    text = String.format("%.1f A", state.motorCurrentAmps),
                    fontSize = 15.sp,
                    color = EvEmerald,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(modifier = Modifier.size(1.dp, 24.dp).background(Color(0xFF334155)))

            // Live Pack Volts
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "PACK VOLTS", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                Text(
                    text = String.format("%.1f V", state.livePackVoltage),
                    fontSize = 15.sp,
                    color = Color(0xFFFBBF24),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Odometer & Trip readout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ODO: ${String.format("%.1f", state.totalOdometerKm)} km",
                fontSize = 11.sp,
                color = TextSecondary,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "TRIP: ${String.format("%.2f", state.tripDistanceKm)} km",
                fontSize = 11.sp,
                color = TextSecondary,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
