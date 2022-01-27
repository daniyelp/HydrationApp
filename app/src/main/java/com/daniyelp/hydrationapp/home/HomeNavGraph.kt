package com.daniyelp.hydrationapp.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.daniyelp.hydrationapp.home.history.HistoryScreen
import com.daniyelp.hydrationapp.home.today.TodayContract
import com.daniyelp.hydrationapp.home.today.TodayScreen
import com.daniyelp.hydrationapp.home.today.TodayViewModel
import com.daniyelp.hydrationapp.navigation.AppDestinations

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation(
        startDestination = AppDestinations.Home.Today.route,
        route = AppDestinations.Home.route
    ) {
        composable(route = AppDestinations.Home.Today.route) {
            val todayViewModel = viewModel<TodayViewModel>()
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
        composable(route = AppDestinations.Home.History.route) {
            HomeScreen(navController) {
                HistoryScreen()
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
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                alwaysShowLabel = true,
                selected = navController.currentBackStackEntry?.destination?.route == item.route,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}
