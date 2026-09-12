package com.example.toonstudio.ui.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import com.example.toonstudio.model.CharacterExpression
import com.example.toonstudio.model.CharacterFacing
import com.example.toonstudio.model.CharacterType
import kotlin.math.sin

fun DrawScope.drawCartoonCharacter(
    type: CharacterType,
    expression: CharacterExpression,
    facing: CharacterFacing,
    isSpeaking: Boolean,
    animationTimeMs: Long,
    centerOffset: Offset,
    scale: Float = 1.0f
) {
    withTransform({
        translate(left = centerOffset.x, top = centerOffset.y)
        scale(
            scaleX = if (facing == CharacterFacing.FACING_LEFT) -scale else scale,
            scaleY = scale,
            pivot = Offset.Zero
        )
    }) {
        // Mouth animation offset based on speech and sine wave
        val mouthOpen = if (isSpeaking) {
            ((sin(animationTimeMs / 120.0) + 1.0) * 0.5).toFloat() * 14f
        } else {
            when (expression) {
                CharacterExpression.LAUGHING, CharacterExpression.SHOUTING -> 10f
                CharacterExpression.SURPRISED -> 12f
                else -> 2f
            }
        }

        // Slight breathing bounce
        val breathY = (sin(animationTimeMs / 400.0) * 2f).toFloat()

        when (type) {
            CharacterType.BOY -> drawBoyCharacter(expression, mouthOpen, breathY)
            CharacterType.GIRL -> drawGirlCharacter(expression, mouthOpen, breathY)
            CharacterType.HERO -> drawHeroCharacter(expression, mouthOpen, breathY)
            CharacterType.HEROINE -> drawHeroineCharacter(expression, mouthOpen, breathY)
            CharacterType.KING -> drawKingCharacter(expression, mouthOpen, breathY)
            CharacterType.MONSTER -> drawMonsterCharacter(expression, mouthOpen, breathY)
            CharacterType.THIEF -> drawThiefCharacter(expression, mouthOpen, breathY)
            CharacterType.POLICE -> drawPoliceCharacter(expression, mouthOpen, breathY)
            CharacterType.GRANDFATHER -> drawGrandfatherCharacter(expression, mouthOpen, breathY)
            CharacterType.FAIRY -> drawFairyCharacter(expression, mouthOpen, breathY, animationTimeMs)
            CharacterType.ROBOT -> drawRobotCharacter(expression, mouthOpen, breathY, animationTimeMs)
            CharacterType.FOX -> drawFoxCharacter(expression, mouthOpen, breathY)
            CharacterType.TIGER -> drawTigerCharacter(expression, mouthOpen, breathY)
        }
    }
}

// Common Skin Tones
private val SkinLight = Color(0xFFFFDBB5)
private val SkinFair = Color(0xFFFFCDA3)
private val SkinOlive = Color(0xFFE0AC69)
private val SkinMonster = Color(0xFF66BB6A)
private val DarkHair = Color(0xFF1E1E24)
private val BrownHair = Color(0xFF4E342E)
private val GoldenCrown = Color(0xFFFFD700)

