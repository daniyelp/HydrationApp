package com.daniyelp.hydrationapp.units

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun UnitsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Units")
                }
            )
        }
    ) {

    }
}