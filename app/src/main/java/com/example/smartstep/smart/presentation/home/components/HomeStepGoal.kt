@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.smart.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.buttons.PrimaryButton
import com.example.smartstep.core.presentation.designsystem.pickerdialogs.PickerColumn
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun HomeAdaptiveStepGoal(
    onDismiss: () -> Unit,
    windowSizeClass: WindowSizeClass,
    content: @Composable () -> Unit
) {
    if (windowSizeClass.minWidthDp >= 840)
        HomeDialog(
            onDismissRequest = onDismiss,
        ) {
            content()
        }
    else
        HomeBottomSheet(
            dragHandle = null,
            sheetGesturesEnabled = false,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false,
                shouldDismissOnClickOutside = false
            ),
            onDismiss = onDismiss,
        ) {
            content()
        }
}

@Composable
fun HomeStepGoal(
    items: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.step_goal),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight(500),
            color = MaterialTheme.colorScheme.onPrimary,
        )
        PickerColumn(
            items = items,
            selected = selected,
            onSelected = onSelected,
        )
        PrimaryButton(
            text = stringResource(R.string.save),
            onClick = onSaveClick,
        )
        TextButton(
            onClick = onCancelClick,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight(500),
            )
        }
    }
}

@Preview
@Composable
private fun HomeStepGoalPreview() {
    SmartStepTheme {
        HomeStepGoal(
            items = (100..220).toList(),
            selected = 103,
            onSelected = {},
//            onDismiss = {},
            onSaveClick = {},
            onCancelClick = {}
        )
    }
}