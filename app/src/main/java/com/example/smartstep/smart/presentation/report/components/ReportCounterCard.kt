package com.example.smartstep.smart.presentation.report.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.report.model.DataType
import java.util.Locale

@Composable
fun ReportCounterCard(
    isTablet: Boolean,
    value: Float,
    dailyAverageValue: Float,
    dataType: DataType,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .then(
                if (isTablet)
                    Modifier.widthIn(max = 394.dp)
                else
                    Modifier.fillMaxWidth()
            )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(getDescription(dataType)),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                )
                Text(
                    text = stringResource(R.string.this_week),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                )
            }
            Text(
                text = if (dataType == DataType.DISTANCE)
                    "%.1f".format(Locale.US,value)
                else
                    "%.0f".format(value),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(
                    R.string.average_value,
                    if (dataType == DataType.DISTANCE)
                        "%.1f".format(Locale.US, dailyAverageValue)
                    else
                        "%.0f".format(dailyAverageValue),
                    stringResource(getValueSuffix(dataType))
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

private fun getDescription(dataType: DataType): Int {
    return when (dataType) {
        DataType.STEPS -> R.string.steps_label
        DataType.CALORIES -> R.string.calories_label
        DataType.TIME -> R.string.minutes_label
        DataType.DISTANCE -> R.string.kilometers_label
    }
}

private fun getValueSuffix(dataType: DataType): Int {
    return when (dataType) {
        DataType.STEPS -> R.string.steps_lowercase
        DataType.CALORIES -> R.string.kcal
        DataType.TIME -> R.string.min
        DataType.DISTANCE -> R.string.km
    }
}

@Preview
@Composable
private fun ReportCounterCardPreview() {
    SmartStepTheme {
        ReportCounterCard(
            isTablet = false,
            value = 1000f,
            dailyAverageValue = 1000f,
            dataType = DataType.DISTANCE
        )
    }
}