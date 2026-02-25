package com.example.smartstep.core.presentation.designsystem.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.designsystem.theme.borderColor

@Composable
fun SmartTextFieldInput(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
        BasicTextField(
            state = state,
            textStyle = MaterialTheme.typography.bodyLarge,
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = {
                if (!asCharSequence().isDigitsOnly())
                    revertAllChanges()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun SmartTextFieldInputPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(16.dp)
        ) {
            SmartTextFieldInput(
                state = rememberTextFieldState(),
                label = "Steps"
            )
        }
    }
}