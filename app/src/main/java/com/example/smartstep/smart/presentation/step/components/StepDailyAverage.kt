package com.example.smartstep.smart.presentation.step.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.preview.PreviewModels
import com.example.smartstep.smart.presentation.step.models.StepUi
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StepDailyAverage(
    dailyAverage: Int,
    weeklyStats: List<StepUi>,
    modifier: Modifier = Modifier
) {
    val usFormatter = NumberFormat.getNumberInstance(Locale.US)
    var dailyItemWidth by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.daily_average, usFormatter.format(dailyAverage)),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.surfaceContainerHighest
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    dailyItemWidth = it.width / 7 - 30
                }
        ) {
            weeklyStats.forEach {
                StepDayItem(
                    stepUi = it,
                    size = with(LocalDensity.current) { dailyItemWidth.toDp() }
                )
            }
        }
    }
}


@Preview
@Composable
private fun StepDailyAveragePreview() {
    SmartStepTheme {
        val previewWeeklyStats = PreviewModels.stepState.weeklyStats
        StepDailyAverage(
            dailyAverage = 1000,
            weeklyStats = previewWeeklyStats
        )
    }
}