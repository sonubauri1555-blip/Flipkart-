package com.example.toto.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.TotoLocale
import com.example.toto.model.TotoTab
import com.example.ui.theme.CockpitSurface
import com.example.ui.theme.EvCyan
import com.example.ui.theme.EvEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun TotoBottomNav(
    currentTab: TotoTab,
    currentLanguage: AppLanguage,
    onTabSelected: (TotoTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = CockpitSurface,
        tonalElevation = 8.dp
    ) {
        // Cockpit / Drive Dashboard
        NavigationBarItem(
            selected = currentTab == TotoTab.COCKPIT,
            onClick = { onTabSelected(TotoTab.COCKPIT) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = "Cockpit",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = TotoLocale.tabCockpit(currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == TotoTab.COCKPIT) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = navColors()
        )

        // Electrical Switchboard
        NavigationBarItem(
            selected = currentTab == TotoTab.SWITCHES,
            onClick = { onTabSelected(TotoTab.SWITCHES) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ToggleOn,
                    contentDescription = "Switches",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = TotoLocale.tabSwitches(currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == TotoTab.SWITCHES) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = navColors()
        )

        // Battery & BMS
        NavigationBarItem(
            selected = currentTab == TotoTab.BATTERY,
            onClick = { onTabSelected(TotoTab.BATTERY) },
            icon = {
                Icon(
                    imageVector = Icons.Default.BatteryChargingFull,
                    contentDescription = "BMS",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = TotoLocale.tabBattery(currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == TotoTab.BATTERY) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = navColors()
        )

        // Commercial Fare Meter
        NavigationBarItem(
            selected = currentTab == TotoTab.FARE_METER,
            onClick = { onTabSelected(TotoTab.FARE_METER) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = "Fare Meter",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = TotoLocale.tabFareMeter(currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == TotoTab.FARE_METER) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = navColors()
        )

        // Settings & ECU
        NavigationBarItem(
            selected = currentTab == TotoTab.SETTINGS,
            onClick = { onTabSelected(TotoTab.SETTINGS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Settings",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = TotoLocale.tabSettings(currentLanguage),
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == TotoTab.SETTINGS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = navColors()
        )
    }
}

@Composable
private fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = EvEmerald,
    selectedTextColor = EvEmerald,
    unselectedIconColor = TextMuted,
    unselectedTextColor = TextSecondary,
    indicatorColor = Color(0xFF064E3B).copy(alpha = 0.4f)
)
