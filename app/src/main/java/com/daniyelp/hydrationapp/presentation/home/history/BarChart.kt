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
    fun dpToPx(dp: Dp) = with(localDensity) { dp.toPx() }

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

    data class Line(
        val color: Color = solidColorDifference,
        val alpha: Float = alphaColorDifference,
        val start: Offset,
        val end: Offset,
        val strokeWidth: Float
    )

    fun DrawScope.drawLine(line: Line) {
        drawLine(
            color = line.color,
            alpha = line.alpha,
            start = line.start,
            end = line.end,
            strokeWidth = line.strokeWidth
        )
    }

    val canvasTopPadding = spToDp(textFontSize)
    var canvasHeightPx by remember { mutableStateOf(0f) }
    val canvasHeight by derivedStateOf { pxToDp(canvasHeightPx) }
    var canvasWidthPx by remember { mutableStateOf(0f) }
    val canvasWidth by derivedStateOf { pxToDp(canvasWidthPx) }
    val rectangleWidthPx by derivedStateOf {
        canvasWidthPx / (barDataList.size * 2 - 1)
    }
    val rectangleWidth by derivedStateOf { pxToDp(rectangleWidthPx) }
    val barDataMaxValue = barDataList.flatMap { listOf(it.goal, it.reached) }.maxOrNull()!!
    val horizontalLinesYFractional by derivedStateOf {
        if (horizontalLinesNumber > 0) {
            listOf(0f) + (1 until horizontalLinesNumber).map { 1f / horizontalLinesNumber * it }
        } else {
            emptyList()
        }
    }
    val horizontalLines by derivedStateOf {
        horizontalLinesYFractional
            .map { fraction -> fraction * canvasHeightPx }
            .map { y ->
                Line(
                    color = solidColorDifference,
                    start = Offset(0f, y),
                    end = Offset(canvasWidthPx, y),
                    strokeWidth = dpToPx(horizontalLineThickness),
                    alpha = alphaColorDifference
                )
            }
    }
    val leftTextValues by derivedStateOf {
        (1..horizontalLinesNumber).map { barDataMaxValue / it }
    }
    var reachedVerticalRectangles by remember { mutableStateOf(emptyList<Rect>()) }
    var reachedVerticalRectanglesInitial by remember { mutableStateOf(emptyList<Rect>()) }
    var goalVerticalRectangles by remember { mutableStateOf(emptyList<Rect>()) }
    val squares by derivedStateOf {
        reachedVerticalRectangles.mapIndexed { index, rect ->
            Rect(
                color = solidColorDifference,
                alpha = if(index == 0 || index == barDataList.size - 1 || rect.color == solidColorDifference) 1f else alphaColorDifference,
                topLeft = Offset(rect.topLeft.x, 0f),
                size = Size(rectangleWidthPx, rectangleWidthPx),
                barData = null
            )
        }
    }
    var highlightedVerticalRectangle by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(barDataList, canvasHeightPx) {
        reachedVerticalRectangles = barDataList.mapIndexed { index, barData ->
            if(barData.reached < barData.goal) {
                val reachedBarHeightPx = barData.reached.toFloat() / barDataMaxValue * canvasHeightPx
                Rect(
                    color = solidColorUncompleted,
                    topLeft = Offset(
                        2 * rectangleWidthPx * index,
                        canvasHeightPx - reachedBarHeightPx
                    ),
                    size = Size(rectangleWidthPx, reachedBarHeightPx),
                    barData = barData
                )
            } else {
                val barHeightPx = barData.reached.toFloat() / barDataMaxValue * canvasHeightPx
                Rect(
                    color = solidColorCompleted,
                    topLeft = Offset(2 * rectangleWidthPx * index, canvasHeightPx - barHeightPx),
                    size = Size(rectangleWidthPx, barHeightPx),
                    barData = barData
                )
            }
        }
        reachedVerticalRectanglesInitial = reachedVerticalRectangles.toList()

        goalVerticalRectangles = barDataList.mapIndexed { index, barData ->
            if (barData.reached < barData.goal) {
                val goalBarHeightPx = barData.goal.toFloat() / barDataMaxValue * canvasHeightPx
                Rect(
                    color = solidColorDifference,
                    alpha = alphaColorDifference,
                    topLeft = Offset(
                        2 * rectangleWidthPx * index,
                        canvasHeightPx - goalBarHeightPx
                    ),
                    size = Size(rectangleWidthPx, goalBarHeightPx)
                )
            } else {
                null
            }
        }.filterNotNull()
    }

    fun onCanvasTouchRelease() {
        reachedVerticalRectangles = reachedVerticalRectanglesInitial.toList()
        highlightedVerticalRectangle = null
    }

    fun onCanvasTouch(x: Float) {
        fun getRectToHighlight(x: Float): Int {
            if(x <= 0) {
                return 0
            } else if(x >= canvasWidthPx) {
                return barDataList.size - 1
            } else {
                reachedVerticalRectanglesInitial.forEachIndexed { index, rect ->
                    if (x >= rect.topLeft.x && x <= rect.topLeft.x + rect.size.width * 2) {
                        return index
                    }
                }
            }
            return -1
        }
        fun highlightRect(rect: Rect): Rect = rect
            .copy(
                color = solidColorDifference,
                topLeft = rect.topLeft.copy(y = 0f),
                size = rect.size.copy(height = canvasHeightPx)
            )
            .apply {
                highlightedVerticalRectangle = this.copy()
            }
        reachedVerticalRectangles = reachedVerticalRectanglesInitial
            .toMutableList()
            .apply {
                val rectIndex= getRectToHighlight(x)
                set(rectIndex, highlightRect(get(rectIndex)))
            }
    }

    @Composable
    fun VerticalRectangles(modifier: Modifier = Modifier) {
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
                                onCanvasTouch(event.x)
                                true
                            }
                            else -> {
                                onCanvasTouchRelease()
                                false
                            }
                        }
                    }
            ) {
                canvasHeightPx = size.height
                canvasWidthPx = size.width
                horizontalLines.forEach(::drawLine)
                reachedVerticalRectangles.forEach(::drawRect)
                goalVerticalRectangles.forEach(::drawRect)
            }

            highlightedVerticalRectangle?.let { bar ->
                val cardWidth = 156.dp
                Column(
                    modifier = Modifier
                        .offset(
                            x = with(localDensity) {
                                (bar.topLeft.x + 4 * rectangleWidthPx)
                                    .toDp()
                                    .takeIf { it + cardWidth <= canvasWidth }
                                    ?: ((bar.topLeft.x - 4 * rectangleWidthPx).toDp() - cardWidth)
                            },
                            y = canvasHeight / 4
                        )
                        .width(cardWidth)
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
    fun Squares(modifier: Modifier = Modifier) {
        Canvas(modifier) {
            squares.forEach { rect ->
                drawRect(rect)
            }
        }
    }

    @Composable
    fun StartTexts() {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.TopEnd
        ) {
            //draw the texts (without the "0")
            horizontalLinesYFractional.forEachIndexed { index, fraction ->
                ChartText(
                    modifier = Modifier.offset(y = canvasHeight * fraction + canvasTopPadding * (1 / 4f)),
                    text = leftTextValues[index].toString(),
                )
            }
            //draw the "0" text
            ChartText(
                modifier = Modifier.offset(y = canvasHeight),
                text = "0",
            )
        }
    }

    @Composable
    fun StartEndTexts() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ChartText(barDataList.firstOrNull()?.text ?: "")
            ChartText(barDataList.lastOrNull()?.text ?: "")
        }
    }

    Row(modifier = modifier) {
        StartTexts()
        Spacer(modifier = Modifier.width(1.dp))
        Column {
            VerticalRectangles(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(top = canvasTopPadding)
            )
            Spacer(modifier = Modifier.height(rectangleWidth))
            if (rectangleWidth < squareVisibleMaxSizeThreshold) {
                Squares(
                    modifier = Modifier
                        .height(rectangleWidth)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(rectangleWidth))
            }
            StartEndTexts()
        }
    }
}