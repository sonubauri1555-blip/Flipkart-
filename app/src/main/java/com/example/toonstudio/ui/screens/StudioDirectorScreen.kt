package com.example.toonstudio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.toonstudio.model.ActionSoundEffect
import com.example.toonstudio.model.BgmStyle
import com.example.toonstudio.model.CameraMotion
import com.example.toonstudio.model.CartoonEnvironment
import com.example.toonstudio.model.CharacterPosition
import com.example.toonstudio.model.CharacterType
import com.example.toonstudio.model.SceneActor
import com.example.toonstudio.model.WeatherEffect
import com.example.toonstudio.ui.ToonStudioViewModel
import com.example.toonstudio.ui.components.CartoonCanvasViewer
import com.example.toonstudio.ui.components.CharacterRosterDialog
import com.example.toonstudio.ui.components.SceneTimelineStrip
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCard
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ToonCyan
import com.example.ui.theme.ToonOrange
import com.example.ui.theme.ToonPink
import com.example.ui.theme.ToonYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudioDirectorScreen(
    viewModel: ToonStudioViewModel,
    modifier: Modifier = Modifier
) {
    val project by viewModel.currentProject.collectAsState()
    val selectedIndex by viewModel.selectedSceneIndex.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val currentSpeaker by viewModel.currentSpeaker.collectAsState()
    val scrollState = rememberScrollState()

    val currentScene = viewModel.getSelectedScene() ?: return

    // Actor editing modal state
    var editingActorSlot by remember { mutableStateOf<CharacterPosition?>(null) }

    if (editingActorSlot != null) {
        val slot = editingActorSlot!!
        val currentActor = when (slot) {
            CharacterPosition.LEFT -> currentScene.leftActor
            CharacterPosition.CENTER -> currentScene.centerActor
            CharacterPosition.RIGHT -> currentScene.rightActor
            else -> null
        }

        Dialog(onDismissRequest = { editingActorSlot = null }) {
            CharacterRosterDialog(
                currentActor = currentActor,
                slotPosition = slot,
                onSaveActor = { actor ->
                    val updated = when (slot) {
                        CharacterPosition.LEFT -> currentScene.copy(leftActor = actor)
                        CharacterPosition.CENTER -> currentScene.copy(centerActor = actor)
                        CharacterPosition.RIGHT -> currentScene.copy(rightActor = actor)
                        else -> currentScene
                    }
                    viewModel.updateCurrentScene(updated)
                    editingActorSlot = null
                },
                onTestVoice = { type -> viewModel.testCharacterVoice(type) },
                onDismiss = { editingActorSlot = null }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        // 1. Live Cartoon Preview Canvas
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            CartoonCanvasViewer(
                scene = currentScene,
                isSpeaking = isSpeaking,
                currentSpeaker = currentSpeaker,
                playbackProgress = 0f
            )
        }

        // Action Toolbar below Canvas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.testCurrentSceneVoice() },
                colors = ButtonDefaults.buttonColors(containerColor = ToonCyan, contentColor = Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("preview_voice_button")
            ) {
                Icon(
                    imageVector = Icons.Default.RecordVoiceOver,
                    contentDescription = "Voice",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("ভয়েস টেস্ট ও প্রিভিউ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.startMoviePlayback(selectedIndex) },
                colors = ButtonDefaults.buttonColors(containerColor = ToonPink, contentColor = Color.White),
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("play_from_here_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("এখান থেকে চালান", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Scene Timeline Strip
        SceneTimelineStrip(
            scenes = project.scenes,
            selectedIndex = selectedIndex,
            onSelectScene = { viewModel.selectScene(it) },
            onAddScene = { viewModel.addNewScene() },
            onDeleteScene = { viewModel.deleteScene(it) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Scene Script & Dialogue Editor Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Script", tint = ToonCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "স্ক্রিপ্ট ও সংলাপ রচয়িতা (Scene Script)",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scene Title
                OutlinedTextField(
                    value = currentScene.title,
                    onValueChange = { viewModel.updateCurrentScene(currentScene.copy(title = it)) },
                    label = { Text("দৃশ্যের নাম (Scene Title)", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = StudioCard,
                        unfocusedContainerColor = StudioCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedIndicatorColor = ToonCyan
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Dialogue Text (Main Script line)
                OutlinedTextField(
                    value = currentScene.dialogueText,
                    onValueChange = { viewModel.updateCurrentScene(currentScene.copy(dialogueText = it)) },
                    label = { Text("চরিত্রের মুখে কী বলবে? (Character Dialogue)", fontSize = 12.sp) },
                    placeholder = { Text("যেমন: শুনলাম রাজ্যে চোর এসেছে! এবার কী হবে?", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = StudioCard,
                        unfocusedContainerColor = StudioCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedIndicatorColor = ToonPink
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Narration / Voiceover
                OutlinedTextField(
                    value = currentScene.narrationText,
                    onValueChange = { viewModel.updateCurrentScene(currentScene.copy(narrationText = it)) },
                    label = { Text("পটভূমি বা গল্পের বর্ণনা (Narrator Storyline)", fontSize = 12.sp) },
                    placeholder = { Text("যেমন: কৃষ্ণনগরের রাজদরবারে গম্ভীর পরিস্থিতি তৈরি হলো...", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 1,
                    maxLines = 3,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = StudioCard,
                        unfocusedContainerColor = StudioCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedIndicatorColor = ToonYellow
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Speaker Selector
                Text(
                    text = "কথা কে বলছে? (Active Speaker):",
                    color = ToonYellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val speakerOptions = listOf(
                        CharacterPosition.LEFT to (currentScene.leftActor?.characterType?.defaultNameBn ?: "বাম চরিত্র"),
                        CharacterPosition.CENTER to (currentScene.centerActor?.characterType?.defaultNameBn ?: "মধ্য চরিত্র"),
                        CharacterPosition.RIGHT to (currentScene.rightActor?.characterType?.defaultNameBn ?: "ডান চরিত্র"),
                        null to "বর্ণনাকারী (Narrator)"
                    )

                    speakerOptions.forEach { (pos, label) ->
                        val isSelected = currentScene.speakerPosition == pos
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isSelected) ToonPink else StudioBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateCurrentScene(currentScene.copy(speakerPosition = pos)) },
                            color = if (isSelected) Color(0xFF880E4F) else StudioCard
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Duration Slider
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "দৃশ্যটির স্থায়িত্ব (Scene Duration):",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${currentScene.durationSeconds} সেকেন্ড",
                        color = ToonYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = currentScene.durationSeconds.toFloat(),
                    onValueChange = { viewModel.updateCurrentScene(currentScene.copy(durationSeconds = it.toInt())) },
                    valueRange = 3f..30f,
                    steps = 26,
                    colors = SliderDefaults.colors(
                        thumbColor = ToonYellow,
                        activeTrackColor = ToonCyan,
                        inactiveTrackColor = StudioBorder
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Actors & Characters in Scene
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Actors", tint = ToonPink, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "মঞ্চের কার্টুন ক্যারেক্টারসমূহ (Scene Actors)",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActorSlotCard(
                        title = "বাম চরিত্র (Left)",
                        actor = currentScene.leftActor,
                        onClick = { editingActorSlot = CharacterPosition.LEFT },
                        modifier = Modifier.weight(1f)
                    )
                    ActorSlotCard(
                        title = "মধ্য চরিত্র (Center)",
                        actor = currentScene.centerActor,
                        onClick = { editingActorSlot = CharacterPosition.CENTER },
                        modifier = Modifier.weight(1f)
                    )
                    ActorSlotCard(
                        title = "ডান চরিত্র (Right)",
                        actor = currentScene.rightActor,
                        onClick = { editingActorSlot = CharacterPosition.RIGHT },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Environment & Cinematic Atmosphere
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Palette, contentDescription = "Env", tint = ToonYellow, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "পরিবেশ, আবহাওয়া ও সাউন্ড ইফেক্ট",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Environment selector
                Text("১. কার্টুন ব্যাকগ্রাউন্ড ও দৃশ্যপট:", color = ToonCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CartoonEnvironment.entries.forEach { env ->
                        val isSelected = env == currentScene.environment
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isSelected) ToonCyan else StudioBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateCurrentScene(currentScene.copy(environment = env)) },
                            color = if (isSelected) Color(0xFF004D40) else StudioCard
                        ) {
                            Text(
                                text = env.titleBn,
                                color = if (isSelected) ToonCyan else TextSecondary,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Weather effect
                Text("২. আবহাওয়া ও আলো (Weather Overlay):", color = ToonCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WeatherEffect.entries.forEach { wth ->
                        val isSelected = wth == currentScene.weather
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isSelected) ToonYellow else StudioBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateCurrentScene(currentScene.copy(weather = wth)) },
                            color = if (isSelected) Color(0xFF4A3B00) else StudioCard
                        ) {
                            Text(
                                text = wth.labelBn,
                                color = if (isSelected) ToonYellow else TextSecondary,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Camera motion
                Text("৩. ক্যামেরা মুভমেন্ট (Cinematic Camera):", color = ToonCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CameraMotion.entries.forEach { cam ->
                        val isSelected = cam == currentScene.camera
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isSelected) ToonPink else StudioBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateCurrentScene(currentScene.copy(camera = cam)) },
                            color = if (isSelected) Color(0xFF880E4F) else StudioCard
                        ) {
                            Text(
                                text = cam.labelBn,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sound Effect (SFX)
                Text("৪. অ্যাকশন সাউন্ড এফেক্ট (SFX Hit):", color = ToonCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ActionSoundEffect.entries.forEach { sfx ->
                        val isSelected = sfx == currentScene.sfx
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isSelected) Color(0xFFFF5252) else StudioBorder, RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.updateCurrentScene(currentScene.copy(sfx = sfx))
                                    viewModel.sfxEngine.playSfx(sfx)
                                },
                            color = if (isSelected) Color(0xFFB71C1C) else StudioCard
                        ) {
                            Text(
                                text = sfx.titleBn,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActorSlotCard(
    title: String,
    actor: SceneActor?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(95.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, if (actor != null) ToonCyan else StudioBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (actor != null) StudioCardElevated else StudioCard
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = TextMuted,
                fontSize = 9.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (actor != null) {
                Text(
                    text = actor.characterType.defaultNameBn,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "ভাব: ${actor.expression.labelBn}",
                    color = ToonYellow,
                    fontSize = 9.sp
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF37474F), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = ToonCyan, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "চরিত্র যোগ করুন",
                    color = ToonCyan,
                    fontSize = 9.sp
                )
            }
        }
    }
}
