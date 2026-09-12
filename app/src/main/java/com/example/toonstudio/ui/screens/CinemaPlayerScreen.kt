package com.example.toonstudio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toonstudio.model.CartoonScene
import com.example.toonstudio.ui.ToonStudioViewModel
import com.example.toonstudio.ui.components.CartoonCanvasViewer
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCard
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ToonCyan
import com.example.ui.theme.ToonPink
import com.example.ui.theme.ToonYellow

@Composable
fun CinemaPlayerScreen(
    viewModel: ToonStudioViewModel,
    modifier: Modifier = Modifier
) {
    val project by viewModel.currentProject.collectAsState()
    val isPlaying by viewModel.isPlayingMovie.collectAsState()
    val currentSceneIndex by viewModel.movieCurrentSceneIndex.collectAsState()
    val sceneProgress by viewModel.moviePlaybackProgress.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val currentSpeaker by viewModel.currentSpeaker.collectAsState()

    val scenes = project.scenes
    val activeScene = if (currentSceneIndex in scenes.indices) scenes[currentSceneIndex] else scenes.firstOrNull() ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Movie Header Ribbon
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.TheaterComedy, contentDescription = "Cinema", tint = ToonPink, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "পূর্ণাঙ্গ সিনেমা মোড • মোট সময়: ${project.formattedDuration} (${scenes.size} টি দৃশ্য)",
                    color = ToonYellow,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Big Cinematic Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, if (isPlaying) ToonPink else StudioBorder, RoundedCornerShape(16.dp))
        ) {
            CartoonCanvasViewer(
                scene = activeScene,
                isSpeaking = isSpeaking,
                currentSpeaker = currentSpeaker,
                playbackProgress = sceneProgress
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress Details
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "দৃশ্য ${currentSceneIndex + 1} / ${scenes.size}",
                color = ToonCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${activeScene.durationSeconds} সেকেণ্ডের দৃশ্য",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { sceneProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = ToonPink,
            trackColor = StudioCard
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Main Playback Controls
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Restart / Loop
                IconButton(onClick = { viewModel.startMoviePlayback(0) }) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = "Restart", tint = TextSecondary, modifier = Modifier.size(24.dp))
                }

                // Previous Scene
                IconButton(
                    onClick = { viewModel.prevMovieScene() },
                    enabled = currentSceneIndex > 0
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Prev",
                        tint = if (currentSceneIndex > 0) Color.White else TextMuted,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Play / Pause Master Button
                Surface(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .clickable { viewModel.toggleMoviePlayback() }
                        .testTag("cinema_play_pause_button"),
                    color = if (isPlaying) ToonPink else ToonCyan,
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = if (isPlaying) Color.White else Color.Black,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                // Next Scene
                IconButton(
                    onClick = { viewModel.nextMovieScene() },
                    enabled = currentSceneIndex < scenes.size - 1
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = if (currentSceneIndex < scenes.size - 1) Color.White else TextMuted,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Fast forward scene
                IconButton(onClick = {
                    if (currentSceneIndex < scenes.size - 1) {
                        viewModel.nextMovieScene()
                    } else {
                        viewModel.startMoviePlayback(0)
                    }
                }) {
                    Icon(imageVector = Icons.Default.FastForward, contentDescription = "Next", tint = TextSecondary, modifier = Modifier.size(24.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scene Selector Strip
        Text(
            text = "সরাসরি যেকোনো দৃশ্যে যান (Jump to Scene):",
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(scenes) { idx, sc ->
                val isCurrent = idx == currentSceneIndex
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, if (isCurrent) ToonPink else StudioBorder, RoundedCornerShape(8.dp))
                        .clickable { viewModel.startMoviePlayback(idx) },
                    color = if (isCurrent) StudioCardElevated else StudioCard
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "সিন ${idx + 1}",
                            color = if (isCurrent) ToonPink else TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = sc.environment.category,
                            color = TextMuted,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }
    }
}
