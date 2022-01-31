package com.daniyelp.hydrationapp.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.daniyelp.hydrationapp.R

@Composable
fun BackgroundImage(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            modifier = modifier,
            painter = rememberImagePainter(R.drawable.leaf_background),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Surface(modifier = Modifier.fillMaxSize().alpha(0.4f)) {}
    }
}