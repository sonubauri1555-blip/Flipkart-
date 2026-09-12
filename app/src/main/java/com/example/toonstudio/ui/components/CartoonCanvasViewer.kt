package com.example.toonstudio.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toonstudio.model.CameraMotion
import com.example.toonstudio.model.CartoonScene
import com.example.toonstudio.model.CharacterPosition
import com.example.toonstudio.model.CharacterType
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.ToonCyan
import com.example.ui.theme.ToonPink
import com.example.ui.theme.ToonYellow
import kotlin.math.sin

@Composable
fun CartoonCanvasViewer(
    scene: CartoonScene,
    isSpeaking: Boolean,
    currentSpeaker: CharacterType?,
    playbackProgress: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "canvas_anim")
    val animTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100000f,
        animationSpec = infiniteRepeatable(
            animation = tween(100000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time_flow"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9.5f)
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, StudioBorder, RoundedCornerShape(16.dp))
            .background(Color.Black)
            .testTag("cartoon_canvas_viewer")
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val timeMs = animTime.toLong()
            val w = size.width
            val h = size.height

            // Calculate Camera Transform
            val (camScale, camTranslateX, camTranslateY) = when (scene.camera) {
                CameraMotion.ZOOM_IN -> {
                    // Zoom slightly towards center or speaker
                    val zoomPivotX = when (scene.speakerPosition) {
                        CharacterPosition.LEFT -> w * 0.22f
                        CharacterPosition.RIGHT -> w * 0.78f
                        else -> w * 0.5f
                    }
                    val targetZoom = 1.15f
                    Triple(targetZoom, (w * 0.5f - zoomPivotX) * 0.15f, 0f)
                }
                CameraMotion.PAN_LEFT_TO_RIGHT -> {
                    val panFraction = (timeMs % 8000L) / 8000f
                    Triple(1.08f, (panFraction - 0.5f) * -60f, 0f)
                }
                CameraMotion.PAN_RIGHT_TO_LEFT -> {
                    val panFraction = (timeMs % 8000L) / 8000f
                    Triple(1.08f, (0.5f - panFraction) * -60f, 0f)
                }
                CameraMotion.SHAKE -> {
                    val shakeX = (sin(timeMs / 40.0) * 6f).toFloat()
                    val shakeY = (sin(timeMs / 30.0) * 4f).toFloat()
                    Triple(1.0f, shakeX, shakeY)
                }
                CameraMotion.STATIC -> Triple(1.0f, 0f, 0f)
            }

            withTransform({
                translate(left = camTranslateX, top = camTranslateY)
                scale(camScale, camScale, pivot = Offset(w / 2f, h / 2f))
            }) {
                // 1. Draw Background Environment
                drawCartoonEnvironment(scene.environment, timeMs)

                // 2. Draw Characters
                val groundY = h * 0.70f

                // Left Actor
                scene.leftActor?.let { actor ->
                    val actorIsSpeaking = isSpeaking && (scene.speakerPosition == CharacterPosition.LEFT || currentSpeaker == actor.characterType)
                    drawCartoonCharacter(
                        type = actor.characterType,
                        expression = actor.expression,
                        facing = actor.facing,
                        isSpeaking = actorIsSpeaking,
                        animationTimeMs = timeMs,
                        centerOffset = Offset(w * CharacterPosition.LEFT.fractionX, groundY),
                        scale = 1.0f
                    )
                }

                // Center Actor
                scene.centerActor?.let { actor ->
                    val actorIsSpeaking = isSpeaking && (scene.speakerPosition == CharacterPosition.CENTER || currentSpeaker == actor.characterType)
                    drawCartoonCharacter(
                        type = actor.characterType,
                        expression = actor.expression,
                        facing = actor.facing,
                        isSpeaking = actorIsSpeaking,
                        animationTimeMs = timeMs,
                        centerOffset = Offset(w * CharacterPosition.CENTER.fractionX, groundY),
                        scale = 1.05f
                    )
                }

                // Right Actor
                scene.rightActor?.let { actor ->
                    val actorIsSpeaking = isSpeaking && (scene.speakerPosition == CharacterPosition.RIGHT || currentSpeaker == actor.characterType)
                    drawCartoonCharacter(
                        type = actor.characterType,
                        expression = actor.expression,
                        facing = actor.facing,
                        isSpeaking = actorIsSpeaking,
                        animationTimeMs = timeMs,
                        centerOffset = Offset(w * CharacterPosition.RIGHT.fractionX, groundY),
                        scale = 1.0f
                    )
                }

                // 3. Draw Weather Effects over actors
                drawWeatherEffect(scene.weather, timeMs)
            }
        }

        // Overlay 1: Top Bar with Scene Title & Duration Progress
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xCC0D1117),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = scene.title,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${scene.durationSeconds}s",
                        color = ToonYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Speaking Indicator
            if (isSpeaking) {
                Surface(
                    color = ToonPink.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Voice",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ভয়েস চলছে...",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Overlay 2: Dynamic Cartoon Speech Bubble
        if (scene.dialogueText.isNotBlank()) {
            val bubbleAlignment = when (scene.speakerPosition) {
                CharacterPosition.LEFT -> Alignment.TopStart
                CharacterPosition.RIGHT -> Alignment.TopEnd
                else -> Alignment.TopCenter
            }

            val speakerName = scene.activeSpeakerType?.defaultNameBn ?: "চরিত্র"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 45.dp),
                contentAlignment = bubbleAlignment
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    color = Color(0xFFFFFDE7), // Classic comic paper yellow
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF212121))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "💬 $speakerName",
                                color = Color(0xFFC2185B),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = scene.dialogueText,
                            color = Color(0xFF1B1B1B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 17.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Overlay 3: Cinematic Narrator Ribbon (Bottom)
        if (scene.narrationText.isNotBlank()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = Color(0xD9090D16)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "📜 বর্ণনা: ${scene.narrationText}",
                        color = Color(0xFFE0E0E0),
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Playback progress strip
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(Color(0xFF263238), RoundedCornerShape(2.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(playbackProgress.coerceIn(0f, 1f))
                                .height(3.dp)
                                .background(ToonCyan, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}
