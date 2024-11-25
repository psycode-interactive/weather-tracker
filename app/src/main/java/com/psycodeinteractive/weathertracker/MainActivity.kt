package com.psycodeinteractive.weathertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.psycodeinteractive.weathertracker.presentation.theme.WeatherTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTrackerTheme {
                AppEntryPoint(
                    navController = rememberNavController(),
                )
            }
        }
    }
}
