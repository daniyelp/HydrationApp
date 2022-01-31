package com.daniyelp.hydrationapp.presentation.quantity

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daniyelp.hydrationapp.presentation.common.BackgroundImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun UpdateQuantityScreen(
    title: String,
    description: String,
    state: UpdateQuantityContract.State,
    onSendEvent: (UpdateQuantityContract.Event) -> Unit,
    effects: Flow<UpdateQuantityContract.Effect>,
    onNavigationRequest: (UpdateQuantityContract.Effect.Navigation) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = {
                    IconButton(onClick = { onSendEvent(UpdateQuantityContract.Event.Cancel) }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = { onSendEvent(UpdateQuantityContract.Event.Save) }) {
                        Text(
                            text = "SAVE",
                            color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.high)
                        )
                    }
                },
                elevation = 0.dp
            )
        }
    ) {
        LaunchedEffect(effects) {
            effects.onEach { effect ->
                when (effect) {
                    is UpdateQuantityContract.Effect.Navigation -> {
                        focusManager.clearFocus()
                        onNavigationRequest(effect)
                    }
                }
            }.collect()
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Box(Modifier.fillMaxSize()) {
            BackgroundImage(Modifier.fillMaxSize())
            Text(
                modifier = Modifier
                    .widthIn(max = screenWidth * 3 / 4)
                    .padding(top = 24.dp)
                    .align(Alignment.TopCenter),
                text = description,
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = state.quantityInput,
                    onValueChange = { textFieldValue ->
                        onSendEvent(
                            UpdateQuantityContract.Event.SetQuantity(
                                textFieldValue
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = with(LocalDensity.current) { 48.dp.toSp() },
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    cursorBrush = Brush.verticalGradient(
                        0.00f to Color.Transparent,
                        0.20f to Color.Transparent,
                        0.20f to MaterialTheme.colors.primary,
                        0.85f to MaterialTheme.colors.primary,
                        0.85f to Color.Transparent,
                        1.00f to Color.Transparent
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .width(128.dp)
                                .border(
                                    2.dp,
                                    MaterialTheme.colors.primary,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            innerTextField()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${state.unit.toString()} (${state.unit.toShortString()})",
                    fontSize = MaterialTheme.typography.h6.fontSize
                )
            }
        }
    }
}