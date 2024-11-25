package com.psycodeinteractive.weathertracker.data.source.local.model

import com.psycodeinteractive.weathertracker.data.model.ForecastDataModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface ForecastLocalModel {
    @Serializable
    data object Empty : ForecastLocalModel
    @Serializable
    data class Forecast(val forecast: ForecastDataModel) : ForecastLocalModel
}
