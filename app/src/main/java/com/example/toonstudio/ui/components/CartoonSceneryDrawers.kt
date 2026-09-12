package com.example.toonstudio.ui.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.toonstudio.model.CartoonEnvironment
import com.example.toonstudio.model.WeatherEffect
import com.example.ui.theme.CityConcrete
import com.example.ui.theme.DarkForestGreen
import com.example.ui.theme.GrassGreen
import com.example.ui.theme.PalaceGold
import com.example.ui.theme.RiverAqua
import com.example.ui.theme.SkyDayBlue
import com.example.ui.theme.SkyNightDeep
import com.example.ui.theme.SkySunsetOrange
import com.example.ui.theme.SpaceVoid
import kotlin.math.sin

fun DrawScope.drawCartoonEnvironment(
    environment: CartoonEnvironment,
    animationTimeMs: Long
) {
    val w = size.width
    val h = size.height

    when (environment) {
        CartoonEnvironment.VILLAGE_FARM -> drawVillageScenery(w, h, animationTimeMs)
        CartoonEnvironment.DEEP_JUNGLE -> drawJungleScenery(w, h, animationTimeMs)
        CartoonEnvironment.ROYAL_PALACE -> drawPalaceScenery(w, h)
        CartoonEnvironment.CITY_STREET -> drawCityScenery(w, h)
        CartoonEnvironment.SCHOOL_CLASSROOM -> drawClassroomScenery(w, h)
        CartoonEnvironment.NIGHT_FOREST -> drawNightForestScenery(w, h, animationTimeMs)
        CartoonEnvironment.RIVER_SUNSET -> drawRiverSunsetScenery(w, h, animationTimeMs)
        CartoonEnvironment.OUTER_SPACE -> drawSpaceScenery(w, h, animationTimeMs)
        CartoonEnvironment.CLOUD_KINGDOM -> drawCloudKingdomScenery(w, h, animationTimeMs)
    }
}

fun DrawScope.drawWeatherEffect(
    weather: WeatherEffect,
    animationTimeMs: Long
) {
    val w = size.width
    val h = size.height

    when (weather) {
        WeatherEffect.RAIN -> {
            val dropCount = 40
            for (i in 0 until dropCount) {
                val seedX = (i * 137) % w.toInt()
                val speed = 800f + (i % 5) * 100f
                val y = ((animationTimeMs * 1.5f + i * 90) % h)
                val x = (seedX - (animationTimeMs * 0.3f) % 60f + w) % w
                drawLine(
                    color = Color(0xB381D4FA),
                    start = Offset(x, y),
                    end = Offset(x - 10f, y + 25f),
                    strokeWidth = 2.5f
                )
            }
        }
        WeatherEffect.SNOW -> {
            val flakeCount = 30
            for (i in 0 until flakeCount) {
                val seedX = (i * 181) % w.toInt()
                val y = ((animationTimeMs * 0.2f + i * 110) % h)
                val sway = sin((animationTimeMs + i * 200) / 300.0).toFloat() * 15f
                val x = (seedX + sway + w) % w
                drawCircle(Color(0xE6FFFFFF), radius = 3f + (i % 3), center = Offset(x, y))
            }
        }
        WeatherEffect.NIGHT_STARS -> {
            for (i in 0 until 35) {
                val x = ((i * 223) % w.toInt()).toFloat()
                val y = ((i * 127) % (h * 0.55f).toInt()).toFloat()
                val twinkle = (sin((animationTimeMs + i * 350) / 200.0) * 0.5 + 0.5).toFloat()
                drawCircle(Color.White.copy(alpha = 0.3f + twinkle * 0.7f), radius = 2f + twinkle * 2f, center = Offset(x, y))
            }
        }
        WeatherEffect.SUNSET_GLOW -> {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0x33FF5722), Color(0x4DFF9800), Color(0x1AFFEB3B))
                ),
                size = size
            )
        }
        WeatherEffect.MAGIC_SPARKLES -> {
            for (i in 0 until 20) {
                val x = ((i * 311) % w.toInt()).toFloat()
                val y = ((i * 197) % h.toInt()).toFloat()
                val sparklePulse = (sin((animationTimeMs + i * 250) / 150.0) * 0.5 + 0.5).toFloat()
                val sparkColor = if (i % 2 == 0) Color(0xFFFFD700) else Color(0xFF00E5FF)
                drawCircle(sparkColor.copy(alpha = sparklePulse * 0.8f), radius = 3f + sparklePulse * 4f, center = Offset(x, y))
            }
        }
        WeatherEffect.THUNDER_FLASH -> {
            val flash = (animationTimeMs % 2000L) in 100L..250L
            if (flash) {
                drawRect(Color.White.copy(alpha = 0.45f), size = size)
            }
        }
        WeatherEffect.NONE -> {}
    }
}

