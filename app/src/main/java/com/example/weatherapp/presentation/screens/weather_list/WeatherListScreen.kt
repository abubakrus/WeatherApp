package com.example.weatherapp.presentation.screens.weather_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.presentation.components.DailyWeathersItem
import com.example.weatherapp.presentation.models.CountryInfo
import com.example.weatherapp.presentation.models.WeatherDayInfoUi
import com.example.weatherapp.presentation.models.WeatherUi
import com.example.weatherapp.presentation.theme.Purple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun WeatherListScreenPreview() {
    MaterialTheme {
        WeatherListScreen(
            uiStateFlow = MutableStateFlow(WeatherListUiState.Loading)
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherListScreen(
    uiStateFlow: StateFlow<WeatherListUiState>,
    modifier: Modifier = Modifier,
) {

    val uiState = uiStateFlow.collectAsStateWithLifecycle().value

    val fullScreenModifier = Modifier
        .fillMaxSize()
        .background(Purple)

    when (uiState) {
        is WeatherListUiState.Loading -> {
            LoadingWeatherListScreen(fullScreenModifier)
        }

        is WeatherListUiState.Loaded -> {
            LoadedWeatherListScreen(
                dailyWeathers = uiState.dailyWeathers,
                countryInfo = uiState.countryInfo,
                fullScreenModifier
            )
        }

        is WeatherListUiState.Error -> {
            ErrorWeatherListScreen(uiState.message, fullScreenModifier)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun LoadedWeatherListScreen(
    dailyWeathers: List<WeatherDayInfoUi>,
    countryInfo: CountryInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.statusBarsPadding(),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 24.dp)

        ) {
            items(
                items = dailyWeathers,
                key = { item -> item.hashCode() }
            ) { weather ->
                DailyWeathersItem(
                    weather = weather,
                    countryInfo = countryInfo
                )
            }
        }
    }
}

@Composable
private fun LoadingWeatherListScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun ErrorWeatherListScreen(
    errorMessage: String, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage, style = MaterialTheme.typography.titleLarge
        )
    }

}