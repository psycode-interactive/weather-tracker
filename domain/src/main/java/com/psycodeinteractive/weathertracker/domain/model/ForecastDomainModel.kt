package com.psycodeinteractive.weathertracker.domain.model

data class ForecastDomainModel(
    val location: LocationDomainModel,
    val weather: WeatherDomainModel,
    val isSaved: Boolean
)
