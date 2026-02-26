package com.example.smartstep.smart.presentation.step.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun StepResetSteps(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Are you sure you want to reset today ’s steps?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.End),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = onCancelClick
            ) {
                Text(
                    text = stringResource(com.example.smartstep.R.string.cancel),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight(500)
                )
            }
            TextButton(
                onClick = onSaveClick
            ) {
                Text(
                    text = stringResource(com.example.smartstep.R.string.save),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight(500)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xD0D0D0)
@Composable
private fun StepResetStepsPreview() {
    SmartStepTheme {
        StepResetSteps(
            onCancelClick = {},
            onSaveClick = {}
        )
    }
}