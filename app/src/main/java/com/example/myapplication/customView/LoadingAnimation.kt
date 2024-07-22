package com.example.myapplication.customView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.LightModeColor
import kotlinx.coroutines.delay

@Composable
fun LoadingAnimation(modifier: Modifier) {
    var sweepAngle by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect (true){
        while (true){
            sweepAngle += 5f
            if (sweepAngle == 360f) {
                sweepAngle = -360f
            }
            delay(10)
        }
    }
    Box(modifier = modifier.size(30.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {

                drawArc(
                    size = size,
                    color = LightModeColor.BackGroundColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(10f, cap = StrokeCap.Round),
                )
        }
    }
}