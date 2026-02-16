package com.example.smartstep.smart.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun StepCounterProgressIndicator(
   progress: () -> Float,
    modifier: Modifier = Modifier
) {
    val trackColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.2f)
    val progressColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
    ) {
        val padding = 2.dp.toPx()
        val progressWidth = (size.width - 2 * padding) * progress()
        drawRoundRect(
            color = trackColor,
            size = size,
            cornerRadius = CornerRadius(x = 100f, y = 100f)
        )

        drawRoundRect(
            color = progressColor,
            topLeft = Offset(x = padding, y = padding),
            size = Size(width = progressWidth, height = size.height - 2 * padding),
            cornerRadius = CornerRadius(x = 100f, y = 100f)
        )
    }
}

@Preview
@Composable
private fun StepCounterProgressIndicatorPreview(
) {
    SmartStepTheme {
        StepCounterProgressIndicator(
            progress = { 1f }
        )
    }
}

