package com.example.toto.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.toto.model.TotoTab
import com.example.toto.ui.components.TopHeaderBar
import com.example.toto.ui.components.TotoBottomNav
import com.example.toto.ui.screens.BatteryBmsScreen
import com.example.toto.ui.screens.CockpitScreen
import com.example.toto.ui.screens.FareMeterScreen
import com.example.toto.ui.screens.SettingsScreen
import com.example.toto.ui.screens.SwitchesScreen
import com.example.ui.theme.CockpitBackground

@Composable
fun TotoMainScreen(
    viewModel: TotoViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.vehicleState.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CockpitBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopHeaderBar(
                state = state,
                currentLanguage = selectedLanguage,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onToggleConnection = { viewModel.toggleBleConnection() },
                onTriggerEmergencySos = { viewModel.toggleEmergencySos() }
            )
        },
        bottomBar = {
            TotoBottomNav(
                currentTab = currentTab,
                currentLanguage = selectedLanguage,
                onTabSelected = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CockpitBackground)
        ) {
            Crossfade(
                targetState = currentTab,
                label = "tab_crossfade"
            ) { tab ->
                when (tab) {
                    TotoTab.COCKPIT -> CockpitScreen(viewModel = viewModel)
                    TotoTab.SWITCHES -> SwitchesScreen(viewModel = viewModel)
                    TotoTab.BATTERY -> BatteryBmsScreen(viewModel = viewModel)
                    TotoTab.FARE_METER -> FareMeterScreen(viewModel = viewModel)
                    TotoTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
