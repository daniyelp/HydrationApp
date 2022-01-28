package com.daniyelp.hydrationapp.quantity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
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
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .widthIn(max = screenWidth * 3 / 4)
                    .padding(top = 24.dp)
                    .align(Alignment.TopCenter),
                text = "Here you can set your hydration goal based on your preferred unit of measurement",
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.widthIn(min = 96.dp),
                    value = state.quantityInput,
                    onValueChange = { textFieldValue -> onSendEvent(QuantityContract.Event.SetQuantity(textFieldValue)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "milliliters (ml)")
            }
        }
    }
}