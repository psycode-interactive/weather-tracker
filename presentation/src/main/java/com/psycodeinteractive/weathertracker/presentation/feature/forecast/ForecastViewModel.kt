package com.psycodeinteractive.weathertracker.presentation.feature.forecast

import androidx.lifecycle.viewModelScope
import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.usecase.GetForecastsByQueryUseCase
import com.psycodeinteractive.weathertracker.domain.usecase.GetSavedForecastUseCase
import com.psycodeinteractive.weathertracker.domain.usecase.RefreshSavedForecastUseCase
import com.psycodeinteractive.weathertracker.domain.usecase.SaveForecastUseCase
import com.psycodeinteractive.weathertracker.presentation.base.BaseViewModel
import com.psycodeinteractive.weathertracker.presentation.base.ViewState
import com.psycodeinteractive.weathertracker.presentation.feature.forecast.mapper.toDomain
import com.psycodeinteractive.weathertracker.presentation.feature.forecast.mapper.toPresentation
import com.psycodeinteractive.weathertracker.presentation.feature.forecast.model.ForecastPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.update

private const val QUERY_DEBOUNCE_MILLISECONDS = 300L

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastsByQueryUseCase: GetForecastsByQueryUseCase,
    private val getSavedForecastUseCase: GetSavedForecastUseCase,
    private val saveForecastUseCase: SaveForecastUseCase,
    private val refreshSavedForecastUseCase: RefreshSavedForecastUseCase
): BaseViewModel<ForecastViewState>() {
    override val initialViewState = ForecastViewState.Loading

    private val searchQueryFlow = MutableStateFlow("")

    init {
        getSavedForecast()
        observeSearch()
    }

    private fun getSavedForecast() {
        viewModelScope.launch {
            val savedForecast = refreshSavedForecastUseCase().getOrNull() ?: getSavedForecastUseCase()
            updateViewState {
                ForecastViewState.Content(currentForecast = savedForecast?.toPresentation())
            }
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQueryFlow
                .filterNot(String::isBlank)
                .debounce(QUERY_DEBOUNCE_MILLISECONDS)
                .collect(::searchForecasts)
        }
    }

    private suspend fun searchForecasts(query: String) {
        val items = if (query.isNotBlank()) {
            getForecastsByQueryUseCase(query).getOrNull().orEmpty()
        } else {
            emptyList()
        }
        updateViewState { lastState ->
            lastState as ForecastViewState.Content
            lastState.copy(
                forecastSearchResults = items.map(ForecastDomainModel::toPresentation)
            )
        }
    }

    fun onSelectForecastAction(forecast: ForecastPresentationModel) {
        viewModelScope.launch {
            saveForecastUseCase(forecast.toDomain())
            updateViewState { lastState ->
                lastState as ForecastViewState.Content
                lastState.copy(
                    currentForecast = forecast,
                    query = "",
                    forecastSearchResults = emptyList()
                )
            }
        }
    }

    fun onQueryChanged(query: String) {
        updateViewState { lastState ->
            lastState as ForecastViewState.Content
            lastState.copy(
                query = query
            )
        }
        searchQueryFlow.update { query }
    }
}

sealed interface ForecastViewState : ViewState {
    object Loading : ForecastViewState
    data class Content(
        val currentForecast: ForecastPresentationModel? = null,
        val forecastSearchResults: List<ForecastPresentationModel> = emptyList(),
        val isRefreshing: Boolean = false,
        val query: String = ""
    ) : ForecastViewState
}
