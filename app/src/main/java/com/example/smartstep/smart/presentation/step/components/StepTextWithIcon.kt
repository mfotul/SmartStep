package com.example.smartstep.smart.presentation.step.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun StepTextWithIcon(
    @DrawableRes id: Int,
    text: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        StepIcon(
            id = id,
            shape = RoundedCornerShape(8.dp),
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepTextWithIconPreview() {
    SmartStepTheme {
        StepTextWithIcon(
            id = R.drawable.sneakers,
            text = "3.2",
            unit = "km"
        )
    }
}
