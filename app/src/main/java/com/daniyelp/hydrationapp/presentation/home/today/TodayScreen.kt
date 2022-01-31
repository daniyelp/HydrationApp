package com.daniyelp.hydrationapp.presentation.home.today

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.BlurTransformation
import com.daniyelp.hydrationapp.R
import com.daniyelp.hydrationapp.presentation.common.BackgroundImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

@Composable
fun TodayScreen(
    state: TodayContract.State,
    onSendEvent: (TodayContract.Event) -> Unit,
    effects: Flow<TodayContract.Effect>,
    onNavigationRequest: (TodayContract.Effect.Navigation) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val currentQuantityValue by derivedStateOf { state.currentQuantity.getValue(state.unit) }
    val dailyGoalValue by derivedStateOf { state.dailyGoal.getValue(state.unit) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Today's progress")
                },
                actions = {
                    IconButton(onClick = { onSendEvent(TodayContract.Event.NavigateToSettings) }) {
                        Icon(
                            imageVector  = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.high)
                        )
                    }
                },
                elevation = 0.dp
            )
        }
    ) {
        LaunchedEffect(effects) {
            effects.onEach { effect ->
                when (effect) {
                    is TodayContract.Effect.Navigation -> {
                        onNavigationRequest(effect)
                    }
                }
            }.collect()
        }
        Box(modifier = Modifier.fillMaxSize()) {
            BackgroundImage(modifier = Modifier.fillMaxSize())
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${currentQuantityValue * 100 / if (dailyGoalValue != 0) dailyGoalValue else 1}%",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.h3.fontSize,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "of $dailyGoalValue ${state.unit.toShortString()} Goal"
                )
                Spacer(modifier = Modifier.height(8.dp))
                val fillRatio by derivedStateOf {
                    (currentQuantityValue / if (dailyGoalValue != 0) dailyGoalValue.toFloat() else 1f).coerceIn(
                        0f,
                        1f
                    )
                }
                Glass(
                    topWidth = screenWidth / 2,
                    fillRatio = fillRatio,
                    quantity = "$currentQuantityValue ${state.unit.toShortString()}"
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.containers.forEach { container ->
                        Button(
                            onClick = {
                                onSendEvent(
                                    TodayContract.Event.SelectContainer(
                                        container.id
                                    )
                                )
                            }
                        ) {
                            Text(text = "${container.quantity.getValue(state.unit)} ${state.unit.toShortString()}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.widthIn(max = screenWidth * 3 / 4),
                    text = "Happy you're back to track your healthy habit of staying hydrated",
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.body1.fontSize
                )
            }
        }
    }
}

@Composable
private fun Glass(
    topWidth: Dp,
    borderWidth: Dp = 8.dp,
    bottomWidthToTopWidthRatio: Float = 0.62f,
    topWidthToHeightRatio: Float = 0.75f,
    fillRatio: Float,
    quantity: String
) {
    Surface(
        modifier = Modifier
            .width(topWidth)
            .height(topWidth * (1 / topWidthToHeightRatio)),
        shape = GlassShape(bottomWidthToTopWidthRatio),
        border = BorderStroke(borderWidth, MaterialTheme.colors.onBackground),
        color = Color.Transparent,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fillRatio)
                    .align(Alignment.BottomCenter),
                color = Color.White.copy(alpha = 0.5f),
                content = {}
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                text = quantity
            )
        }
    }
}

private class GlassShape(val bottomWidthToTopWidthRatio: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(
        path = Path().apply {
            val bottomWidth = size.width * bottomWidthToTopWidthRatio
            val bottomToTopDpDiff = size.width - bottomWidth
            reset()
            lineTo(x = size.width, y = 0f)
            lineTo(x = size.width - bottomToTopDpDiff / 2, y = size.height)
            lineTo(x = bottomToTopDpDiff / 2, y = size.height)
            close()
        }
    )
}