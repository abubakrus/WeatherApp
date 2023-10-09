package com.example.weatherapp.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.usecases.FetchWeathersUseCases
import com.example.weatherapp.presentation.models.CountryInfo
import com.example.weatherapp.presentation.models.WeatherUi
import com.example.weatherapp.presentation.utils.mappers.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainScreenUiState {

    object Loading : MainScreenUiState()

    data class Loaded(
        val weather: WeatherUi,
        val countryInfo: CountryInfo
    ) : MainScreenUiState()

    data class Error(
        val message: String
    ) : MainScreenUiState()
}


@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchWeathersUseCases: FetchWeathersUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Loading)
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()


    fun fetchCurrentWeathers() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.tryEmit(MainScreenUiState.Loading)
            val weatherParams = fetchWeathersUseCases.invoke()
            val weather = weatherParams.first.toUi()

            if (weather.isUnKnown()) {
                _uiState.tryEmit(MainScreenUiState.Error("Что то пашло не так "))
            } else {
                _uiState.tryEmit(MainScreenUiState.Loaded(weather, weatherParams.second))
            }
        }
    }
}