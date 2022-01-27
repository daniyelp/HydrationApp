package com.daniyelp.hydrationapp.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun SettingsScreen(
    state: SettingsContract.State,
    onSendEvent: (SettingsContract.Event) -> Unit,
    effects: Flow<SettingsContract.Effect>,
    onNavigationRequest: (SettingsContract.Effect.Navigation) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(onClick = { onSendEvent(SettingsContract.Event.NavigateUp) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        LaunchedEffect(effects) {
            effects.onEach { effect ->
                when (effect) {
                    is SettingsContract.Effect.Navigation -> {
                        onNavigationRequest(effect)
                    }
                }
            }.collect()
        }

        Column {
            Button(onClick = { onSendEvent(SettingsContract.Event.SelectUnits) }) {
                Text("Units")
            }
            Button(onClick = { onSendEvent(SettingsContract.Event.SelectDailyGoal) }) {
                Text("Daily Goal")
            }
            Button(onClick = { onSendEvent(SettingsContract.Event.SelectContainer(1)) }) {
                Text("Container 1")
            }
            Button(onClick = { onSendEvent(SettingsContract.Event.SelectContainer(2)) }) {
                Text("Container 2")
            }
            Button(onClick = { onSendEvent(SettingsContract.Event.SelectContainer(3)) }) {
                Text("Container 3")
            }
        }
    }
}