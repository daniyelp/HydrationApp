package com.daniyelp.hydrationapp.presentation.home.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

data class BarData(val reached: Int, val goal: Int, val text: String)

@Composable
fun BarChart(
    barDataList: List<BarData>,
    modifier: Modifier = Modifier,
    solidColorCompleted: Color = MaterialTheme.colors.primary,
    solidColorUncompleted: Color = MaterialTheme.colors.secondary,
    solidColorDifference: Color = MaterialTheme.colors.onBackground,
    alphaColorDifference: Float = 0.2f,
    textColor: Color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
    textFontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
    horizontalLinesNumber: Int = 5,
    horizontalLineThickness: Dp = 2.dp,
    squareVisibleMaxSizeThreshold: Dp = 8.dp
) {
    val localDensity = LocalDensity.current
    fun spToDp(sp: TextUnit) = with(localDensity) { sp.toDp() }
    fun pxToDp(px: Float) = with(localDensity) { px.toDp() }

    val canvasTopPadding = spToDp(textFontSize)
    var canvasHeightPx by remember { mutableStateOf(0f) }
    var canvasWidthPx by remember { mutableStateOf(0f) }
    var barWidthPx by remember { mutableStateOf(0f) }
    val canvasHeight by derivedStateOf { pxToDp(canvasHeightPx) }
    val barWidth by derivedStateOf { pxToDp(barWidthPx) }
    val maxValue = barDataList.flatMap { listOf(it.goal, it.reached) }.maxOrNull()!!
    val horizontalLinesYFractional =
        if (horizontalLinesNumber > 0)
            listOf(0f) + (1 until horizontalLinesNumber).map { 1f / horizontalLinesNumber * it }
        else
            emptyList()
    val leftTextValues = (1..horizontalLinesNumber).map { maxValue / it }

    @Composable
    fun ChartText(
        text: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = textColor,
            fontSize = textFontSize
        )
    }

    @Composable
    fun VerticalBarsCanvas(modifier: Modifier = Modifier) {
        Canvas(modifier) {
            canvasHeightPx = size.height
            canvasWidthPx = size.width
            barWidthPx = canvasWidthPx / (barDataList.size * 2 - 1)
            val horizontalLinesY =
                horizontalLinesYFractional.map { fraction -> fraction * canvasHeightPx }
            //draw the horizontal lines
            horizontalLinesY.forEach { y ->
                drawLine(
                    color = solidColorDifference,
                    start = Offset(0f, y),
                    end = Offset(canvasWidthPx, y),
                    strokeWidth = horizontalLineThickness.toPx(),
                    alpha = alphaColorDifference
                )
            }
            //draw the vertical bars
            barDataList.forEachIndexed { index, barData ->
                if (barData.reached < barData.goal) {
                    val goalBarHeightPx = barData.goal.toFloat() / maxValue * canvasHeightPx
                    val reachedBarHeightPx = barData.reached.toFloat() / maxValue * canvasHeightPx
                    drawRect(
                        color = solidColorDifference,
                        alpha = alphaColorDifference,
                        topLeft = Offset(2 * barWidthPx * index, canvasHeightPx - goalBarHeightPx),
                        size = Size(barWidthPx, goalBarHeightPx)
                    )
                    drawRect(
                        color = solidColorUncompleted,
                        topLeft = Offset(
                            2 * barWidthPx * index,
                            canvasHeightPx - reachedBarHeightPx
                        ),
                        size = Size(barWidthPx, reachedBarHeightPx)
                    )
                } else {
                    val barHeightPx = barData.reached.toFloat() / maxValue * canvasHeightPx
                    drawRect(
                        color = solidColorCompleted,
                        topLeft = Offset(2 * barWidthPx * index, canvasHeightPx - barHeightPx),
                        size = Size(barWidthPx, barHeightPx)
                    )
                }
            }
        }
    }

    @Composable
    fun SquaresCanvas(modifier: Modifier = Modifier) {
        Canvas(modifier) {
            barDataList.forEachIndexed { index, _ ->
                drawRect(
                    color = solidColorDifference,
                    alpha = if(index == 0 || index == barDataList.size - 1) 1f else alphaColorDifference,
                    topLeft = Offset(2 * barWidthPx * index, 0f),
                    size = Size(barWidthPx, barWidthPx)
                )
            }
        }
    }

    Row(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.TopEnd
        ) {
            //draw the left side texts (without the "0")
            horizontalLinesYFractional.forEachIndexed { index, fraction ->
                ChartText(
                    modifier = Modifier.offset(y = canvasHeight * fraction + canvasTopPadding * (1 / 4f)),
                    text = leftTextValues[index].toString(),
                )
            }
            //draw the left side "0" text
            ChartText(
                modifier = Modifier.offset(y = canvasHeight),
                text = "0",
            )
        }
        Spacer(modifier = Modifier.width(1.dp))
        Column {
            VerticalBarsCanvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(top = canvasTopPadding)
            )
            Spacer(modifier = Modifier.height(barWidth))
            if (barWidth < squareVisibleMaxSizeThreshold) {
                SquaresCanvas(
                    modifier = Modifier
                        .height(barWidth)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(barWidth))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ChartText(barDataList.firstOrNull()?.text ?: "")
                ChartText(barDataList.lastOrNull()?.text ?: "")
            }
        }
    }
}