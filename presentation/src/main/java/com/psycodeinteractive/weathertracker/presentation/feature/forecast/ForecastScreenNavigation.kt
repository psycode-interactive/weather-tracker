package com.psycodeinteractive.weathertracker.presentation.feature.forecast

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.psycodeinteractive.weathertracker.presentation.WeatherAppGraphRoute

fun NavGraphBuilder.forecastScreenRoute() {
    composable<WeatherAppGraphRoute.Forecast> { backStackEntry ->
        ForecastScreen()
    }
}
