package com.example.smartstep.smart.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.fields.SmartTextField
import com.example.smartstep.core.presentation.designsystem.fields.SmartTextFieldInput
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun HomeEditStepsDialog(
    date: String,
    stepsState: TextFieldState,
    onDateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = stringResource(R.string.edit_steps),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight(500)
            )
            Text(
                text = stringResource(R.string.calories_distance_duration),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight(500)
            )
        }
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            SmartTextField(
                label = stringResource(R.string.date),
                value = date,
                onClick = onDateClick
            )
            SmartTextFieldInput(
                state = stepsState,
                label = stringResource(R.string.steps_label)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.End),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = onCancelClick
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight(500)
                )
            }
            TextButton(
                onClick = onSaveClick
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight(500)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun HomeEditStepsDialogPreview() {
    SmartStepTheme {
        HomeEditStepsDialog(
            date = "10000",
            stepsState = rememberTextFieldState(),
            onDateClick = {},
            onCancelClick = {},
            onSaveClick = {}
        )
    }

}