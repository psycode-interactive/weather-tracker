package com.psycodeinteractive.weathertracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDataModel(
    @SerialName("temp_c")
    val tempC: Double,
    val condition: ConditionDataModel,
    val humidity: Long,
    @SerialName("feelslike_c")
    val feelslikeC: Double,
    val uv: Double,
)

@Serializable
data class ConditionDataModel(
    val icon: String,
)