// Sceneries
private fun DrawScope.drawVillageScenery(w: Float, h: Float, timeMs: Long) {
    // Sky
    drawRect(
        brush = Brush.verticalGradient(listOf(SkyDayBlue, Color(0xFFBBDEFB))),
        size = Size(w, h * 0.65f)
    )
    // Sun
    drawCircle(Color(0xFFFFEE58), radius = 32f, center = Offset(w * 0.82f, h * 0.18f))

    // Clouds floating
    val cloudOffset1 = (timeMs / 100f) % (w + 100f) - 50f
    drawCloud(Offset(cloudOffset1, h * 0.15f))
    val cloudOffset2 = ((timeMs / 140f) + 200f) % (w + 120f) - 60f
    drawCloud(Offset(cloudOffset2, h * 0.25f), scale = 0.8f)

    // Distant Rolling Hills
    val hillPath = Path().apply {
        moveTo(0f, h * 0.60f)
        cubicTo(w * 0.25f, h * 0.48f, w * 0.55f, h * 0.68f, w, h * 0.54f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(hillPath, color = Color(0xFF81C784))

    // Lush Ground Field
    drawRect(GrassGreen, topLeft = Offset(0f, h * 0.60f), size = Size(w, h * 0.40f))

    // Clay Mud Hut (Bengali Matir Ghor)
    val hutX = w * 0.12f
    val hutY = h * 0.52f
    drawRoundRect(Color(0xFFD7CCC8), topLeft = Offset(hutX, hutY), size = Size(90f, 65f), cornerRadius = CornerRadius(6f, 6f))
    // Thatched Straw Roof
    val roof = Path().apply {
        moveTo(hutX - 12f, hutY)
        lineTo(hutX + 45f, hutY - 35f)
        lineTo(hutX + 102f, hutY)
        close()
    }
    drawPath(roof, color = Color(0xFF8D6E63))
    // Door
    drawRoundRect(Color(0xFF5D4037), topLeft = Offset(hutX + 32f, hutY + 20f), size = Size(26f, 45f), cornerRadius = CornerRadius(4f, 4f))

    // Palm / Coconut Tree
    drawPalmTree(Offset(w * 0.88f, h * 0.62f))
}

private fun DrawScope.drawJungleScenery(w: Float, h: Float, timeMs: Long) {
    // Deep green atmosphere
    drawRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF0D3813), DarkForestGreen, Color(0xFF1B5E20))),
        size = Size(w, h)
    )
    // Sunlight beams
    drawLine(Color(0x22FFF59D), start = Offset(w * 0.3f, 0f), end = Offset(w * 0.1f, h), strokeWidth = 35f)
    drawLine(Color(0x22FFF59D), start = Offset(w * 0.7f, 0f), end = Offset(w * 0.5f, h), strokeWidth = 45f)

    // Tree trunks
    drawRect(Color(0xFF3E2723), topLeft = Offset(w * 0.05f, 0f), size = Size(40f, h))
    drawRect(Color(0xFF4E342E), topLeft = Offset(w * 0.85f, 0f), size = Size(50f, h))

    // Jungle Foliage & Ground
    drawRect(Color(0xFF2E7D32), topLeft = Offset(0f, h * 0.65f), size = Size(w, h * 0.35f))
    drawCircle(Color(0xFF388E3C), radius = 50f, center = Offset(w * 0.2f, h * 0.7f))
    drawCircle(Color(0xFF43A047), radius = 60f, center = Offset(w * 0.75f, h * 0.72f))
}

