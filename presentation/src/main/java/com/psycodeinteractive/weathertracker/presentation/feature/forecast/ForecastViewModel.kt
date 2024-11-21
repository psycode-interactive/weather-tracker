package com.psycodeinteractive.weathertracker.presentation.feature.forecast

import androidx.lifecycle.viewModelScope
import com.psycodeinteractive.weathertracker.domain.model.ForecastDomainModel
import com.psycodeinteractive.weathertracker.domain.usecase.GetForecastsByQueryUseCase
import com.psycodeinteractive.weathertracker.domain.usecase.GetSavedForecastUseCase
import com.psycodeinteractive.weathertracker.domain.usecase.RefreshSavedForecastUseCase
import com.psycodeinteractive.weathertracker.presentation.base.BaseViewModel
import com.psycodeinteractive.weathertracker.presentation.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val QUERY_DEBOUNCE_MILLISECONDS = 300L

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastsByQueryUseCase: GetForecastsByQueryUseCase,
    private val getSavedForecastUseCase: GetSavedForecastUseCase,
    private val refreshSavedForecastUseCase: RefreshSavedForecastUseCase
): BaseViewModel<ForecastViewState>() {
    override val initialViewState = ForecastViewState.Loading

    private val searchQueryFlow = MutableStateFlow("")

    init {
         viewModelScope.launch {
             val savedForecast = getSavedForecastUseCase()
             savedForecast?.run {
                 refreshSavedForecastUseCase()
                     .onSuccess { forecast ->
                         updateViewState { ForecastViewState.Success(forecast) }
                     }
                     .onFailure {
                         updateViewState { ForecastViewState.Error(it.message ?: "An error occurred") }
                     }
             }
         }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(QUERY_DEBOUNCE_MILLISECONDS)
                .collect {
                    if (it.isBlank()) {
                        getForecastsByQueryUseCase(query)
                            .onSuccess { forecast ->
                                updateViewState { ForecastViewState.Success(forecast) }
                            }
                            .onFailure {
                                updateViewState { ForecastViewState.Error(it.message ?: "An error occurred") }
                            }
                    } else {
                        updateViewState { lastState ->
                            lastState as ForecastViewState.Content
                            lastState.copy(forecastSearchResults = emptyList())
                        }
                    }
                }
        }
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            getForecastsByQueryUseCase(query)
                .onSuccess { forecast ->
                    updateViewState { ForecastViewState.Success(forecast) }
                }
                .onFailure {
                    updateViewState { ForecastViewState.Error(it.message ?: "An error occurred") }
                }
        }
    }
}

sealed class ForecastViewState : ViewState {
    object Loading : ForecastViewState()
    data class Error(val message: String) : ForecastViewState()
    data class Content(
        val currentCityForecast: List<ForecastDomainModel>,
        val forecastSearchResults: List<ForecastDomainModel>
    ) : ForecastViewState()
}
