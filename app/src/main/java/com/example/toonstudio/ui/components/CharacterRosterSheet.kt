package com.example.toonstudio.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toonstudio.model.CharacterExpression
import com.example.toonstudio.model.CharacterFacing
import com.example.toonstudio.model.CharacterPosition
import com.example.toonstudio.model.CharacterType
import com.example.toonstudio.model.SceneActor
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterRosterDialog(
    currentActor: SceneActor?,
    slotPosition: CharacterPosition,
    onSaveActor: (SceneActor?) -> Unit,
    onTestVoice: (CharacterType) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedType by remember { mutableStateOf(currentActor?.characterType ?: CharacterType.BOY) }
    var selectedExpression by remember { mutableStateOf(currentActor?.expression ?: CharacterExpression.HAPPY) }
    var selectedFacing by remember { mutableStateOf(currentActor?.facing ?: if (slotPosition == CharacterPosition.RIGHT) CharacterFacing.FACING_LEFT else CharacterFacing.FACING_RIGHT) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = StudioSurface,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, StudioBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎭 ক্যারেক্টার সিলেক্টর (${slotPosition.name})",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "১. কার্টুন চরিত্র নির্বাচন করুন (সকল চরিত্র):",
                color = ToonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Character Grid
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CharacterType.entries.forEach { charType ->
                    val isSelected = charType == selectedType
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .border(if (isSelected) 1.5.dp else 1.dp, if (isSelected) ToonPink else StudioBorder, RoundedCornerShape(10.dp))
                            .clickable { selectedType = charType }
                            .testTag("char_select_${charType.name}"),
                        color = if (isSelected) StudioCardElevated else StudioCard
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = charType.defaultNameBn,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Test Voice button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StudioCard, RoundedCornerShape(10.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "গলার স্বর: ${selectedType.roleDescBn}",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "পিচ: ${selectedType.voicePitch}x | গতি: ${selectedType.voiceSpeed}x",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
                OutlinedButton(
                    onClick = { onTestVoice(selectedType) },
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Hear", tint = ToonYellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ভয়েস টেস্ট", color = ToonYellow, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "২. মুখের অভিব্যক্তি (Face Expression):",
                color = ToonCyan,
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
                    val isSelected = expr == selectedExpression
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .border(if (isSelected) 1.dp else 0.5.dp, if (isSelected) ToonYellow else StudioBorder, RoundedCornerShape(8.dp))
                            .clickable { selectedExpression = expr },
                        color = if (isSelected) Color(0xFF37474F) else StudioCard
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

            // Action buttons: Remove Actor OR Save
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (currentActor != null) {
                    OutlinedButton(
                        onClick = { onSaveActor(null) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("চরিত্র মুছুন", fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = {
                        val actor = SceneActor(
                            characterType = selectedType,
                            position = slotPosition,
                            facing = selectedFacing,
                            expression = selectedExpression
                        )
                        onSaveActor(actor)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ToonCyan, contentColor = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("সংরক্ষণ করুন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
