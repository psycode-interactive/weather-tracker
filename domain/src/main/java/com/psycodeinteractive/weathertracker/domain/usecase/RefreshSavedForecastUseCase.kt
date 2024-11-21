package com.psycodeinteractive.weathertracker.domain.usecase

import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.repository.ForecastRepository
import javax.inject.Inject

class RefreshSavedForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(): Result<ForecastDomainModel> {
        return forecastRepository.refreshSavedForecast()
    }
}
