package com.psycodeinteractive.weathertracker.domain.usecase

import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.repository.ForecastRepository
import javax.inject.Inject

class GetSavedForecastUseCase @Inject constructor(
    private val forecastRepository: ForecastRepository
) {
    suspend operator fun invoke(): ForecastDomainModel?{
        return forecastRepository.getSavedForecast()
    }
}
