package com.daniyelp.hydrationapp.presentation.units

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun UnitsScreen(
    state: UnitsContract.State,
    onSendEvent: (UnitsContract.Event) -> Unit,
    effects: Flow<UnitsContract.Effect>,
    onNavigationRequest: (UnitsContract.Effect.Navigation) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Units")
                },
                navigationIcon = {
                    IconButton(onClick = { onSendEvent(UnitsContract.Event.NavigateUp) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        LaunchedEffect(effects) {
            effects.onEach { effect ->
                when (effect) {
                    is UnitsContract.Effect.Navigation -> {
                        onNavigationRequest(effect)
                    }
                }
            }.collect()
        }
    }
}