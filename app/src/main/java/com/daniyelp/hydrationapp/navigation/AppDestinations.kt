package com.daniyelp.hydrationapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.ui.graphics.vector.ImageVector

object AppDestinations {
    sealed class Screen(val route: String)
    object Home: Screen("home") {
        sealed class HomeScreen(route: String, val icon: ImageVector, val title: String): Screen(route)
        object History: HomeScreen("history", Icons.Default.History, "History")
        object Today: HomeScreen("today", Icons.Default.LocalDrink, "Today")
    }
    object Settings: Screen("settings")
    object DailyGoal: Screen("dailygoal")
    object Container: Screen("container")
}