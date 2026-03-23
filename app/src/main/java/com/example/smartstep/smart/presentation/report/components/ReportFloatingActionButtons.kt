package com.example.smartstep.smart.presentation.report.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.designsystem.theme.borderColor
import com.example.smartstep.smart.presentation.report.model.DataType

@Composable
fun ReportFloatingActionButtons(
    selectedDataType: DataType,
    onDataTypeChange: (DataType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.borderColor),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DataType.entries.forEachIndexed { index, dataType ->
                val isSelected = dataType == selectedDataType

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent)
                        .clickable { onDataTypeChange(dataType) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = getIconForDataType(dataType)),
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = getLabelForDataType(dataType)),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (index < DataType.entries.size - 1) {
                    VerticalDivider(
                        color = MaterialTheme.colorScheme.borderColor,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        }
    }
}

private fun getIconForDataType(dataType: DataType): Int {
    return when (dataType) {
        DataType.STEPS -> R.drawable.sneakers
        DataType.CALORIES -> R.drawable.weight_diet
        DataType.TIME -> R.drawable.time_clock
        DataType.DISTANCE -> R.drawable.pin__location__direction
    }
}

private fun getLabelForDataType(dataType: DataType): Int {
    return when (dataType) {
        DataType.STEPS -> R.string.steps_label
        DataType.CALORIES -> R.string.calories_label
        DataType.TIME -> R.string.time_label
        DataType.DISTANCE -> R.string.distance_label
    }
}

@Preview
@Composable
private fun ReportFloatingActionButtonsPreview() {
    SmartStepTheme {
        ReportFloatingActionButtons(
            selectedDataType = DataType.CALORIES,
            onDataTypeChange = {}
        )
    }
}