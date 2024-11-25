package com.psycodeinteractive.weathertracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDataModel(
    val location: LocationDataModel,
    @SerialName("current")
    val weather: WeatherDataModel
)
