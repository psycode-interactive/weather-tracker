package com.psycodeinteractive.weathertracker.domain.model

data class WeatherDomainModel(
    val temperatureCelsius: Double,
    val condition: ConditionDomainModel,
    val humidity: Long,
    val uv: Double,
    val feelsLikeCelsius: Double,
)

data class ConditionDomainModel(
    val icon: String,
)