private fun DrawScope.drawPalaceScenery(w: Float, h: Float) {
    // Ornate Royal Hall
    drawRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF3E2723), Color(0xFF5D4037))),
        size = Size(w, h * 0.65f)
    )
    // Marble Ground with Red Carpet
    drawRect(Color(0xFFD7CCC8), topLeft = Offset(0f, h * 0.65f), size = Size(w, h * 0.35f))
    drawRect(Color(0xFFB71C1C), topLeft = Offset(w * 0.3f, h * 0.65f), size = Size(w * 0.4f, h * 0.35f))
    // Gold borders on carpet
    drawLine(PalaceGold, start = Offset(w * 0.3f, h * 0.65f), end = Offset(w * 0.3f, h), strokeWidth = 6f)
    drawLine(PalaceGold, start = Offset(w * 0.7f, h * 0.65f), end = Offset(w * 0.7f, h), strokeWidth = 6f)

    // Golden Pillars
    drawPillar(w * 0.08f, h)
    drawPillar(w * 0.84f, h)

    // Throne Dais in the back center
    drawRoundRect(PalaceGold, topLeft = Offset(w * 0.42f, h * 0.48f), size = Size(w * 0.16f, h * 0.18f), cornerRadius = CornerRadius(10f, 10f))
}

private fun DrawScope.drawCityScenery(w: Float, h: Float) {
    // Evening City Sky
    drawRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF37474F), Color(0xFF78909C))),
        size = Size(w, h * 0.65f)
    )
    // Skyscrapers
    drawRect(Color(0xFF263238), topLeft = Offset(w * 0.05f, h * 0.20f), size = Size(65f, h * 0.45f))
    drawRect(Color(0xFF37474F), topLeft = Offset(w * 0.25f, h * 0.12f), size = Size(80f, h * 0.53f))
    drawRect(Color(0xFF1E272C), topLeft = Offset(w * 0.52f, h * 0.24f), size = Size(70f, h * 0.41f))
    drawRect(Color(0xFF455A64), topLeft = Offset(w * 0.75f, h * 0.16f), size = Size(85f, h * 0.49f))

    // Windows with yellow lights
    drawCircle(Color(0xFFFFEE58), radius = 4f, center = Offset(w * 0.28f, h * 0.22f))
    drawCircle(Color(0xFFFFEE58), radius = 4f, center = Offset(w * 0.35f, h * 0.30f))
    drawCircle(Color(0xFFFFEE58), radius = 4f, center = Offset(w * 0.80f, h * 0.25f))

    // Street Road
    drawRect(CityConcrete, topLeft = Offset(0f, h * 0.65f), size = Size(w, h * 0.35f))
    // Zebra Crossing stripes
    for (i in 0 until 6) {
        drawRect(Color.White, topLeft = Offset(w * (0.2f + i * 0.1f), h * 0.82f), size = Size(25f, 10f))
    }
}

