package com.psycodeinteractive.weathertracker.data.repository

import androidx.datastore.core.DataStore
import com.psycodeinteractive.weathertracker.data.mapper.toData
import com.psycodeinteractive.weathertracker.data.mapper.toDomain
import com.psycodeinteractive.weathertracker.data.source.local.model.ForecastLocalModel
import com.psycodeinteractive.weathertracker.data.source.remote.ForecastApiService
import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

class ForecastDataRepository @Inject constructor(
    private val forecastApiService: ForecastApiService,
    private val dataStore: DataStore<ForecastLocalModel>,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    override suspend fun searchCities(query: String) = Result.runCatching {
        withContext(ioDispatcher) {
            forecastApiService.search(query).map { city ->
                getForecast(city.name)
            }
        }
    }.onFailure(Timber::e)

    override suspend fun getSavedForecast() = withContext(ioDispatcher) {
        val savedForecast = dataStore.data.firstOrNull() as? ForecastLocalModel.Forecast
        savedForecast?.forecast?.toDomain(true)
    }

    override suspend fun refreshSavedForecast() = Result.runCatching {
        withContext(ioDispatcher) {
            val savedForecast = requireNotNull(getSavedForecast()) { "No saved forecast found" }
            getForecast(savedForecast.location.name)
        }
    }.onFailure(Timber::e)

    override suspend fun saveForecast(forecast: ForecastDomainModel) {
        withContext(ioDispatcher) {
            dataStore.updateData { ForecastLocalModel.Forecast(forecast.toData()) }
        }
    }

    private suspend fun getForecast(query: String): ForecastDomainModel {
        val forecast = forecastApiService.fetchForecast(query)
        val isSaved = getSavedForecast()?.location?.name == forecast.location.name
        return forecast.toDomain(isSaved).apply {
            if (isSaved) saveForecast(this)
        }
    }
}
