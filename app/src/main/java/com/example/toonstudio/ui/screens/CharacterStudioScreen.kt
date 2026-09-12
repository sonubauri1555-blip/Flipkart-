package com.example.toonstudio.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toonstudio.model.CharacterExpression
import com.example.toonstudio.model.CharacterFacing
import com.example.toonstudio.model.CharacterType
import com.example.toonstudio.ui.ToonStudioViewModel
import com.example.toonstudio.ui.components.drawCartoonCharacter
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
fun CharacterStudioScreen(
    viewModel: ToonStudioViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCharacter by remember { mutableStateOf(CharacterType.BOY) }
    var currentExpression by remember { mutableStateOf(CharacterExpression.HAPPY) }
    var customDialogue by remember { mutableStateOf("আমি কার্টুন স্টুডিওতে কথা বলছি!") }

    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "char_studio_anim")
    val animTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100000f,
        animationSpec = infiniteRepeatable(
            animation = tween(100000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "char_anim"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 70.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Face, contentDescription = "Face", tint = ToonCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "সকল কার্টুন চরিত্র গ্যালারি (Character Studio)",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "মানুষ, শিশু, রাজা, দৈত্য, পশু ও অন্যান্য সকল চরিত্র",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal Carousel of All Characters
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(CharacterType.entries) { charType ->
                val isSelected = charType == selectedCharacter
                Surface(
                    modifier = Modifier
                        .width(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, if (isSelected) ToonPink else StudioBorder, RoundedCornerShape(12.dp))
                        .clickable {
                            selectedCharacter = charType
                            customDialogue = when (charType) {
                                CharacterType.BOY -> "চলো বাবলু আর টুনি মিলে একটা মজার গল্প বানাই!"
                                CharacterType.GIRL -> "আমার নাম টুনি! ফুল আর প্রজাপতি আমার খুব প্রিয়!"
                                CharacterType.KING -> "মহারাজের হুকুম! রাজ্যের সকলে আনন্দ করো!"
                                CharacterType.MONSTER -> "হা হা হা! আমি এই জঙ্গলের সবথেকে শক্তিশালী দৈত্য!"
                                CharacterType.THIEF -> "আজ রাতে রাজবাড়ির সোনার গয়না চুরি করতেই হবে!"
                                CharacterType.POLICE -> "থামো চোর! আইনের হাত থেকে রেহাই নেই!"
                                CharacterType.GRANDFATHER -> "দাদুভাই, সততাই মানুষের জীবনের আসল সম্পদ।"
                                CharacterType.FAIRY -> "জাদুর ডানায় ভর করে মেঘের দেশে উড়ে চলো!"
                                CharacterType.ROBOT -> "রোবট Z-9 সঙ্কেত গ্রহণ করেছে! কাজ শুরু করছি!"
                                CharacterType.HERO -> "অন্যায় দেখলে চুপ করে থাকব না! লড়াই করব!"
                                CharacterType.HEROINE -> "বুদ্ধিমান মানুষ কখনো বিপদে ভেঙে পড়ে না!"
                                CharacterType.FOX -> "চালাক শিয়ালের বুদ্ধির কাছে সবাই কাবু!"
                                CharacterType.TIGER -> "হালুম! আমি সুন্দরবনের রয়েল বেঙ্গল টাইগার!"
                            }
                        }
                        .testTag("char_card_${charType.name}"),
                    color = if (isSelected) StudioCardElevated else StudioCard
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = charType.defaultNameBn,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = charType.roleDescBn,
                            color = if (isSelected) ToonYellow else TextMuted,
                            fontSize = 9.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big Character Stage Viewer
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎭 ${selectedCharacter.defaultNameBn}",
                    color = ToonCyan,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = selectedCharacter.roleDescBn,
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Canvas showing the character with breathing/talking animation
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF131A2A))
                        .border(1.5.dp, StudioBorder, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCartoonCharacter(
                            type = selectedCharacter,
                            expression = currentExpression,
                            facing = CharacterFacing.FACING_RIGHT,
                            isSpeaking = isSpeaking,
                            animationTimeMs = animTime.toLong(),
                            centerOffset = Offset(size.width / 2f, size.height * 0.45f),
                            scale = 1.35f
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Expression selection
                Text(
                    text = "অভিব্যক্তি পরিবর্তন করুন:",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CharacterExpression.entries.forEach { expr ->
                        val isSelected = expr == currentExpression
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, if (isSelected) ToonYellow else StudioBorder, RoundedCornerShape(8.dp))
                                .clickable { currentExpression = expr },
                            color = if (isSelected) Color(0xFF4A3B00) else StudioCard
                        ) {
                            Text(
                                text = expr.labelBn,
                                color = if (isSelected) ToonYellow else TextSecondary,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Live custom speech test
                OutlinedTextField(
                    value = customDialogue,
                    onValueChange = { customDialogue = it },
                    label = { Text("চরিত্রকে দিয়ে কিছু বলান (Type to Speak)", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = StudioCard,
                        unfocusedContainerColor = StudioCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedIndicatorColor = ToonPink
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.speechEngine.speak(customDialogue, selectedCharacter)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ToonPink, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("speak_custom_dialogue_button")
                ) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Speak", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("মুখে কথা বলান (Speak Now)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
