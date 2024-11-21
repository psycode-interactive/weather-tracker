package com.psycodeinteractive.weathertracker.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface WeatherAppGraphRoute {
    @Serializable
    data object Root : WeatherAppGraphRoute

    @Serializable
    data object Forecast : WeatherAppGraphRoute
}
