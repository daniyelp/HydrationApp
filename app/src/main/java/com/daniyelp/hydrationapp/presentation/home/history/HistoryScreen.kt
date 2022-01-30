package com.daniyelp.hydrationapp.presentation.home.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.dateToString

@Composable
fun HistoryScreen(state: HistoryContract.State) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "History")
                }
            )
        }
    ) {
        LazyColumn {
            item {
                Text(text = "History", fontSize = 88.sp)
            }
            itemsIndexed(state.dayProgressList) { index, dayProgress ->
                DayProgressCard(dayProgress, state.unit)
                if (index != state.dayProgressList.size - 1) {
                    Divider(modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun DayProgressCard(dayProgress: DayProgress, unit: QuantityUnit) {
    val quantityValue by derivedStateOf { dayProgress.quantity.getValue(unit) }
    val goalValue by derivedStateOf { dayProgress.goal.getValue(unit) }
    val percentCompleted by derivedStateOf { quantityValue * 100 / if (goalValue != 0) goalValue else 1 }
    val shortUnit by derivedStateOf { unit.toShortString() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            val colorHigh = MaterialTheme.colors.onBackground.copy(ContentAlpha.high)
            val colorMedium = MaterialTheme.colors.onBackground.copy(ContentAlpha.medium)
            val smallerFont = MaterialTheme.typography.body1.fontSize
            val biggerFont = MaterialTheme.typography.h5.fontSize
            Text(
                text = dateToString(dayProgress.date, "EEEE, dd MMMM"),
                style = TextStyle(
                    color = colorMedium,
                    fontSize = smallerFont
                )
            )
            Text(
                text = "$quantityValue $shortUnit",
                style = TextStyle(
                    color = colorHigh,
                    fontSize = biggerFont
                )
            )
            Row {
                Text(
                    text = "${percentCompleted}% ",
                    style = TextStyle(
                        color = colorHigh,
                        fontSize = smallerFont
                    )
                )
                Text(
                    text = "out of $goalValue $shortUnit Goal",
                    style = TextStyle(
                        color = colorMedium,
                        fontSize = smallerFont
                    )
                )
            }
        }
        if(percentCompleted >= 100) {
            Icon(
                modifier = Modifier.size(48.dp).align(Alignment.CenterVertically),
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