private fun DrawScope.drawClassroomScenery(w: Float, h: Float) {
    // Classroom Wall
    drawRect(Color(0xFFFFF9C4), size = Size(w, h * 0.65f))
    // Floor
    drawRect(Color(0xFF8D6E63), topLeft = Offset(0f, h * 0.65f), size = Size(w, h * 0.35f))

    // Green Blackboard
    val bbX = w * 0.2f
    val bbY = h * 0.15f
    val bbW = w * 0.6f
    val bbH = h * 0.38f
    drawRoundRect(Color(0xFF5D4037), topLeft = Offset(bbX - 6f, bbY - 6f), size = Size(bbW + 12f, bbH + 12f), cornerRadius = CornerRadius(6f, 6f))
    drawRect(Color(0xFF1B5E20), topLeft = Offset(bbX, bbY), size = Size(bbW, bbH))

    // Chalk line on blackboard
    drawLine(Color.White.copy(alpha = 0.7f), start = Offset(bbX + 20f, bbY + 30f), end = Offset(bbX + bbW - 20f, bbY + 30f), strokeWidth = 2f)
}

private fun DrawScope.drawNightForestScenery(w: Float, h: Float, timeMs: Long) {
    // Midnight Sky
    drawRect(
        brush = Brush.verticalGradient(listOf(SkyNightDeep, Color(0xFF1A237E))),
        size = Size(w, h * 0.65f)
    )
    // Crescent Moon
    drawCircle(Color(0xFFFFF9C4), radius = 28f, center = Offset(w * 0.82f, h * 0.18f))
    drawCircle(SkyNightDeep, radius = 24f, center = Offset(w * 0.79f, h * 0.16f))

    // Spooky Tree Silhouettes
    drawSpookyTree(Offset(w * 0.15f, h * 0.65f))
    drawSpookyTree(Offset(w * 0.85f, h * 0.65f))

    // Dark ground
    drawRect(Color(0xFF0F172A), topLeft = Offset(0f, h * 0.65f), size = Size(w, h * 0.35f))
}

private fun DrawScope.drawRiverSunsetScenery(w: Float, h: Float, timeMs: Long) {
    // Sunset Sky
    drawRect(
        brush = Brush.verticalGradient(listOf(SkySunsetOrange, Color(0xFFFFD54F), Color(0xFFFFCC80))),
        size = Size(w, h * 0.55f)
    )
    // Golden Sun dipping into horizon
    drawCircle(Color(0xFFFF5722), radius = 35f, center = Offset(w * 0.5f, h * 0.52f))

    // Distant Mountain Ridges
    val mtn = Path().apply {
        moveTo(0f, h * 0.55f)
        lineTo(w * 0.25f, h * 0.38f)
        lineTo(w * 0.55f, h * 0.55f)
        lineTo(w * 0.78f, h * 0.40f)
        lineTo(w, h * 0.55f)
        close()
    }
    drawPath(mtn, color = Color(0xFF5D4037))

    // Flowing River
    drawRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF0288D1), RiverAqua)),
        topLeft = Offset(0f, h * 0.55f),
        size = Size(w, h * 0.45f)
    )

    // Wooden Boat
    val boatX = w * 0.3f
    val boatY = h * 0.72f
    val boatPath = Path().apply {
        moveTo(boatX, boatY)
        lineTo(boatX + 50f, boatY)
        lineTo(boatX + 42f, boatY + 12f)
        lineTo(boatX + 8f, boatY + 12f)
        close()
    }
    drawPath(boatPath, color = Color(0xFF4E342E))
}

private fun DrawScope.drawSpaceScenery(w: Float, h: Float, timeMs: Long) {
    drawRect(SpaceVoid, size = Size(w, h))

    // Distant Nebula
    drawCircle(
        brush = Brush.radialGradient(listOf(Color(0x55E040FB), Color(0x00000000))),
        radius = 120f,
        center = Offset(w * 0.3f, h * 0.3f)
    )

    // Ringed Planet
    val planetCenter = Offset(w * 0.75f, h * 0.25f)
    drawCircle(Color(0xFF00E5FF), radius = 28f, center = planetCenter)
    drawOval(Color(0xFFFFD700), topLeft = Offset(planetCenter.x - 45f, planetCenter.y - 10f), size = Size(90f, 20f), style = Stroke(4f))

    // Cosmic platform
    drawRoundRect(Color(0xFF263238), topLeft = Offset(0f, h * 0.70f), size = Size(w, h * 0.30f), cornerRadius = CornerRadius(16f, 16f))
}

