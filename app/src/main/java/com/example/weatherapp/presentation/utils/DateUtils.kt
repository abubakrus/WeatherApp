package com.example.weatherapp.presentation.utils

import java.util.Date

enum class DayType {
    LIGHT,
    NIGHT,
    SUNSET
}

fun fetchDayType(): DayType {
    val current = Date().hours
    return when (current) {
        in 20..23 -> DayType.NIGHT
        in 0..6 -> DayType.NIGHT
        in 8..18 -> DayType.LIGHT
        7 -> DayType.SUNSET
        19 -> DayType.SUNSET
        else -> DayType.LIGHT
    }
}

