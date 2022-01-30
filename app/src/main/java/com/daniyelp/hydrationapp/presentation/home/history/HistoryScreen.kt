package com.daniyelp.hydrationapp.presentation.home.history

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "History")
                }
            )
        }
    ) {
        Text(text = "History", fontSize = 88.sp)
    }
}