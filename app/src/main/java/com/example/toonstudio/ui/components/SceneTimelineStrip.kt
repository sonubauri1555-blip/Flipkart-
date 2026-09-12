package com.example.toonstudio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toonstudio.model.CartoonScene
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioBorderActive
import com.example.ui.theme.StudioCard
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ToonCyan
import com.example.ui.theme.ToonPink
import com.example.ui.theme.ToonYellow

@Composable
fun SceneTimelineStrip(
    scenes: List<CartoonScene>,
    selectedIndex: Int,
    onSelectScene: (Int) -> Unit,
    onAddScene: () -> Unit,
    onDeleteScene: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Movie,
                contentDescription = "Scenes",
                tint = ToonCyan,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "মুভি টাইমলাইন দৃশ্যসমূহ (${scenes.size} টি সিন)",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "মোট সময়: ${scenes.sumOf { it.durationSeconds }}s",
                color = ToonYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(scenes) { index, scene ->
                val isSelected = index == selectedIndex
                SceneCardItem(
                    scene = scene,
                    index = index + 1,
                    isSelected = isSelected,
                    onClick = { onSelectScene(index) },
                    onDelete = { onDeleteScene(scene.id) },
                    canDelete = scenes.size > 1
                )
            }

            item {
                AddSceneButton(onClick = onAddScene)
            }
        }
    }
}

@Composable
private fun SceneCardItem(
    scene: CartoonScene,
    index: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    canDelete: Boolean
) {
    val borderColor = if (isSelected) ToonCyan else StudioBorder
    val bgColor = if (isSelected) StudioCardElevated else StudioCard

    Surface(
        modifier = Modifier
            .width(135.dp)
            .height(96.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("scene_card_$index"),
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(if (isSelected) ToonCyan else Color(0xFF37474F), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$index",
                        color = if (isSelected) Color.Black else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${scene.durationSeconds}s",
                    color = ToonYellow,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = scene.title,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = scene.environment.titleBn,
                color = TextSecondary,
                fontSize = 9.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            val dialoguePreview = if (scene.dialogueText.isNotBlank()) scene.dialogueText else "ডায়ালগ লিখুন..."
            Text(
                text = dialoguePreview,
                color = TextMuted,
                fontSize = 9.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AddSceneButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(85.dp)
            .height(96.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, ToonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("add_scene_button"),
        color = StudioCard,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(ToonCyan.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Scene",
                    tint = ToonCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "+ নতুন সিন",
                color = ToonCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
