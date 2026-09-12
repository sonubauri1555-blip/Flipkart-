package com.example.toonstudio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.toonstudio.model.StudioNavTab
import com.example.toonstudio.ui.screens.CharacterStudioScreen
import com.example.toonstudio.ui.screens.CinemaPlayerScreen
import com.example.toonstudio.ui.screens.ProjectLibraryScreen
import com.example.toonstudio.ui.screens.StudioDirectorScreen
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ToonCyan
import com.example.ui.theme.ToonPink
import com.example.ui.theme.ToonYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToonStudioApp(
    viewModel: ToonStudioViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val project by viewModel.currentProject.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StudioBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(ToonCyan, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = "ToonStudio",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "কার্টুন স্টুডিও (ToonStudio)",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = project.title,
                                color = ToonPink,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⏱️ ${project.formattedDuration}",
                                color = ToonYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioSurface)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = StudioSurface,
                modifier = Modifier
                    .border(width = 1.dp, color = StudioBorder)
                    .testTag("bottom_nav_bar")
            ) {
                // 1. Script Director Tab
                NavigationBarItem(
                    selected = currentTab == StudioNavTab.SCRIPT_DIRECTOR,
                    onClick = { viewModel.setTab(StudioNavTab.SCRIPT_DIRECTOR) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Theaters,
                            contentDescription = "Director"
                        )
                    },
                    label = { Text("স্ক্রিপ্ট ডিরেক্টর", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = ToonCyan,
                        indicatorColor = ToonCyan,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    )
                )

                // 2. Cinema Player Tab
                NavigationBarItem(
                    selected = currentTab == StudioNavTab.CINEMA_PLAYER,
                    onClick = { viewModel.setTab(StudioNavTab.CINEMA_PLAYER) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = "Cinema"
                        )
                    },
                    label = { Text("সিনেমা প্লেয়ার", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = ToonPink,
                        indicatorColor = ToonPink,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    )
                )

                // 3. Characters Tab
                NavigationBarItem(
                    selected = currentTab == StudioNavTab.CHARACTERS,
                    onClick = { viewModel.setTab(StudioNavTab.CHARACTERS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Characters"
                        )
                    },
                    label = { Text("চরিত্র স্টুডিও", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = ToonYellow,
                        indicatorColor = ToonYellow,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    )
                )

                // 4. Library Tab
                NavigationBarItem(
                    selected = currentTab == StudioNavTab.STORY_LIBRARY,
                    onClick = { viewModel.setTab(StudioNavTab.STORY_LIBRARY) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Library"
                        )
                    },
                    label = { Text("মুভি লাইব্রেরি", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = ToonCyan,
                        indicatorColor = ToonCyan,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextMuted
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentTab) {
                StudioNavTab.SCRIPT_DIRECTOR -> StudioDirectorScreen(viewModel = viewModel)
                StudioNavTab.CINEMA_PLAYER -> CinemaPlayerScreen(viewModel = viewModel)
                StudioNavTab.CHARACTERS -> CharacterStudioScreen(viewModel = viewModel)
                StudioNavTab.STORY_LIBRARY -> ProjectLibraryScreen(viewModel = viewModel)
            }
        }
    }
}
