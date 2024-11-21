package com.psycodeinteractive.weathertracker.domain.model

data class WeatherDomainModel(
    val temperatureCelsius: Double,
    val condition: ConditionDomainModel,
    val humidity: Long,
    val uv: Long,
    val feelsLikeCelsius: Double,
)

data class ConditionDomainModel(
    val text: String,
    val icon: String,
)
