package com.psycodeinteractive.weathertracker.presentation.feature.forecast

import androidx.compose.runtime.Composable
import com.psycodeinteractive.weathertracker.presentation.base.PreviewWithThemes
import com.psycodeinteractive.weathertracker.presentation.base.Screen

@Composable
fun ForecastScreen() {
    Screen<ForecastViewModel, _> {
        ForecastScreenContent(
            viewState = viewState,
            onTryAgainClick = viewModel::onTryAgainAction,
            onAddUserClick = viewModel::onAddUserAction,
            onNavigateToAddUser = onNavigateToAddUser,
            onNavigateToAddUserHandled = viewModel::onNavigateToAddUserHandled,
            onDismissDeleteUserDialog = viewModel::onDismissDeleteDialogAction,
            onDeleteUserAction = viewModel::onDeleteUserAction,
            onUserLongPress = viewModel::onUserLongPressAction
        )
    }
}

@Composable
private fun ForecastScreenContent() {

}

@PreviewWithThemes
@Composable
fun PreviewForecastScreen() {
    ForecastScreen(
        onNavigateToAddUser = {},
        wasUserAdded = false
    )
}
