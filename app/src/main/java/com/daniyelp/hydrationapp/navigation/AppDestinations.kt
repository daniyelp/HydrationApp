package com.daniyelp.hydrationapp.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.ui.graphics.vector.ImageVector
import com.daniyelp.hydrationapp.R

object AppDestinations {
    sealed class Screen(val route: String)
    object Home: Screen("home") {
        sealed class HomeScreen(route: String, @DrawableRes val iconId: Int, val title: String): Screen(route)
        object History: HomeScreen("history", R.drawable.ic_history, "History")
        object Today: HomeScreen("today", R.drawable.ic_glass, "Today")
    }
    object Settings: Screen("settings")
    object DailyGoal: Screen("dailygoal")
    object Container: Screen("container/{containerId}") {
        fun createRoute(containerId: Int) = "container/$containerId"
    }
}