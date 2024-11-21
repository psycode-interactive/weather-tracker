package com.psycodeinteractive.weathertracker.domain.repository

import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel

interface ForecastRepository {
    suspend fun searchCities(query: String): Result<List<ForecastDomainModel>>
    suspend fun getSavedForecast(): ForecastDomainModel?
    suspend fun refreshSavedForecast(): Result<ForecastDomainModel>
}
