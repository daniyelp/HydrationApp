package com.daniyelp.hydrationapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.daniyelp.hydrationapp.presentation.home.homeNavGraph
import com.daniyelp.hydrationapp.navigation.AppDestinations
import com.daniyelp.hydrationapp.presentation.quantity.*
import com.daniyelp.hydrationapp.presentation.settings.SettingsContract
import com.daniyelp.hydrationapp.presentation.settings.SettingsScreen
import com.daniyelp.hydrationapp.presentation.settings.SettingsViewModel
import com.daniyelp.hydrationapp.presentation.theme.HydrationAppTheme
import dagger.hilt.android.AndroidEntryPoint
import soup.compose.material.motion.*
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@AndroidEntryPoint
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            HydrationAppTheme {
                val navController = rememberMaterialMotionNavController()
                MaterialMotionNavHost(
                    navController = navController,
                    startDestination = AppDestinations.Home.route
                ) {
                    homeNavGraph(navController)
                    composable(
                        route = AppDestinations.Settings.route,
                        enterMotionSpec = { initial, _ ->
                            when(initial.destination.route) {
                                AppDestinations.Container.route, AppDestinations.DailyGoal.route -> materialElevationScaleIn()
                                else -> translateYIn({ it }, 250)
                            }
                        },
                        exitMotionSpec = { _, target ->
                            when(target.destination.route) {
                                AppDestinations.Container.route, AppDestinations.DailyGoal.route -> materialElevationScaleOut()
                                else -> translateYOut({ it }, 250)
                            }
                        }
                    ) {
                        SettingsScreenDestination(navController)
                    }
                    composable(
                        route = AppDestinations.DailyGoal.route,
                        enterMotionSpec = { _, _ ->
                            materialSharedAxisZIn()
                        },
                        exitMotionSpec = { _, _ ->
                            materialSharedAxisZOut()
                        }
                    ) {
                        DailyGoalScreenDestination(navController)
                    }
                    composable(
                        route = AppDestinations.Container.route,
                        enterMotionSpec = { _, _ ->
                            materialSharedAxisZIn()
                        },
                        exitMotionSpec = { _, _ ->
                            materialSharedAxisZOut()
                        },
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