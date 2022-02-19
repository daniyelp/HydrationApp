package com.daniyelp.hydrationapp.presentation.home.history

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

data class BarData(val reached: Int, val goal: Int, val text: String, val unit: String)

@ExperimentalComposeUiApi
@Composable
fun BarChart(
    barDataList: List<BarData>,
    modifier: Modifier = Modifier,
    solidColorCompleted: Color = MaterialTheme.colors.primary,
    solidColorUncompleted: Color = MaterialTheme.colors.secondary,
    solidColorDifference: Color = MaterialTheme.colors.onBackground,
    onColorDifference: Color = MaterialTheme.colors.background,
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
        //var highlight by remember { mutableStateOf(false) }
        data class Rect(
            val color: Color,
            val alpha: Float = 1f,
            val topLeft: Offset,
            val size: Size,
            val barData: BarData? = null
        )
        fun DrawScope.drawRect(rect: Rect) {
            drawRect(
                color = rect.color,
                alpha = rect.alpha,
                topLeft = rect.topLeft,
                size = rect.size
            )
        }
        var progressVerticalBars by remember { mutableStateOf(emptyList<Rect>()) }
        var progressVerticalBarsOriginal by remember { mutableStateOf(emptyList<Rect>()) }
        var goalVerticalBars by remember { mutableStateOf(emptyList<Rect>()) }
        var highlightedProgressVerticalBar by remember { mutableStateOf<Rect?>(null) }

        LaunchedEffect(barDataList, canvasHeightPx) {
            progressVerticalBars = barDataList.mapIndexed { index, barData ->
                if(barData.reached < barData.goal) {
                    val reachedBarHeightPx = barData.reached.toFloat() / maxValue * canvasHeightPx
                    Rect(
                        color = solidColorUncompleted,
                        topLeft = Offset(
                            2 * barWidthPx * index,
                            canvasHeightPx - reachedBarHeightPx
                        ),
                        size = Size(barWidthPx, reachedBarHeightPx),
                        barData = barData
                    )
                } else {
                    val barHeightPx = barData.reached.toFloat() / maxValue * canvasHeightPx
                    Rect(
                        color = solidColorCompleted,
                        topLeft = Offset(2 * barWidthPx * index, canvasHeightPx - barHeightPx),
                        size = Size(barWidthPx, barHeightPx),
                        barData = barData
                    )
                }
            }
            progressVerticalBarsOriginal = progressVerticalBars.toList()

            goalVerticalBars = barDataList.mapIndexed { index, barData ->
                if (barData.reached < barData.goal) {
                    val goalBarHeightPx = barData.goal.toFloat() / maxValue * canvasHeightPx
                    Rect(
                        color = solidColorDifference,
                        alpha = alphaColorDifference,
                        topLeft = Offset(
                            2 * barWidthPx * index,
                            canvasHeightPx - goalBarHeightPx
                        ),
                        size = Size(barWidthPx, goalBarHeightPx)
                    )
                } else {
                    null
                }
            }.filterNotNull()
        }
        Box(modifier = modifier) {
            val requestDisallowInterceptTouchEvent = remember { RequestDisallowInterceptTouchEvent() }
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInteropFilter(
                        requestDisallowInterceptTouchEvent
                    ) { event ->
                        requestDisallowInterceptTouchEvent.invoke(true)
                        when (event.action) {
                            MotionEvent.ACTION_DOWN,
                            MotionEvent.ACTION_MOVE -> {
                                fun highlightRect(rect: Rect): Rect = rect
                                    .copy(
                                            color = solidColorDifference,
                                            topLeft = rect.topLeft.copy(y = 0f),
                                            size = rect.size.copy(height = canvasHeightPx)
                                    )
                                    .apply {
                                        highlightedProgressVerticalBar = this.copy()
                                    }

                                val x = event.x

                                if(x <= 0) {
                                    progressVerticalBars = progressVerticalBarsOriginal
                                        .toMutableList()
                                        .apply {
                                            set(0, highlightRect(progressVerticalBarsOriginal.first()))
                                        }
                                } else if(x >= canvasWidthPx) {
                                    progressVerticalBars = progressVerticalBarsOriginal
                                        .toMutableList()
                                        .apply {
                                            set(progressVerticalBarsOriginal.size - 1, highlightRect(progressVerticalBarsOriginal.last()))
                                        }
                                } else {
                                    progressVerticalBars =
                                        progressVerticalBarsOriginal.map { rect ->
                                            if (x >= rect.topLeft.x && x <= rect.topLeft.x + rect.size.width * 2) {
                                                highlightRect(rect)
                                            } else {
                                                rect
                                            }
                                        }
                                }
                                true
                            }
                            else -> {
                                progressVerticalBars = progressVerticalBarsOriginal.toList()
                                highlightedProgressVerticalBar = null
                                false
                            }
                        }
                    }
            ) {
                canvasHeightPx = size.height
                canvasWidthPx = size.width
                barWidthPx = canvasWidthPx / (barDataList.size * 2 - 1)
                val horizontalLinesY = horizontalLinesYFractional.map { fraction -> fraction * canvasHeightPx }
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
                progressVerticalBars.forEach { rect ->
                    drawRect(rect)
                }
                goalVerticalBars.forEach { rect ->
                    drawRect(rect)
                }
            }

            highlightedProgressVerticalBar?.let { bar ->
                Column(
                    modifier = Modifier
                        .offset(
                            x = with(localDensity) { (bar.topLeft.x + 4 * barWidthPx).toDp() },
                            y = canvasHeight / 4
                        )
                        .width(156.dp)
                        .background(
                            color = bar.color,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    with(bar.barData!!) {
                        @Composable
                        fun CustomText(
                            text: String,
                            fontSize: TextUnit = textFontSize,
                            fontWeight: FontWeight = FontWeight.Normal
                        ) {
                            Text(
                                text = text,
                                color = onColorDifference,
                                fontSize = fontSize,
                                fontWeight = fontWeight
                            )
                        }
                        CustomText(
                            text = text,
                            fontSize = textFontSize * 1.5,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText("Goal:")
                            Row(
                                modifier= Modifier
                                    .padding(
                                        vertical = 2.dp,
                                        horizontal = 4.dp
                                    )
                            ) {
                                CustomText(goal.toString().plus(" "))
                                CustomText(unit)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText("Reached:")
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = if (reached >= goal) solidColorCompleted else solidColorUncompleted,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(
                                        vertical = 2.dp,
                                        horizontal = 4.dp
                                    )
                            ) {
                                CustomText(reached.toString().plus(" "))
                                CustomText(unit)
                            }
                        }
                    }
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