package com.daniyelp.hydrationapp.presentation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import soup.compose.material.motion.navigation.composable
import androidx.navigation.navigation
import com.daniyelp.hydrationapp.presentation.home.history.HistoryScreen
import com.daniyelp.hydrationapp.presentation.home.today.TodayContract
import com.daniyelp.hydrationapp.presentation.home.today.TodayScreen
import com.daniyelp.hydrationapp.presentation.home.today.TodayViewModel
import com.daniyelp.hydrationapp.navigation.AppDestinations
import com.daniyelp.hydrationapp.presentation.home.history.HistoryViewModel
import soup.compose.material.motion.*

@ExperimentalAnimationApi
fun materialSharedAxisXIn(
    forward: Boolean,
    slideDistance: Dp = MotionConstants.DefaultSlideDistance,
    durationMillis: Int = MotionConstants.motionDurationLong1,
): EnterMotionSpec = EnterMotionSpec(
    transition = { _, density ->
        soup.compose.material.motion.animation.materialSharedAxisXIn(
            forward = forward,
            density = density,
            slideDistance = slideDistance,
            durationMillis = durationMillis
        )
    }
)

@ExperimentalAnimationApi
fun materialSharedAxisXOut(
    forward: Boolean,
    slideDistance: Dp = MotionConstants.DefaultSlideDistance,
    durationMillis: Int = MotionConstants.motionDurationLong1,
): ExitMotionSpec = ExitMotionSpec(
    transition = { _, density ->
        soup.compose.material.motion.animation.materialSharedAxisXOut(
            forward = forward,
            density = density,
            slideDistance = slideDistance,
            durationMillis = durationMillis
        )
    }
)

@ExperimentalAnimationApi
fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation(
        startDestination = AppDestinations.Home.Today.route,
        route = AppDestinations.Home.route
    ) {
        composable(
            route = AppDestinations.Home.Today.route,
            enterMotionSpec = { initial, _ ->
                when(initial.destination.route) {
                    AppDestinations.Settings.route -> materialSharedAxisZIn()
                    else -> materialSharedAxisXIn(forward = true)
                }
            },
            exitMotionSpec = { _, target ->
                when(target.destination.route) {
                    AppDestinations.Settings.route ->  materialElevationScaleOut()
                    else -> materialSharedAxisXOut(forward = true)
                }
            },
        ) {
            val todayViewModel = hiltViewModel<TodayViewModel>()
            HomeScreen(navController) {
                TodayScreen(
                    state = todayViewModel.viewState.value,
                    onSendEvent = todayViewModel::setEvent,
                    effects = todayViewModel.effect,
                    onNavigationRequest = { navEffect ->
                        when(navEffect) {
                            TodayContract.Effect.Navigation.ToSettings -> {
                                navController.navigate(AppDestinations.Settings.route)
                            }
                        }
                    }
                )
            }
        }
        composable(
            route = AppDestinations.Home.History.route,
            enterMotionSpec = { _, _ ->
                materialSharedAxisXIn(forward = false)
            },
            exitMotionSpec = { _, _ ->
                materialSharedAxisXOut(forward = false)
            },
        ) {
            val historyViewModel = hiltViewModel<HistoryViewModel>()
            HomeScreen(navController) {
                HistoryScreen(state = historyViewModel.viewState.value)
            }
        }
    }
}

@Composable
private fun HomeScreen(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { HomeBottomBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Composable
private fun HomeBottomBar(
    navController: NavController
) {
    val items = listOf(
        AppDestinations.Home.Today,
        AppDestinations.Home.History
    )
    BottomNavigation {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(item.iconId), contentDescription = item.title) },
                label = { Text(text = item.title) },
                alwaysShowLabel = true,
                selected = navController.currentBackStackEntry?.destination?.route == item.route,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}
