package com.daniyelp.hydrationapp.presentation.home.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daniyelp.hydrationapp.util.dateToString

@Composable
fun HistoryScreen(state: HistoryContract.State) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "History")
                },
                elevation = 0.dp
            )
        }
    ) {
        if(state.dayProgressList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Empty")
            }
        } else {
            LazyColumn() {
                item {
                    Text(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp),
                        text = "Here you can see your hydration data for the last 30 days",
                        textAlign = TextAlign.Center,
                        fontSize = MaterialTheme.typography.body1.fontSize
                    )
                }
                item {
                    BarChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        barDataList = state.dayProgressList.map {
                            BarData(
                                reached = it.quantity.getValue(state.unit),
                                goal = it.goal.getValue(state.unit),
                                text = dateToString(it.date, "dd.MM")
                            )
                        }
                    )
                }
                itemsIndexed(state.dayProgressList) { index, dayProgress ->
                    DayProgressCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        dayProgress = dayProgress,
                        unit = state.unit
                    )
                    if (index != state.dayProgressList.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

