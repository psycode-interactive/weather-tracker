package com.psycodeinteractive.weathertracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDataModel(
    val location: LocationDataModel,
    val weather: WeatherDataModel
)
