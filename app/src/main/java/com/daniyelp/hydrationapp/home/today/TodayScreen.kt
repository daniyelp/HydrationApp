package com.daniyelp.hydrationapp.home.today

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable

@Composable
fun TodayScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Today's progress")
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) {

    }
}