private fun DrawScope.drawCloudKingdomScenery(w: Float, h: Float, timeMs: Long) {
    drawRect(
        brush = Brush.verticalGradient(listOf(Color(0xFFE1BEE7), Color(0xFFB39DDB), Color(0xFF80DEEA))),
        size = Size(w, h)
    )

    // Rainbow Arc
    drawArc(
        brush = Brush.sweepGradient(listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Magenta)),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(w * 0.1f, h * 0.1f),
        size = Size(w * 0.8f, h * 0.8f),
        style = Stroke(12f)
    )

    // Fluffy cloud banks as ground
    drawCloud(Offset(w * 0.2f, h * 0.72f), scale = 1.4f)
    drawCloud(Offset(w * 0.55f, h * 0.75f), scale = 1.6f)
    drawCloud(Offset(w * 0.85f, h * 0.70f), scale = 1.3f)
}

// Scenery Helpers
private fun DrawScope.drawCloud(center: Offset, scale: Float = 1.0f) {
    val r = 22f * scale
    drawCircle(Color(0xE6FFFFFF), radius = r, center = center)
    drawCircle(Color(0xE6FFFFFF), radius = r * 1.25f, center = Offset(center.x + r, center.y - r * 0.3f))
    drawCircle(Color(0xE6FFFFFF), radius = r * 0.9f, center = Offset(center.x + r * 2f, center.y))
    drawRoundRect(Color(0xE6FFFFFF), topLeft = Offset(center.x - r * 0.5f, center.y - r * 0.2f), size = Size(r * 2.8f, r * 1.3f), cornerRadius = CornerRadius(r, r))
}

private fun DrawScope.drawPalmTree(base: Offset) {
    // Curved trunk
    val trunk = Path().apply {
        moveTo(base.x - 8f, base.y)
        cubicTo(base.x - 12f, base.y - 60f, base.x + 8f, base.y - 110f, base.x + 12f, base.y - 150f)
        lineTo(base.x + 22f, base.y - 150f)
        cubicTo(base.x + 18f, base.y - 110f, base.x + 2f, base.y - 60f, base.x + 8f, base.y)
        close()
    }
    drawPath(trunk, color = Color(0xFF6D4C41))

    // Palm fronds / leaves
    val top = Offset(base.x + 17f, base.y - 150f)
    drawCircle(Color(0xFF2E7D32), radius = 35f, center = top)
    drawCircle(Color(0xFF388E3C), radius = 28f, center = Offset(top.x - 22f, top.y + 6f))
    drawCircle(Color(0xFF388E3C), radius = 28f, center = Offset(top.x + 22f, top.y + 6f))
}

private fun DrawScope.drawPillar(x: Float, h: Float) {
    drawRoundRect(PalaceGold, topLeft = Offset(x, h * 0.10f), size = Size(36f, h * 0.60f), cornerRadius = CornerRadius(4f, 4f))
    drawRect(Color(0xFFFFA000), topLeft = Offset(x - 6f, h * 0.10f), size = Size(48f, 14f))
    drawRect(Color(0xFFFFA000), topLeft = Offset(x - 6f, h * 0.67f), size = Size(48f, 16f))
}

private fun DrawScope.drawSpookyTree(base: Offset) {
    val trunk = Path().apply {
        moveTo(base.x - 12f, base.y)
        lineTo(base.x - 6f, base.y - 80f)
        lineTo(base.x - 30f, base.y - 120f)
        lineTo(base.x - 25f, base.y - 122f)
        lineTo(base.x - 2f, base.y - 85f)
        lineTo(base.x + 25f, base.y - 125f)
        lineTo(base.x + 28f, base.y - 122f)
        lineTo(base.x + 6f, base.y - 80f)
        lineTo(base.x + 12f, base.y)
        close()
    }
    drawPath(trunk, color = Color(0xFF1C1917))
}
