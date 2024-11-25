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
    icon = "https:$icon",
)

fun ForecastDomainModel.toData() = ForecastDataModel(
    location = location.toData(),
    weather = weather.toData(),
)

fun WeatherDomainModel.toData() = WeatherDataModel(
    tempC = temperatureCelsius,
    condition = condition.toData(),
    humidity = humidity,
    uv = uv,
    feelslikeC = feelsLikeCelsius
)

fun LocationDomainModel.toData() = LocationDataModel(
    name = name
)

fun ConditionDomainModel.toData() = ConditionDataModel(
    icon = icon,
)
