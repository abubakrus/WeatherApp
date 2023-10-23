package com.example.weatherapp.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.presentation.components.HourlyWeatherItemList
import com.example.weatherapp.presentation.models.CountryInfo
import com.example.weatherapp.presentation.models.WeatherDayInfoUi
import com.example.weatherapp.presentation.utils.DayType
import com.example.weatherapp.presentation.utils.fetchDayType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navigateToWeatherList: () -> Unit,
    uiStateFlow: StateFlow<MainScreenUiState>,
    modifier: Modifier = Modifier,
) {
    val uiState = uiStateFlow.collectAsStateWithLifecycle().value

    val fullScreenModifier = Modifier.fillMaxSize()

    if (uiState is MainScreenUiState.Error) {
        ErrorMainScreen(
            errorMessage = "", modifier = fullScreenModifier
        )
    }

    MainScreen(
        navigateToWeatherList = navigateToWeatherList, uiState = uiState
    )

}

@Composable
private fun MainScreen(
    navigateToWeatherList: () -> Unit, uiState: MainScreenUiState, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        val backgroundImageId = when (fetchDayType()) {
            DayType.SUNSET -> R.drawable.main_backround_sunsed
            DayType.LIGHT -> R.drawable.main_screen_bacground_light
            DayType.NIGHT -> R.drawable.main_screen_bacground_night
        }
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painterResource(id = backgroundImageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        when (uiState) {
            is MainScreenUiState.Loading -> LoadingBlock()
            is MainScreenUiState.Loaded -> LoadedBlock(
                currentWeather = uiState.weather.currentWeather, countryInfo = uiState.countryInfo
            )

            else -> {
                NoOnLocation()
            }
        }

        IconButton(
            onClick = navigateToWeatherList,
            modifier = Modifier
                .padding(
                    top = 32.dp, start = 24.dp
                )
                .size(72.dp)
                .align(Alignment.TopEnd),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.menu_icon),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun BoxScope.LoadedBlock(
    currentWeather: WeatherDayInfoUi, countryInfo: CountryInfo, modifier: Modifier = Modifier
) {

    var isShowInfoBlock by remember { mutableStateOf(false) }

    rememberCoroutineScope().launch {
        delay(100)
        isShowInfoBlock = true
    }

    AnimatedVisibility(
        modifier = Modifier.align(Alignment.TopCenter), visible = isShowInfoBlock, enter = fadeIn(
            animationSpec = tween(durationMillis = 1000)
        ), exit = fadeOut(
            animationSpec = tween(durationMillis = 1000)
        )
    ) {
        WeatherInfo(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 52.dp),
            city = countryInfo.cityName,
            temperature = "${currentWeather.temperature}°",
            weatherType = currentWeather.weatherConditionType.descripition,
            windSpeed = "${currentWeather.windSpeed}km/h"
        )
    }
    AnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomCenter),
        visible = isShowInfoBlock,
        enter = slideInVertically(animationSpec = tween(1000), initialOffsetY = { it / 2 }),
        exit = slideOutVertically(animationSpec = tween(1000), targetOffsetY = { it / 2 })

    ) {
        HourlyWeatherItemList(
            weatherHours = currentWeather.hourlyWeathers,
        )
    }
}

@Composable
private fun BoxScope.LoadingBlock(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 52.dp)
            .fillMaxWidth()
            .height(300.dp)
            .align(Alignment.TopCenter), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMainScreen(
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

@Composable
fun WeatherInfo(
    city: String,
    temperature: String,
    weatherType: String,
    windSpeed: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = city,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = temperature,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = weatherType,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = windSpeed,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
    }


}


@Composable
fun TabBar(
    navigateToWeatherList: (String) -> Unit, modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            modifier = modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.tab_bar_background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Icon(
            modifier = modifier
                .padding(start = 32.dp)
                .padding(top = 20.dp)
                .size(44.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(id = R.drawable.location),
            contentDescription = null,
            tint = Color.White
        )
        Icon(
            modifier = modifier
                .padding(end = 32.dp)
                .padding(top = 20.dp)
                .size(32.dp)
                .align(Alignment.CenterEnd)
                .clickable { navigateToWeatherList("Test") },
            painter = painterResource(id = R.drawable.list),
            contentDescription = null,
            tint = Color.White
        )
        Icon(
            modifier = modifier
                .padding(bottom = 12.dp)
                .size(44.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.plus),
            contentDescription = null,
            tint = Color.Black
        )
    }
}

@Composable
fun NoOnLocation(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 52.dp)
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_location),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold, color = Color.White
            ),
            textAlign = TextAlign.Center
        )
    }

}

