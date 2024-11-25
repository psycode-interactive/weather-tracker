package com.psycodeinteractive.weathertracker.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State : ViewState> : ViewModel() {

    private val _viewState by lazy { MutableStateFlow(initialViewState) }
    val viewState by lazy { _viewState.asStateFlow() }

    protected abstract val initialViewState: State

    protected fun updateViewState(update: (lastState: State) -> State) {
        _viewState.update(update)
    }
}

interface ViewState
