package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EvEmerald,
    onPrimary = Color.Black,
    primaryContainer = EvEmeraldDark,
    onPrimaryContainer = Color.White,
    secondary = EvCyan,
    onSecondary = Color.Black,
    secondaryContainer = EvCyanDark,
    onSecondaryContainer = Color.White,
    tertiary = EvAmber,
    onTertiary = Color.Black,
    tertiaryContainer = EvAmberLight,
    onTertiaryContainer = Color.Black,
    background = CockpitBackground,
    onBackground = TextPrimary,
    surface = CockpitSurface,
    onSurface = TextPrimary,
    surfaceVariant = CockpitCard,
    onSurfaceVariant = TextSecondary,
    outline = CockpitBorder,
    error = EvRed,
    errorContainer = EvRedDark,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
