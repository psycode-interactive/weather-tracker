package com.psycodeinteractive.weathertracker.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val themeColorsLight = lightColorScheme(
    primary = Color(0xFF2C2C2C),
    secondary = Color(0xFFF2F2F2),
    tertiary = Color(0xFF9A9A9A),
    onSecondary = Color(0xFFC4C4C4),
    surface = Color.White,
)

@Composable
fun WeatherTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = themeColorsLight,
        typography = themeTypography
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
        ) {
            content()
        }
    }
}
