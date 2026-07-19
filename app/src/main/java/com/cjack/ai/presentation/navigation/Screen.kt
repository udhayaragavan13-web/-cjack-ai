package com.cjack.ai.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Validation : Screen("validation")
    object Dashboard : Screen("dashboard")
    object Ambulance : Screen("ambulance")
    object History : Screen("history")
    object Settings : Screen("settings")
}
