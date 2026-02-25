package com.example.smartstep.smart.presentation.home.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartstep.core.presentation.designsystem.theme.Inter
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeDayItem(
    day: String,
    steps: Int,
    dailyGoal: Int,
    modifier: Modifier = Modifier
) {
    val usFormatter = NumberFormat.getNumberInstance(Locale.US)
    val white = MaterialTheme.colorScheme.surfaceContainerHighest
    val progress = (steps.toFloat() / dailyGoal).coerceIn(0f, 1f) * 360f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .padding(4.dp)
    ) {
        Canvas(
            modifier = Modifier
                .size(40.dp)
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
                text = day,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            )
            Text(
                text = usFormatter.format(steps),
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
private fun HomeDayItemPreview() {
    SmartStepTheme {
        HomeDayItem(
            day = "Mon",
            steps = 250,
            dailyGoal = 1000
        )
    }
}
