package com.psycodeinteractive.weathertracker

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.psycodeinteractive.weathertracker.presentation.WeatherAppGraphRoute

@Composable
fun AppEntryPoint(
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) { padding ->
        NavHost(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
            navController = navController,
            startDestination = WeatherAppGraphRoute.Root,
            contentAlignment = Alignment.TopStart
        ) {
            weatherGraph(
                navController = navController,
            )
        }
    }
}

fun NavGraphBuilder.weatherGraph(
    navController: NavHostController
) {
    navigation<WeatherAppGraphRoute.Root>(
        startDestination = WeatherAppGraphRoute.Forecast,
    ) {
        userListScreenRoute(
            onNavigateToAddUser = navController::navigateToAddUser
        )
        addUserScreenRoute(
            navController = navController,
            onNavigateUp = navController::popBackStack
        )
    }
}
