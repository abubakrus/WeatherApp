package com.example.weatherapp.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.presentation.models.CountryInfo
import com.example.weatherapp.presentation.models.WeatherDayInfoUi
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DailyWeathersItemPreview() {
    MaterialTheme {
        DailyWeathersItem(
            weather = WeatherDayInfoUi.unknown,
            countryInfo = CountryInfo.unknown
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyWeathersItem(
    weather: WeatherDayInfoUi,
    countryInfo: CountryInfo,
    modifier: Modifier = Modifier
) {
    val date = try {
        val df = DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.ENGLISH)
        val localDate = LocalDateTime.parse(weather.time, df)
        Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
    } catch (e: DateTimeParseException) {
        Date()
    }


    var isWeatherLight = false
    when (date.hours) {
        in 19..23 -> isWeatherLight = false
        in 0..6 -> isWeatherLight = false
        in 7..18 -> isWeatherLight = true
    }
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(185.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.daily_weather_background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 10.dp, start = 20.dp
                    )
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "${weather.temperature}°",
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 64.sp,
                    color = Color.White
                )

                Text(
                    text = weather.time,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )


                Text(
                    text = countryInfo.fetchFullInfo(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

            }
            Column(
                modifier = Modifier.padding(start = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(160.dp), painter = painterResource(
                        id = if (isWeatherLight) weather.weatherConditionType.lightImageID
                        else weather.weatherConditionType.nightImageID
                    ), contentDescription = null
                )
                Text(
                    text = weather.weatherConditionType.descripition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}