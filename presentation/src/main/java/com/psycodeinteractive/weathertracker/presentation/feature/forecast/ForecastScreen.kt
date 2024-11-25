package com.psycodeinteractive.weathertracker.presentation.feature.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.toCoilUri
import com.psycodeinteractive.weathertracker.presentation.R
import com.psycodeinteractive.weathertracker.presentation.base.PreviewWithThemes
import com.psycodeinteractive.weathertracker.presentation.base.Screen
import com.psycodeinteractive.weathertracker.presentation.feature.forecast.model.ForecastPresentationModel
import com.psycodeinteractive.weathertracker.presentation.theme.WeatherTrackerTheme

@Composable
fun ForecastScreen() {
    Screen<ForecastViewModel, _> {
        ForecastScreenContent(
            viewState = viewState,
            onQueryChange = viewModel::onQueryChanged,
            onSelectForecastAction = viewModel::onSelectForecastAction
        )
    }
}

@Composable
private fun ForecastScreenContent(
    viewState: ForecastViewState,
    onQueryChange: (String) -> Unit,
    onSelectForecastAction: (ForecastPresentationModel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        with(viewState) {
            when (this) {
                ForecastViewState.Loading -> CircularProgressIndicator()
                is ForecastViewState.Content -> {
                    SearchField(
                        query = query,
                        onQueryChange = onQueryChange
                    )
                    when {
                        query.isNotBlank() -> SearchResults(
                            searchResults = forecastSearchResults,
                            onSelectForecastAction = onSelectForecastAction
                        )
                        currentForecast != null -> CurrentCityForecast(forecast = currentForecast)
                        else -> NoForecastSaved()
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 44.dp)
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.shapes.large
            )
            .padding(horizontal = 20.dp)
            .height(46.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp),
            value = query,
            maxLines = 1,
            onValueChange = onQueryChange,
            textStyle = MaterialTheme.typography.labelLarge
                .copy(color = MaterialTheme.colorScheme.primary),
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterEnd)
        )
        if (query.isEmpty()) {
            Text(
                text = stringResource(R.string.search_location),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun SearchResults(
    searchResults: List<ForecastPresentationModel>,
    onSelectForecastAction: (ForecastPresentationModel) -> Unit
) {
    val focusManager = LocalFocusManager.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        items(searchResults) { forecast ->
            ForecastCityCard(
                cityName = forecast.cityName,
                temperatureCelsius = forecast.temperatureCelsius,
                conditionIconUrl = forecast.conditionIconUrl,
                onClick = {
                    focusManager.clearFocus()
                    onSelectForecastAction(forecast)
                }
            )
        }
    }
}

@Composable
private fun CurrentCityForecast(
    forecast: ForecastPresentationModel,
) {
    with(forecast) {
        Column(
            modifier = Modifier
                .padding(top = 74.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier.size(123.dp),
                model = conditionIconUrl,
                contentDescription = null,
            )
            Text(
                text = cityName,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${temperatureCelsius}°C",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.size(11.dp))

            ForecastDetails(
                humidity = humidity,
                uv = uv,
                feelsLikeCelsius = feelsLikeCelsius
            )
        }
    }
}

@Composable
private fun NoForecastSaved() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_city_selected),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.please_search_for_a_city),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ForecastDetails(
    humidity: Long,
    uv: Double,
    feelsLikeCelsius: Double,
) {
    Row(
        modifier = Modifier
            .padding(start = 57.dp, end = 44.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.large)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.humidity),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = "$humidity %",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.uv),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = uv.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Column(
            modifier = Modifier.padding(end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.feels_like),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = "$feelsLikeCelsius°",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun ForecastCityCard(
    cityName: String,
    temperatureCelsius: Double,
    conditionIconUrl: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 31.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${temperatureCelsius}°",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        AsyncImage(
            modifier = Modifier.size(84.dp),
            model = conditionIconUrl,
            contentScale = ContentScale.FillHeight,
            contentDescription = null
        )
    }
}

@PreviewWithThemes
@Composable
fun PreviewForecastScreenContentLoading() {
    WeatherTrackerTheme {
        ForecastScreenContent(
            viewState = ForecastViewState.Loading,
            onQueryChange = {},
            onSelectForecastAction = {}
        )
    }
}

@PreviewWithThemes
@Composable
fun PreviewForecastScreenContent() {
    WeatherTrackerTheme {
        ForecastScreenContent(
            viewState = ForecastViewState.Content(
                currentForecast = ForecastPresentationModel(
                    cityName = "Berlin",
                    temperatureCelsius = 20.2,
                    humidity = 100,
                    conditionIconUrl = "https://example.com/icon.png",
                    uv = 7.0,
                    feelsLikeCelsius = 21.2,
                    isCurrent = true
                )
            ),
            onQueryChange = {},
            onSelectForecastAction = {}
        )
    }
}