private fun DrawScope.drawBoyCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Body / Red T-Shirt
    drawRoundRect(
        color = Color(0xFFFF3D00),
        topLeft = Offset(-25f, 25f + breathY),
        size = Size(50f, 65f),
        cornerRadius = CornerRadius(12f, 12f)
    )
    // Blue Shorts
    drawRoundRect(
        color = Color(0xFF1976D2),
        topLeft = Offset(-22f, 85f + breathY),
        size = Size(44f, 35f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    // Legs
    drawRect(SkinFair, topLeft = Offset(-16f, 120f + breathY), size = Size(10f, 30f))
    drawRect(SkinFair, topLeft = Offset(6f, 120f + breathY), size = Size(10f, 30f))
    // Shoes
    drawRoundRect(Color(0xFF212121), topLeft = Offset(-20f, 145f + breathY), size = Size(16f, 12f), cornerRadius = CornerRadius(4f, 4f))
    drawRoundRect(Color(0xFF212121), topLeft = Offset(4f, 145f + breathY), size = Size(16f, 12f), cornerRadius = CornerRadius(4f, 4f))

    // Head
    drawCircle(SkinFair, radius = 32f, center = Offset(0f, -10f + breathY))
    // Hair with playful tuft
    drawArc(
        color = DarkHair,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(-33f, -44f + breathY),
        size = Size(66f, 50f)
    )
    drawCircle(DarkHair, radius = 8f, center = Offset(14f, -40f + breathY))

    // Eyes & Expression
    drawEyes(Offset(-12f, -12f + breathY), Offset(12f, -12f + breathY), expr)
    // Smile / Mouth
    drawMouth(Offset(0f, 6f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawGirlCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Pink Frock Dress
    val frockPath = Path().apply {
        moveTo(-18f, 20f + breathY)
        lineTo(18f, 20f + breathY)
        lineTo(36f, 85f + breathY)
        lineTo(-36f, 85f + breathY)
        close()
    }
    drawPath(frockPath, color = Color(0xFFFF4081))
    // Ribbon Belt
    drawRect(Color(0xFFFFD54F), topLeft = Offset(-22f, 48f + breathY), size = Size(44f, 8f))

    // Legs
    drawRect(SkinLight, topLeft = Offset(-14f, 85f + breathY), size = Size(8f, 40f))
    drawRect(SkinLight, topLeft = Offset(6f, 85f + breathY), size = Size(8f, 40f))
    // Red Shoes
    drawCircle(Color(0xFFD81B60), radius = 8f, center = Offset(-10f, 128f + breathY))
    drawCircle(Color(0xFFD81B60), radius = 8f, center = Offset(10f, 128f + breathY))

    // Long Hair Behind
    drawRoundRect(BrownHair, topLeft = Offset(-36f, -30f + breathY), size = Size(72f, 75f), cornerRadius = CornerRadius(20f, 20f))
    // Head
    drawCircle(SkinLight, radius = 30f, center = Offset(0f, -10f + breathY))
    // Hair Bangs & Ribbons
    drawArc(BrownHair, startAngle = 180f, sweepAngle = 180f, useCenter = true, topLeft = Offset(-30f, -42f + breathY), size = Size(60f, 46f))
    drawCircle(Color(0xFFFFD54F), radius = 6f, center = Offset(-26f, -25f + breathY))
    drawCircle(Color(0xFFFFD54F), radius = 6f, center = Offset(26f, -25f + breathY))

    drawEyes(Offset(-11f, -12f + breathY), Offset(11f, -12f + breathY), expr, isEyelash = true)
    drawMouth(Offset(0f, 6f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawKingCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Royal Robe (Purple & Gold)
    drawRoundRect(
        color = Color(0xFF4A148C),
        topLeft = Offset(-35f, 25f + breathY),
        size = Size(70f, 105f),
        cornerRadius = CornerRadius(14f, 14f)
    )
    // Gold Sash
    drawRect(GoldenCrown, topLeft = Offset(-10f, 25f + breathY), size = Size(20f, 105f))

    // Head
    drawCircle(SkinOlive, radius = 35f, center = Offset(0f, -12f + breathY))

    // Crown
    val crownPath = Path().apply {
        moveTo(-30f, -38f + breathY)
        lineTo(-25f, -65f + breathY)
        lineTo(-10f, -48f + breathY)
        lineTo(0f, -70f + breathY)
        lineTo(10f, -48f + breathY)
        lineTo(25f, -65f + breathY)
        lineTo(30f, -38f + breathY)
        close()
    }
    drawPath(crownPath, color = GoldenCrown)
    drawCircle(Color(0xFFE53935), radius = 4f, center = Offset(0f, -52f + breathY))

    // Royal Moustache
    drawMoustache(Offset(0f, 2f + breathY))
    drawEyes(Offset(-13f, -15f + breathY), Offset(13f, -15f + breathY), expr)
    drawMouth(Offset(0f, 12f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawMonsterCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Massive Green Body
    drawRoundRect(
        color = Color(0xFF2E7D32),
        topLeft = Offset(-45f, 20f + breathY),
        size = Size(90f, 95f),
        cornerRadius = CornerRadius(18f, 18f)
    )

    // Horns
    val leftHorn = Path().apply {
        moveTo(-25f, -35f + breathY)
        lineTo(-45f, -70f + breathY)
        lineTo(-15f, -45f + breathY)
        close()
    }
    drawPath(leftHorn, color = Color(0xFFFFB300))
    val rightHorn = Path().apply {
        moveTo(25f, -35f + breathY)
        lineTo(45f, -70f + breathY)
        lineTo(15f, -45f + breathY)
        close()
    }
    drawPath(rightHorn, color = Color(0xFFFFB300))

    // Monster Head
    drawCircle(SkinMonster, radius = 40f, center = Offset(0f, -10f + breathY))

    // Glowing Red Eyes
    drawCircle(Color(0xFFFF1744), radius = 8f, center = Offset(-16f, -16f + breathY))
    drawCircle(Color(0xFFFF1744), radius = 8f, center = Offset(16f, -16f + breathY))
    drawCircle(Color.Black, radius = 4f, center = Offset(-16f, -16f + breathY))
    drawCircle(Color.Black, radius = 4f, center = Offset(16f, -16f + breathY))

    // Fangs & Wide Mouth
    val mouthY = 12f + breathY
    drawArc(
        color = Color(0xFF212121),
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(-25f, mouthY - 4f),
        size = Size(50f, 16f + mouthOpen)
    )
    // Two upward fangs
    val fang1 = Path().apply {
        moveTo(-14f, mouthY + mouthOpen)
        lineTo(-11f, mouthY - 4f)
        lineTo(-8f, mouthY + mouthOpen)
        close()
    }
    drawPath(fang1, color = Color.White)
    val fang2 = Path().apply {
        moveTo(8f, mouthY + mouthOpen)
        lineTo(11f, mouthY - 4f)
        lineTo(14f, mouthY + mouthOpen)
        close()
    }
    drawPath(fang2, color = Color.White)
}

private fun DrawScope.drawThiefCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Striped Shirt (Burglar)
    drawRoundRect(Color(0xFF424242), topLeft = Offset(-25f, 25f + breathY), size = Size(50f, 70f), cornerRadius = CornerRadius(10f, 10f))
    drawRect(Color.White, topLeft = Offset(-25f, 40f + breathY), size = Size(50f, 8f))
    drawRect(Color.White, topLeft = Offset(-25f, 60f + breathY), size = Size(50f, 8f))

    // Head
    drawCircle(SkinOlive, radius = 30f, center = Offset(0f, -10f + breathY))

    // Black Beanie Cap
    drawRoundRect(Color(0xFF212121), topLeft = Offset(-32f, -42f + breathY), size = Size(64f, 26f), cornerRadius = CornerRadius(12f, 12f))

    // Black Eye Mask
    drawRoundRect(Color.Black, topLeft = Offset(-26f, -18f + breathY), size = Size(52f, 16f), cornerRadius = CornerRadius(6f, 6f))

    // Sneaky eyes inside mask
    drawCircle(Color.White, radius = 5f, center = Offset(-12f, -10f + breathY))
    drawCircle(Color.White, radius = 5f, center = Offset(12f, -10f + breathY))
    drawCircle(Color.Black, radius = 3f, center = Offset(-10f, -10f + breathY))
    drawCircle(Color.Black, radius = 3f, center = Offset(14f, -10f + breathY))

    // Sneaky smile
    drawMouth(Offset(2f, 10f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawPoliceCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Khaki Uniform
    drawRoundRect(Color(0xFFBCAAA4), topLeft = Offset(-30f, 25f + breathY), size = Size(60f, 80f), cornerRadius = CornerRadius(10f, 10f))
    // Police Belt
    drawRect(Color(0xFF3E2723), topLeft = Offset(-30f, 75f + breathY), size = Size(60f, 10f))

    // Head
    drawCircle(SkinFair, radius = 32f, center = Offset(0f, -10f + breathY))

    // Police Cap with golden badge
    drawRoundRect(Color(0xFF3E2723), topLeft = Offset(-34f, -40f + breathY), size = Size(68f, 24f), cornerRadius = CornerRadius(8f, 8f))
    drawCircle(GoldenCrown, radius = 5f, center = Offset(0f, -30f + breathY))

    drawMoustache(Offset(0f, 4f + breathY))
    drawEyes(Offset(-12f, -12f + breathY), Offset(12f, -12f + breathY), expr)
    drawMouth(Offset(0f, 12f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawHeroCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Stylish Navy Jacket
    drawRoundRect(Color(0xFF1565C0), topLeft = Offset(-28f, 25f + breathY), size = Size(56f, 80f), cornerRadius = CornerRadius(12f, 12f))
    drawRect(Color(0xFFFFC107), topLeft = Offset(-6f, 25f + breathY), size = Size(12f, 80f))

    // Head
    drawCircle(SkinFair, radius = 32f, center = Offset(0f, -10f + breathY))

    // Spiky Heroic Hair
    val hair = Path().apply {
        moveTo(-32f, -15f + breathY)
        lineTo(-38f, -45f + breathY)
        lineTo(-20f, -38f + breathY)
        lineTo(-10f, -55f + breathY)
        lineTo(5f, -42f + breathY)
        lineTo(25f, -50f + breathY)
        lineTo(32f, -15f + breathY)
        close()
    }
    drawPath(hair, color = DarkHair)

    drawEyes(Offset(-12f, -12f + breathY), Offset(12f, -12f + breathY), expr)
    drawMouth(Offset(0f, 8f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawHeroineCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Beautiful Teal Kurti
    drawRoundRect(Color(0xFF00897B), topLeft = Offset(-24f, 22f + breathY), size = Size(48f, 85f), cornerRadius = CornerRadius(12f, 12f))

    // Long Cascading Hair Behind
    drawRoundRect(DarkHair, topLeft = Offset(-34f, -25f + breathY), size = Size(68f, 80f), cornerRadius = CornerRadius(16f, 16f))

    // Head
    drawCircle(SkinLight, radius = 30f, center = Offset(0f, -10f + breathY))

    drawEyes(Offset(-11f, -12f + breathY), Offset(11f, -12f + breathY), expr, isEyelash = true)
    // Small red bindi
    drawCircle(Color(0xFFD50000), radius = 2.5f, center = Offset(0f, -20f + breathY))
    drawMouth(Offset(0f, 6f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawGrandfatherCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Traditional White Kurta & Shawl
    drawRoundRect(Color(0xFFECEFF1), topLeft = Offset(-30f, 25f + breathY), size = Size(60f, 85f), cornerRadius = CornerRadius(12f, 12f))
    // Orange Shawl
    drawRect(Color(0xFFFF9800), topLeft = Offset(-26f, 25f + breathY), size = Size(16f, 75f))

    // Head
    drawCircle(SkinOlive, radius = 32f, center = Offset(0f, -10f + breathY))

    // Fluffy White Hair Sides
    drawCircle(Color(0xFFCFD8DC), radius = 12f, center = Offset(-30f, -15f + breathY))
    drawCircle(Color(0xFFCFD8DC), radius = 12f, center = Offset(30f, -15f + breathY))

    // Round Spectacles
    drawCircle(Color.Black, radius = 9f, center = Offset(-12f, -12f + breathY), style = Stroke(2.5f))
    drawCircle(Color.Black, radius = 9f, center = Offset(12f, -12f + breathY), style = Stroke(2.5f))
    drawLine(Color.Black, start = Offset(-3f, -12f + breathY), end = Offset(3f, -12f + breathY), strokeWidth = 2f)

    // Flowing White Beard
    val beard = Path().apply {
        moveTo(-24f, 4f + breathY)
        lineTo(-18f, 32f + breathY)
        lineTo(0f, 40f + breathY)
        lineTo(18f, 32f + breathY)
        lineTo(24f, 4f + breathY)
        close()
    }
    drawPath(beard, color = Color(0xFFEEEEEE))

    drawMouth(Offset(0f, 10f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawFairyCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float, timeMs: Long) {
    // Ethereal Fluttering Wings
    val wingFlap = (sin(timeMs / 100.0) * 10f).toFloat()
    val leftWing = Path().apply {
        moveTo(-10f, 10f + breathY)
        cubicTo(-50f + wingFlap, -30f, -60f, 40f, -10f, 50f + breathY)
        close()
    }
    drawPath(leftWing, color = Color(0x9980DEEA))
    val rightWing = Path().apply {
        moveTo(10f, 10f + breathY)
        cubicTo(50f - wingFlap, -30f, 60f, 40f, 10f, 50f + breathY)
        close()
    }
    drawPath(rightWing, color = Color(0x9980DEEA))

    // Light Purple Dress
    drawRoundRect(Color(0xFFBA68C8), topLeft = Offset(-20f, 22f + breathY), size = Size(40f, 75f), cornerRadius = CornerRadius(12f, 12f))

    // Head
    drawCircle(SkinLight, radius = 28f, center = Offset(0f, -10f + breathY))

    // Glowing Tiara
    drawArc(GoldenCrown, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(-18f, -36f + breathY), size = Size(36f, 20f), style = Stroke(4f))

    drawEyes(Offset(-10f, -12f + breathY), Offset(10f, -12f + breathY), expr, isEyelash = true)
    drawMouth(Offset(0f, 6f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawRobotCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float, timeMs: Long) {
    // Cyan Metallic Box Body
    drawRoundRect(Color(0xFF37474F), topLeft = Offset(-30f, 25f + breathY), size = Size(60f, 75f), cornerRadius = CornerRadius(8f, 8f))
    // Chest Display Screen
    drawRoundRect(Color(0xFF00E5FF), topLeft = Offset(-18f, 40f + breathY), size = Size(36f, 25f), cornerRadius = CornerRadius(4f, 4f))

    // Robot Antenna
    drawLine(Color(0xFF78909C), start = Offset(0f, -30f + breathY), end = Offset(0f, -50f + breathY), strokeWidth = 4f)
    val ledColor = if ((timeMs / 300) % 2L == 0L) Color.Red else Color.Yellow
    drawCircle(ledColor, radius = 6f, center = Offset(0f, -52f + breathY))

    // Square Head
    drawRoundRect(Color(0xFF455A64), topLeft = Offset(-32f, -30f + breathY), size = Size(64f, 50f), cornerRadius = CornerRadius(10f, 10f))

    // Visor Eyes
    drawRoundRect(Color(0xFF00E5FF), topLeft = Offset(-24f, -18f + breathY), size = Size(48f, 12f), cornerRadius = CornerRadius(4f, 4f))

    // Digital LED mouth
    val mouthWidth = 24f + mouthOpen
    drawRoundRect(Color(0xFFFF5252), topLeft = Offset(-mouthWidth / 2f, 4f + breathY), size = Size(mouthWidth, 6f + mouthOpen * 0.5f), cornerRadius = CornerRadius(2f, 2f))
}

private fun DrawScope.drawFoxCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Orange Body
    drawRoundRect(Color(0xFFE65100), topLeft = Offset(-26f, 25f + breathY), size = Size(52f, 70f), cornerRadius = CornerRadius(12f, 12f))
    // Bushy Tail
    val tail = Path().apply {
        moveTo(25f, 65f + breathY)
        cubicTo(60f, 50f, 65f, 15f, 40f, 10f + breathY)
        close()
    }
    drawPath(tail, color = Color(0xFFE65100))

    // Head & Pointy Ears
    val leftEar = Path().apply {
        moveTo(-24f, -25f + breathY)
        lineTo(-34f, -55f + breathY)
        lineTo(-10f, -35f + breathY)
        close()
    }
    drawPath(leftEar, color = Color(0xFFBF360C))
    val rightEar = Path().apply {
        moveTo(24f, -25f + breathY)
        lineTo(34f, -55f + breathY)
        lineTo(10f, -35f + breathY)
        close()
    }
    drawPath(rightEar, color = Color(0xFFBF360C))

    drawCircle(Color(0xFFE65100), radius = 30f, center = Offset(0f, -10f + breathY))
    // White Muzzle
    drawCircle(Color.White, radius = 14f, center = Offset(0f, 4f + breathY))
    drawCircle(Color.Black, radius = 4f, center = Offset(0f, 0f + breathY))

    drawEyes(Offset(-12f, -12f + breathY), Offset(12f, -12f + breathY), expr)
    drawMouth(Offset(0f, 10f + breathY), mouthOpen, expr)
}

private fun DrawScope.drawTigerCharacter(expr: CharacterExpression, mouthOpen: Float, breathY: Float) {
    // Orange Striped Body
    drawRoundRect(Color(0xFFF57C00), topLeft = Offset(-35f, 25f + breathY), size = Size(70f, 85f), cornerRadius = CornerRadius(14f, 14f))
    // Black Stripes
    drawLine(Color.Black, start = Offset(-30f, 45f + breathY), end = Offset(-15f, 45f + breathY), strokeWidth = 4f)
    drawLine(Color.Black, start = Offset(30f, 45f + breathY), end = Offset(15f, 45f + breathY), strokeWidth = 4f)
    drawLine(Color.Black, start = Offset(-30f, 65f + breathY), end = Offset(-15f, 65f + breathY), strokeWidth = 4f)
    drawLine(Color.Black, start = Offset(30f, 65f + breathY), end = Offset(15f, 65f + breathY), strokeWidth = 4f)

    // Tiger Head
    drawCircle(Color(0xFFF57C00), radius = 35f, center = Offset(0f, -12f + breathY))
    // Round Ears
    drawCircle(Color(0xFFE65100), radius = 12f, center = Offset(-28f, -38f + breathY))
    drawCircle(Color(0xFFE65100), radius = 12f, center = Offset(28f, -38f + breathY))

    // Whiskers
    drawLine(Color.Black, start = Offset(-20f, 4f + breathY), end = Offset(-45f, 2f + breathY), strokeWidth = 2f)
    drawLine(Color.Black, start = Offset(20f, 4f + breathY), end = Offset(45f, 2f + breathY), strokeWidth = 2f)

    drawEyes(Offset(-14f, -15f + breathY), Offset(14f, -15f + breathY), expr)
    drawMouth(Offset(0f, 10f + breathY), mouthOpen, expr)
}

// Helpers
private fun DrawScope.drawEyes(leftEye: Offset, rightEye: Offset, expr: CharacterExpression, isEyelash: Boolean = false) {
    when (expr) {
        CharacterExpression.HAPPY, CharacterExpression.LAUGHING -> {
            // Cute curved happy eyes
            drawArc(Color.Black, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(leftEye.x - 7f, leftEye.y - 4f), size = Size(14f, 10f), style = Stroke(3f, cap = StrokeCap.Round))
            drawArc(Color.Black, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(rightEye.x - 7f, rightEye.y - 4f), size = Size(14f, 10f), style = Stroke(3f, cap = StrokeCap.Round))
        }
        CharacterExpression.ANGRY -> {
            drawCircle(Color.White, radius = 6f, center = leftEye)
            drawCircle(Color.White, radius = 6f, center = rightEye)
            drawCircle(Color.Black, radius = 3f, center = leftEye)
            drawCircle(Color.Black, radius = 3f, center = rightEye)
            // Slanted angry brows
            drawLine(Color.Black, start = Offset(leftEye.x - 8f, leftEye.y - 10f), end = Offset(leftEye.x + 8f, leftEye.y - 4f), strokeWidth = 3f)
            drawLine(Color.Black, start = Offset(rightEye.x + 8f, rightEye.y - 10f), end = Offset(rightEye.x - 8f, rightEye.y - 4f), strokeWidth = 3f)
        }
        else -> {
            // Normal wide expressive cartoon eyes
            drawCircle(Color.White, radius = 7f, center = leftEye)
            drawCircle(Color.White, radius = 7f, center = rightEye)
            drawCircle(Color.Black, radius = 3.5f, center = Offset(leftEye.x + 1f, leftEye.y))
            drawCircle(Color.Black, radius = 3.5f, center = Offset(rightEye.x + 1f, rightEye.y))
            // Catchlights
            drawCircle(Color.White, radius = 1.5f, center = Offset(leftEye.x, leftEye.y - 2f))
            drawCircle(Color.White, radius = 1.5f, center = Offset(rightEye.x, rightEye.y - 2f))
            if (isEyelash) {
                drawLine(Color.Black, start = Offset(leftEye.x - 7f, leftEye.y - 6f), end = Offset(leftEye.x - 12f, leftEye.y - 10f), strokeWidth = 2f)
                drawLine(Color.Black, start = Offset(rightEye.x + 7f, rightEye.y - 6f), end = Offset(rightEye.x + 12f, rightEye.y - 10f), strokeWidth = 2f)
            }
        }
    }
}

private fun DrawScope.drawMouth(center: Offset, mouthOpen: Float, expr: CharacterExpression) {
    val h = 4f + mouthOpen
    val w = 20f
    drawArc(
        color = Color(0xFFC2185B),
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(center.x - w / 2f, center.y - 2f),
        size = Size(w, h)
    )
}

private fun DrawScope.drawMoustache(center: Offset) {
    val left = Path().apply {
        moveTo(center.x, center.y)
        cubicTo(center.x - 12f, center.y - 4f, center.x - 22f, center.y + 4f, center.x - 24f, center.y + 12f)
        close()
    }
    drawPath(left, color = Color(0xFF212121))
    val right = Path().apply {
        moveTo(center.x, center.y)
        cubicTo(center.x + 12f, center.y - 4f, center.x + 22f, center.y + 4f, center.x + 24f, center.y + 12f)
        close()
    }
    drawPath(right, color = Color(0xFF212121))
}
