package com.daniyelp.hydrationapp.quantity

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun QuantityScreen(
    state: QuantityContract.State,
    onSendEvent: (QuantityContract.Event) -> Unit,
    effects: Flow<QuantityContract.Effect>,
    onNavigationRequest: (QuantityContract.Effect.Navigation) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Hello")
                },
                navigationIcon = {
                    IconButton(onClick = { onSendEvent(QuantityContract.Event.Cancel )}) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { onSendEvent(QuantityContract.Event.Save) }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    }
                }
            )
        }
    ) {
        LaunchedEffect(effects) {
            effects.onEach { effect ->
                when (effect) {
                    is QuantityContract.Effect.Navigation -> {
                        onNavigationRequest(effect)
                    }
                }
            }.collect()
        }
    }
}