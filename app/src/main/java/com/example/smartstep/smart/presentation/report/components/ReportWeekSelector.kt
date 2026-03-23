package com.example.smartstep.smart.presentation.report.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun ReportWeekSelector(
    week: String,
    isLeftArrowEnabled: Boolean,
    isRightArrowEnabled: Boolean,
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        ReportIconButton(
            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
            onClick = onLeftArrowClick,
            isEnabled = isLeftArrowEnabled
        )
        Text(
            text = week,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        ReportIconButton(
            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
            onClick = onRightArrowClick,
            isEnabled = isRightArrowEnabled
        )
    }
}

@Composable
fun ReportIconButton(
    painter: Painter,
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        enabled = isEnabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
        ),
        modifier = modifier
            .clip(CircleShape)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun ReportWeekSelectorPreview() {
    SmartStepTheme {
        ReportWeekSelector(
            week = "Nov 16 - Nov 22",
            isLeftArrowEnabled = false,
            isRightArrowEnabled = true,
            onLeftArrowClick = {},
            onRightArrowClick = {}
        )
    }
}