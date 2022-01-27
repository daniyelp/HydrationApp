package com.daniyelp.hydrationapp.home.history

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

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

    }
}