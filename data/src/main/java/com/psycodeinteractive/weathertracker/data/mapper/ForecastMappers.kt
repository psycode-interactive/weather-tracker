package com.psycodeinteractive.weathertracker.data.mapper

import com.psycodeinteractive.weathertracker.data.model.ConditionDataModel
import com.psycodeinteractive.weathertracker.data.model.ForecastDataModel
import com.psycodeinteractive.weathertracker.data.model.LocationDataModel
import com.psycodeinteractive.weathertracker.data.model.WeatherDataModel
import com.psycodeinteractive.weathertracker.domain.model.ConditionDomainModel
import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.model.LocationDomainModel
import com.psycodeinteractive.weathertracker.domain.model.WeatherDomainModel

fun ForecastDataModel.toDomain(
    isSaved: Boolean
) = ForecastDomainModel(
    location = location.toDomain(),
    weather = weather.toDomain(),
    isSaved = isSaved
)

fun WeatherDataModel.toDomain() = WeatherDomainModel(
    temperatureCelsius = tempC,
    condition = condition.toDomain(),
    humidity = humidity,
    uv = uv,
    feelsLikeCelsius = feelslikeC
)

fun LocationDataModel.toDomain() = LocationDomainModel(
    name = name
)

fun ConditionDataModel.toDomain() = ConditionDomainModel(
    text = text,
    icon = icon,
)
