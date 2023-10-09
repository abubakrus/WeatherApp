package com.example.weatherapp.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String

    val routeWithArgs: String
}

object MainScreenDestination : Destination {
    override val route: String = "main_screen"

    override val routeWithArgs: String = route
}

object WeatherListScreenDestination : Destination {
    val argKey = "weatherId"

    override val route: String = "weather_list_screen"

    override val routeWithArgs: String = "$route/{${argKey}}"

    val arguments = listOf(navArgument("weatherId") { type = NavType.StringType })

}