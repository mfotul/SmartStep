package com.example.smartstep.smart.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeDailyAverage(
    dailyAverage: Int,
    modifier: Modifier = Modifier
) {
    val usFormatter = NumberFormat.getNumberInstance(Locale.US)
    Column(
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
        ) {
          
        }
    }
}


@Preview
@Composable
private fun HomeDailyAveragePreview() {
    SmartStepTheme {
        HomeDailyAverage(
            dailyAverage = 1000
        )
    }
}