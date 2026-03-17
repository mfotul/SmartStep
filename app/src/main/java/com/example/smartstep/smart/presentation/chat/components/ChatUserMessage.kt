package com.example.smartstep.smart.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun ChatUserMessage(
    windowSizeClass: WindowSizeClass,
    message: String,
    modifier: Modifier = Modifier
) {
    val isTablet =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier
                .then(
                    if (isTablet)
                        Modifier.width(400.dp)
                    else
                        Modifier.fillMaxWidth(0.75f)
                )
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 6.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(16.dp)
        )
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun ChatUserMessagePreview() {
    SmartStepTheme {
        ChatUserMessage(
            windowSizeClass = WindowSizeClass(minHeightDp = 100, minWidthDp = 1000),
            message = "Hi, how can I help you?"
        )
    }
}