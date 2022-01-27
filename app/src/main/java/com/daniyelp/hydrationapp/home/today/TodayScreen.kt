package com.daniyelp.hydrationapp.home.today

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

@Composable
fun TodayScreen(
    state: TodayContract.State,
    onSendEvent: (TodayContract.Event) -> Unit,
    effects: Flow<TodayContract.Effect>,
    onNavigationRequest: (TodayContract.Effect.Navigation) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Today's progress")
                },
                actions = {
                    IconButton(onClick = { onSendEvent(TodayContract.Event.NavigateToSettings) }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) {
        LaunchedEffect(effects) {
            effects.onEach { effect ->
                when (effect) {
                    is TodayContract.Effect.Navigation -> {
                        onNavigationRequest(effect)
                    }
                }
            }.collect()
        }
    }
}