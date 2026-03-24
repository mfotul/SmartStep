package com.example.smartstep.smart.presentation.report.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.designsystem.theme.borderColor
import com.example.smartstep.smart.presentation.preview.PreviewModels
import com.example.smartstep.smart.presentation.report.model.DailyData
import com.example.smartstep.smart.presentation.report.model.DataSource
import com.example.smartstep.smart.presentation.report.model.DataType
import kotlin.math.roundToInt

@Composable
fun ReportDayItem(
    dailyData: DailyData,
    modifier: Modifier = Modifier
) {
    val painterResourceId = when (dailyData.dataSource) {
        DataSource.CURRENT_DATE -> R.drawable.time_clock
        DataSource.DB -> R.drawable.outline_check_24
        DataSource.NONE -> R.drawable.minus
    }

    val iconColor = when (dailyData.dataSource) {
        DataSource.NONE -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.primary
    }

    val iconBackgroundColor = when (dailyData.dataSource) {
        DataSource.NONE -> MaterialTheme.colorScheme.surfaceContainer
        else -> MaterialTheme.colorScheme.secondary
    }

    val columnBackgroundColor = when (dailyData.dataSource) {
        DataSource.NONE -> MaterialTheme.colorScheme.background
        else -> MaterialTheme.colorScheme.surfaceContainerHighest
    }

    val columnBorderColor = when (dailyData.dataSource) {
        DataSource.CURRENT_DATE -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.borderColor
    }

    val contentColor = when (dailyData.dataSource) {
        DataSource.CURRENT_DATE -> MaterialTheme.colorScheme.primary
        DataSource.DB -> MaterialTheme.colorScheme.onPrimary
        DataSource.NONE -> MaterialTheme.colorScheme.onSecondary
    }

    val unit = when (dailyData.dataType) {
        DataType.STEPS -> R.string.steps_lowercase
        DataType.CALORIES -> R.string.calories
        DataType.TIME -> R.string.minutes
        DataType.DISTANCE -> R.string.kilometers
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .background(
                color = columnBackgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = columnBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = dailyData.date,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
            Icon(
                painter = painterResource(id = painterResourceId),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .padding(6.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        MaterialTheme.typography.headlineLarge.toSpanStyle()
                            .copy(color = contentColor, fontWeight = FontWeight.Medium)
                    ) {
                        append(dailyData.value.roundToInt().toString())
                    }
                    withStyle(
                        MaterialTheme.typography.bodySmall.toSpanStyle()
                            .copy(color = MaterialTheme.colorScheme.onSecondary)
                    ) {
                        append(" " + stringResource(id = unit))
                    }
                },
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = when {
                    dailyData.dataSource == DataSource.NONE ->
                        stringResource(R.string.no_data)

                    dailyData.dataType == DataType.STEPS ->
                        stringResource(R.string.goal_steps, dailyData.goal)

                    else -> ""
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xE0E0E0)
@Composable
private fun ReportDayItemPreview() {
    SmartStepTheme {
        ReportDayItem(
            dailyData = PreviewModels.reportState.weekData[2]
        )
    }
}