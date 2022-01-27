package com.daniyelp.hydrationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daniyelp.hydrationapp.home.homeNavGraph
import com.daniyelp.hydrationapp.navigation.AppDestinations
import com.daniyelp.hydrationapp.quantity.QuantityScreen
import com.daniyelp.hydrationapp.settings.SettingsScreen
import com.daniyelp.hydrationapp.ui.theme.HydrationAppTheme
import com.daniyelp.hydrationapp.units.UnitsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HydrationAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppDestinations.Home.route
                ) {
                    homeNavGraph(navController)
                    composable(AppDestinations.Settings.route) {
                        SettingsScreenDestination(navController)
                    }
                    composable(AppDestinations.Units.route) {
                        UnitsScreenDestination(navController)
                    }
                    composable(AppDestinations.DailyGoal.route) {
                        DailyGoalScreenDestination(navController)
                    }
                    composable(AppDestinations.Container.route) {
                        ContainerScreenDestination(navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsScreenDestination(navController: NavController) {
    SettingsScreen()
}

@Composable
private fun UnitsScreenDestination(navController: NavController) {
    UnitsScreen()
}

@Composable
private fun DailyGoalScreenDestination(navController: NavController) {
    QuantityScreen(title = "Daily Goal", description = "")
}

@Composable
private fun ContainerScreenDestination(navController: NavController) {
    QuantityScreen(title = "Container", description = "")
}