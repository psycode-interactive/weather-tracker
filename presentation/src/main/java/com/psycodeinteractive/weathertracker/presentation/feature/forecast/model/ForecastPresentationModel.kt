package com.psycodeinteractive.weathertracker.presentation.feature.forecast.model

data class ForecastPresentationModel(
    val cityName: String,
    val humidity: Long,
    val uv: Double,
    val temperatureCelsius: Double,
    val feelsLikeCelsius: Double,
    val conditionIconUrl: String,
    val isCurrent: Boolean
)
