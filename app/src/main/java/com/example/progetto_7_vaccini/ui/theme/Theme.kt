package com.example.progetto_7_vaccini.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary          = Teal900,
    onPrimary        = Color.White,
    primaryContainer = Teal100,
    onPrimaryContainer = Teal900,
    secondary        = Teal700,
    onSecondary      = Color.White,
    background       = Surface,
    onBackground     = Slate800,
    surface          = Color.White,
    onSurface        = Slate800,
    surfaceVariant   = Slate50,
    onSurfaceVariant = Slate600,
    outline          = Slate200,
    error            = Red700,
    onError          = Color.White
)

private val DarkColors = darkColorScheme(
    primary          = Teal600,
    onPrimary        = Color.White,
    primaryContainer = Teal900,
    onPrimaryContainer = Teal100,
    secondary        = Teal700,
    onSecondary      = Color.White,
    background       = Color(0xFF0A1A1A),
    onBackground     = Color(0xFFE2F0EE),
    surface          = Color(0xFF111F1F),
    onSurface        = Color(0xFFE2F0EE),
    surfaceVariant   = Color(0xFF1A2E2E),
    onSurfaceVariant = Slate400,
    outline          = Color(0xFF2D4444),
    error            = Color(0xFFF87171),
    onError          = Color(0xFF450A0A)
)

@Composable
fun VaccineBiologicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = Typography,
        content     = content
    )
}