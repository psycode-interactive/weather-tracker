package com.psycodeinteractive.weathertracker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val themeColorsLight = lightColorScheme(
    primary = Color(0xFF45875B),
    onPrimary = Color.White,
    secondary = Color.White,
    onSecondary = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun WeatherTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = themeColorsLight,
    ) {
        Surface {
            content()
        }
    }
}
