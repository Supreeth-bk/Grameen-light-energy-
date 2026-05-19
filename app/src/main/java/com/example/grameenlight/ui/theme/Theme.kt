package com.example.grameenlight.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// SOP Specified Colors
val StatusGreen = Color(0xFF27AE60)
val StatusRed = Color(0xFFE74C3C)
val StatusYellow = Color(0xFFF39C12)
val PrimaryBlue = Color(0xFF3498DB) // Action Blue
val SurfaceNocturnal = Color(0xFF131315)
val SurfaceContainer = Color(0xFF1F2021)
val OnSurfaceLight = Color(0xFFE4E2E3)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = Color(0xFF92CCFF),
    background = SurfaceNocturnal,
    surface = SurfaceContainer,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Color(0xFF343536)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = Color(0xFF3398DB),
    background = Color(0xFFF8F9FA),
    surface = Color.White,
    onBackground = Color(0xFF131315),
    onSurface = Color(0xFF131315)
)

@Composable
fun GrameenLightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Uses the default Material3 Typography
        content = content
    )
}
