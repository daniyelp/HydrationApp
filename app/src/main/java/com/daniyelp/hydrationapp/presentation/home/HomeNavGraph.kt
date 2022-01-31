package com.daniyelp.hydrationapp.presentation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import soup.compose.material.motion.navigation.composable
import com.daniyelp.hydrationapp.presentation.home.history.HistoryScreen
import com.daniyelp.hydrationapp.presentation.home.today.TodayContract
import com.daniyelp.hydrationapp.presentation.home.today.TodayScreen
import com.daniyelp.hydrationapp.presentation.home.today.TodayViewModel
import com.daniyelp.hydrationapp.navigation.AppDestinations
import com.daniyelp.hydrationapp.presentation.home.history.HistoryViewModel
import soup.compose.material.motion.*
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController

@ExperimentalAnimationApi
fun NavGraphBuilder.homeComposable(mainNavController: NavController) {
    composable(route = AppDestinations.Home.route) {
        val homeNavController = rememberMaterialMotionNavController()
        BottomBarScreen(navController = homeNavController) {
            MaterialMotionNavHost(
                navController = homeNavController,
                startDestination = AppDestinations.Home.Today.route
            ) {
                composable(
                    route = AppDestinations.Home.Today.route,
                    enterMotionSpec = { initial, _ ->
                        when (initial.destination.route) {
                            AppDestinations.Settings.route -> materialSharedAxisZIn()
                            else -> materialSharedAxisXIn(slideDistance = -MotionConstants.DefaultSlideDistance * 2)
                        }
                    },
                    exitMotionSpec = { _, target ->
                        when (target.destination.route) {
                            AppDestinations.Settings.route -> materialElevationScaleOut()
                            else -> materialSharedAxisXOut(slideDistance = MotionConstants.DefaultSlideDistance * 2)
                        }
                    },
                ) {
                    val todayViewModel = hiltViewModel<TodayViewModel>()
                    TodayScreen(
                        state = todayViewModel.viewState.value,
                        onSendEvent = todayViewModel::setEvent,
                        effects = todayViewModel.effect,
                        onNavigationRequest = { navEffect ->
                            when (navEffect) {
                                TodayContract.Effect.Navigation.ToSettings -> {
                                    mainNavController.navigate(AppDestinations.Settings.route)
                                }
                            }
                        }
                    )
                }
                composable(
                    route = AppDestinations.Home.History.route,
                    enterMotionSpec = { _, _ ->
                        materialSharedAxisXIn(slideDistance = MotionConstants.DefaultSlideDistance * 2)
                    },
                    exitMotionSpec = { _, _ ->
                        materialSharedAxisXOut(slideDistance = -MotionConstants.DefaultSlideDistance * 2)
                    },
                ) {
                    val historyViewModel = hiltViewModel<HistoryViewModel>()
                    HistoryScreen(state = historyViewModel.viewState.value)
                }
            }
        }
    }
}

@Composable
private fun BottomBarScreen(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavController
) {
    val items = listOf(
        AppDestinations.Home.Today,
        AppDestinations.Home.History
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BottomNavigation(elevation = 0.dp) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(item.iconId), contentDescription = item.title) },
                label = { Text(text = item.title) },
                alwaysShowLabel = true,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onBackground,
                selected = navBackStackEntry?.destination?.route == item.route,
                onClick = {
                    if(navBackStackEntry?.destination?.route != item.route)
                        navController.navigate(item.route)
                },
            )
        }
    }
}