package com.daniyelp.hydrationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daniyelp.hydrationapp.home.homeNavGraph
import com.daniyelp.hydrationapp.navigation.AppDestinations
import com.daniyelp.hydrationapp.quantity.QuantityContract
import com.daniyelp.hydrationapp.quantity.QuantityScreen
import com.daniyelp.hydrationapp.quantity.QuantityViewModel
import com.daniyelp.hydrationapp.settings.SettingsContract
import com.daniyelp.hydrationapp.settings.SettingsScreen
import com.daniyelp.hydrationapp.settings.SettingsViewModel
import com.daniyelp.hydrationapp.ui.theme.HydrationAppTheme
import com.daniyelp.hydrationapp.units.UnitsContract
import com.daniyelp.hydrationapp.units.UnitsScreen
import com.daniyelp.hydrationapp.units.UnitsViewModel

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
    val settingsViewModel = viewModel<SettingsViewModel>()
    SettingsScreen(
        state = settingsViewModel.viewState.value,
        onSendEvent = settingsViewModel::setEvent,
        effects = settingsViewModel.effect,
        onNavigationRequest = { navEffect ->
            when (navEffect) {
                SettingsContract.Effect.Navigation.Up -> {
                    navController.popBackStack()
                }
                SettingsContract.Effect.Navigation.ToDailyGoal -> {
                    navController.navigate(AppDestinations.DailyGoal.route)
                }
                SettingsContract.Effect.Navigation.ToUnits -> {
                    navController.navigate(AppDestinations.Units.route)
                }
                is SettingsContract.Effect.Navigation.ToContainer -> {
                    navController.navigate(AppDestinations.Container.route)
                }
            }
        }
    )
}

@Composable
private fun UnitsScreenDestination(navController: NavController) {
    val unitsViewModel = viewModel<UnitsViewModel>()
    UnitsScreen(
        state = unitsViewModel.viewState.value,
        onSendEvent = unitsViewModel::setEvent,
        effects = unitsViewModel.effect,
        onNavigationRequest = { navEffect ->
            when (navEffect) {
                UnitsContract.Effect.Navigation.Up -> {
                    navController.popBackStack()
                }
            }
        }
    )
}

@Composable
private fun DailyGoalScreenDestination(navController: NavController) {
    val quantityViewModel = viewModel<QuantityViewModel>()
    QuantityScreen(
        state = quantityViewModel.viewState.value,
        onSendEvent = quantityViewModel::setEvent,
        effects = quantityViewModel.effect,
        onNavigationRequest = { navEffect ->
            when (navEffect) {
                QuantityContract.Effect.Navigation.Up -> {
                    navController.popBackStack()
                }
            }
        }
    )
}

@Composable
private fun ContainerScreenDestination(navController: NavController) {
    val quantityViewModel = viewModel<QuantityViewModel>()
    QuantityScreen(
        state = quantityViewModel.viewState.value,
        onSendEvent = quantityViewModel::setEvent,
        effects = quantityViewModel.effect,
        onNavigationRequest = { navEffect ->
            when (navEffect) {
                QuantityContract.Effect.Navigation.Up -> {
                    navController.popBackStack()
                }
            }
        }
    )
}