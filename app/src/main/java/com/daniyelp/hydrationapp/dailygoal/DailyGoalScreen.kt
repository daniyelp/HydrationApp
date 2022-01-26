package com.daniyelp.hydrationapp.dailygoal

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable

@Composable
fun DailyGoalScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Daily Goal")
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    }
                }
            )
        }
    ) {

    }
}