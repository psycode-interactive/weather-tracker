package com.psycodeinteractive.weathertracker.data.repository

import androidx.datastore.core.DataStore
import com.psycodeinteractive.weathertracker.data.mapper.toDomain
import com.psycodeinteractive.weathertracker.data.model.ForecastDataModel
import com.psycodeinteractive.weathertracker.data.source.remote.ForecastApiService
import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastDataRepository @Inject constructor(
    private val forecastApiService: ForecastApiService,
    private val dataStore: DataStore<ForecastDataModel>,
    private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {
    override suspend fun searchCities(query: String) = withContext(ioDispatcher) {
        Result.runCatching {
            forecastApiService.search(query).map { city ->
                getForecast(city.name)
            }
        }
    }

    override suspend fun getSavedForecast() = withContext(ioDispatcher) {
        Result.runCatching {
            dataStore.data.firstOrNull()?.toDomain()
        }
    }

    override suspend fun refreshSavedForecast(): Result<ForecastDomainModel> {
        TODO("Not yet implemented")
    }

    private fun getForecast(query: String) = withContext(ioDispatcher) {
        Result.runCatching {
            forecastApiService.fetchForecast(query).toDomain()
        }
    }
}
