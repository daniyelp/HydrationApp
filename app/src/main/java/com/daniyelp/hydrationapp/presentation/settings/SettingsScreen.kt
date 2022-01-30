package com.daniyelp.hydrationapp.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daniyelp.hydrationapp.data.model.QuantityUnit
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
            SettingsOptionsItem(
                title = "Units",
                subtitle = state.unit.toString(),
                items = QuantityUnit.getUnits().map { it.toString() },
                selectedItem = state.unit.toString(),
                onItemSelected = { onSendEvent(SettingsContract.Event.SelectUnit(QuantityUnit.fromString(it)))}
            )
            Divider()
            SettingsSimpleItem(
                title = "Daily Goal",
                subtitle = "${state.dailyGoal.getValue(state.unit)} ${state.unit.toShortString()}",
                onClick = { onSendEvent(SettingsContract.Event.SelectDailyGoal) }
            )
            SettingsCategory(text = "Containers")
            state.containers.forEach {
                SettingsSimpleItem(
                    title = "Container ${it.id}",
                    subtitle = "${it.quantity.getValue(state.unit)} ${state.unit.toShortString()}",
                    onClick = { onSendEvent(SettingsContract.Event.SelectContainer(it.id)) }
                )
                Divider()
            }
        }
    }
}

@Composable
fun SettingsCategory(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
        text = text,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.subtitle2
    )
}

@Composable
fun SettingsSimpleItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1
        )
        if (subtitle.isNotEmpty())
            Text(
                text = subtitle,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(0.5f)
            )
    }
}

@Composable
fun SettingsOptionsItem(
    title: String,
    subtitle: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    if(isDialogOpen) {
        SingleChoiceItemsDialog(
            onDismissRequest = { isDialogOpen = false},
            onClose = { isDialogOpen = false },
            title = "Preferred unit",
            items = items,
            selectedItem = selectedItem,
            onItemSelected = onItemSelected,
            onCancel = { isDialogOpen = false }
        )
    }
    SettingsSimpleItem(
        title = title,
        subtitle = subtitle,
        onClick = { isDialogOpen = true }
    )
}

@Composable
fun SingleChoiceItemsDialog(
    onDismissRequest: () -> Unit,
    onClose: () -> Unit,
    title: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = RoundedCornerShape(8.dp)) {
            Column {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp),
                    text = title,
                    style = MaterialTheme.typography.h5
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable(
                                    onClick = {
                                        onItemSelected(item)
                                        onClose()
                                    }
                                )
                                .padding(horizontal = 32.dp),
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedItem == item,
                                onClick = {
                                    onItemSelected(item)
                                    onClose()
                                }
                            )
                            Text(text = item)
                        }
                    }
                }
                TextButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp),
                    onClick = onCancel
                ) {
                    Text(text = "CANCEL")
                }
            }
        }
    }
}