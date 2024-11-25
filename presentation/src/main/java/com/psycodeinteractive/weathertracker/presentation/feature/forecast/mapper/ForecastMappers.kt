package com.psycodeinteractive.weathertracker.presentation.feature.forecast.mapper

import com.psycodeinteractive.weathertracker.domain.model.ConditionDomainModel
import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.model.LocationDomainModel
import com.psycodeinteractive.weathertracker.domain.model.WeatherDomainModel
import com.psycodeinteractive.weathertracker.presentation.feature.forecast.model.ForecastPresentationModel

fun ForecastDomainModel.toPresentation() = ForecastPresentationModel(
    cityName = location.name,
    humidity = weather.humidity,
    temperatureCelsius = weather.temperatureCelsius,
    feelsLikeCelsius = weather.feelsLikeCelsius,
    conditionIconUrl = weather.condition.icon,
    uv = weather.uv,
    isCurrent = isSaved
)

fun ForecastPresentationModel.toDomain() = ForecastDomainModel(
    location = LocationDomainModel(cityName),
    weather = WeatherDomainModel(
        humidity = humidity,
        temperatureCelsius = temperatureCelsius,
        feelsLikeCelsius = feelsLikeCelsius,
        condition = ConditionDomainModel(icon = conditionIconUrl),
        uv = uv
    ),
    isSaved = isCurrent
)
