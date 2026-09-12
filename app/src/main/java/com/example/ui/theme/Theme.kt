package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ToonCyan,
    onPrimary = Color.Black,
    primaryContainer = ToonBlue,
    onPrimaryContainer = Color.White,
    secondary = ToonPink,
    onSecondary = Color.White,
    secondaryContainer = ToonPurple,
    onSecondaryContainer = Color.White,
    tertiary = ToonYellow,
    onTertiary = Color.Black,
    tertiaryContainer = ToonOrange,
    onTertiaryContainer = Color.Black,
    background = StudioBackground,
    onBackground = TextPrimary,
    surface = StudioSurface,
    onSurface = TextPrimary,
    surfaceVariant = StudioCard,
    onSurfaceVariant = TextSecondary,
    outline = StudioBorder,
    error = ToonRed,
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
