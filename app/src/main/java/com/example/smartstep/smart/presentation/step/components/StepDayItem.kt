package com.example.smartstep.smart.presentation.step.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartstep.core.presentation.designsystem.theme.Inter
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.preview.PreviewModels
import com.example.smartstep.smart.presentation.step.models.StepUi
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StepDayItem(
    stepUi: StepUi,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val usFormatter = NumberFormat.getNumberInstance(Locale.US)
    val white = MaterialTheme.colorScheme.surfaceContainerHighest
    val progress = if (stepUi.steps == 0f) 0f else (stepUi.steps / stepUi.dailyGoal).coerceIn(0f, 1f) * 360f
    val progressSize = if (size > 40.dp) 40.dp else size

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .padding(4.dp)
    ) {
        Canvas(
            modifier = Modifier
                .size(progressSize)
        ) {
            drawCircle(
                color = white,
                style = Stroke(
                    width = 12f
                )
            )
            drawArc(
                color = Color(0xFF0DC600),
                startAngle = -90f,
                sweepAngle = progress,
                useCenter = false,
                style = Stroke(
                    width = 6f,
                    cap = StrokeCap.Round
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stepUi.day,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            )
            Text(
                text = usFormatter.format(stepUi.steps),
                fontFamily = Inter,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0000FF)
@Composable
private fun StepDayItemPreview() {
    SmartStepTheme {
        val fakeStepDayUi = PreviewModels.stepState.weeklyStats.first()
        StepDayItem(
            stepUi = fakeStepDayUi,
            size = 30.dp
        )
    }
}
