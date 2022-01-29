package com.daniyelp.hydrationapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniyelp.hydrationapp.presentation.home.homeNavGraph
import com.daniyelp.hydrationapp.navigation.AppDestinations
import com.daniyelp.hydrationapp.presentation.quantity.*
import com.daniyelp.hydrationapp.presentation.settings.SettingsContract
import com.daniyelp.hydrationapp.presentation.settings.SettingsScreen
import com.daniyelp.hydrationapp.presentation.settings.SettingsViewModel
import com.daniyelp.hydrationapp.presentation.theme.HydrationAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
                    composable(AppDestinations.DailyGoal.route) {
                        DailyGoalScreenDestination(navController)
                    }
                    composable(
                        route = AppDestinations.Container.route,
                        arguments = listOf(
                            navArgument(name = "containerId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val containerId = backStackEntry.arguments?.getInt("containerId")!!
                        ContainerScreenDestination(containerId, navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsScreenDestination(navController: NavController) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
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
                is SettingsContract.Effect.Navigation.ToContainer -> {
                    navController.navigate(AppDestinations.Container.createRoute(navEffect.containerId))
                }
            }
        }
    )
}

@Composable
private fun DailyGoalScreenDestination(navController: NavController) {
    val updateDailyGoalViewModel = hiltViewModel<UpdateDailyGoalViewModel>()
    UpdateQuantityScreen(
        title = "Daily Goal",
        description = "Here you can set your hydration goal based on your preferred unit of measurement",
        state = updateDailyGoalViewModel.viewState.value,
        onSendEvent = updateDailyGoalViewModel::setEvent,
        effects = updateDailyGoalViewModel.effect,
        onNavigationRequest = { navEffect ->
            when (navEffect) {
                UpdateQuantityContract.Effect.Navigation.Up -> {
                    navController.popBackStack()
                }
            }
        }
    )
}

@Composable
private fun ContainerScreenDestination(containerId: Int, navController: NavController) {
    val updateContainerQuantityViewModel = hiltViewModel<UpdateContainerQuantityViewModel>()
    UpdateQuantityScreen(
        title = "Container $containerId",
        description = "Here you can specify your container size so it would be easier for you to enter your daily liquid intake",
        state = updateContainerQuantityViewModel.viewState.value,
        onSendEvent = updateContainerQuantityViewModel::setEvent,
        effects = updateContainerQuantityViewModel.effect,
        onNavigationRequest = { navEffect ->
            when (navEffect) {
                UpdateQuantityContract.Effect.Navigation.Up -> {
                    navController.popBackStack()
                }
            }
        }
    )
}