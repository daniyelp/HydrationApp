package com.daniyelp.hydrationapp.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.daniyelp.hydrationapp.R

@Composable
fun Leaves(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
        Box(modifier = modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.background_leaf),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.4f)
            ) {}
            content()
        }
}