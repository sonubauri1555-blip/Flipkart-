package com.example.toonstudio.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.toonstudio.model.CartoonProject
import com.example.toonstudio.ui.ToonStudioViewModel
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
fun ProjectLibraryScreen(
    viewModel: ToonStudioViewModel,
    modifier: Modifier = Modifier
) {
    val allProjects by viewModel.allProjects.collectAsState()
    val currentProject by viewModel.currentProject.collectAsState()
    val context = LocalContext.current

    var showNewProjectDialog by remember { mutableStateOf(false) }
    var showScriptExportDialog by remember { mutableStateOf(false) }

    if (showNewProjectDialog) {
        NewProjectModal(
            onCreate = { title, desc ->
                viewModel.createNewProject(title, desc)
                showNewProjectDialog = false
            },
            onDismiss = { showNewProjectDialog = false }
        )
    }

    if (showScriptExportDialog) {
        val scriptContent = viewModel.exportScriptText()
        Dialog(onDismissRequest = { showScriptExportDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = StudioSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📜 সম্পূর্ণ স্ক্রিপ্ট এক্সপোর্ট",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "এই স্ক্রিপ্টটি কপি করে ইউটিউব ডেসক্রিপশন বা স্টোরিবোর্ড হিসেবে ব্যবহার করতে পারেন:",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(StudioCard, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = scriptContent,
                            color = Color(0xFFECEFF1),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showScriptExportDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("বন্ধ করুন", color = TextSecondary)
                        }
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Cartoon Script", scriptContent)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "স্ক্রিপ্ট ক্লিপবোর্ডে কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                                showScriptExportDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ToonCyan, contentColor = Color.Black),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("কপি করুন")
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(16.dp)
            .padding(bottom = 70.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = "Library", tint = ToonCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "কার্টুন মুভি লাইব্রেরি (Movie Projects)",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "আপনার তৈরি সিনেমা ও পর্বসমূহ সংরক্ষণ",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create New Project Banner
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.5.dp, ToonCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .clickable { showNewProjectDialog = true }
                .testTag("new_project_card"),
            color = StudioCardElevated
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(ToonCyan.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New", tint = ToonCyan, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "+ নতুন কার্টুন মুভি শুরু করুন",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "১-২ ঘণ্টার মতো বড় ভিডিও বা নতুন পর্বের স্ক্রিপ্ট লিখুন",
                        color = ToonYellow,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Export Current Script button
        OutlinedButton(
            onClick = { showScriptExportDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Script", tint = ToonCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("বর্তমান সিনেমার স্ক্রিপ্ট এক্সপোর্ট করুন", color = ToonCyan, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "সংরক্ষিত সিনেমা তালিকা (${allProjects.size} টি প্রজেক্ট):",
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(allProjects) { proj ->
                val isActive = proj.id == currentProject.id
                ProjectItemCard(
                    project = proj,
                    isActive = isActive,
                    onSelect = { viewModel.switchProject(proj) }
                )
            }
        }
    }
}

@Composable
private fun ProjectItemCard(
    project: CartoonProject,
    isActive: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(if (isActive) 1.5.dp else 1.dp, if (isActive) ToonPink else StudioBorder, RoundedCornerShape(12.dp))
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = if (isActive) StudioCardElevated else StudioCard)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Movie,
                contentDescription = "Movie",
                tint = if (isActive) ToonPink else TextSecondary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    color = if (isActive) Color.White else TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = project.description,
                    color = TextMuted,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "⏱️ মোট সময়: ${project.formattedDuration}",
                        color = ToonYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🎬 ${project.scenes.size} টি দৃশ্য",
                        color = ToonCyan,
                        fontSize = 10.sp
                    )
                }
            }
            if (isActive) {
                Surface(
                    color = ToonPink.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ToonPink)
                ) {
                    Text(
                        text = "সক্রিয়",
                        color = ToonPink,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun NewProjectModal(
    onCreate: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = StudioSurface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, StudioBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "🎬 নতুন কার্টুন সিনেমা তৈরি",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("সিনেমার নাম (Movie Title)") },
                    placeholder = { Text("যেমন: চাঁদের দেশে বাবলু ও রোবট") },
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

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("গল্পের সারসংক্ষেপ (Synopsis)") },
                    placeholder = { Text("যেমন: একটি রোমাঞ্চকর মহাকাশ অভিযান...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = StudioCard,
                        unfocusedContainerColor = StudioCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedIndicatorColor = ToonYellow
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("বাতিল", color = TextSecondary)
                    }
                    Button(
                        onClick = { onCreate(title, desc) },
                        colors = ButtonDefaults.buttonColors(containerColor = ToonCyan, contentColor = Color.Black),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("তৈরি করুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
