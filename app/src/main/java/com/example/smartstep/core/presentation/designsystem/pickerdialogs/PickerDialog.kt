package com.example.smartstep.core.presentation.designsystem.pickerdialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun <T> PickerDialog(
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
    units: List<T>,
    selectedUnit: T,
    unitDisplayText: (T) -> String,
    onSelectedUnits: (T) -> Unit,
    options1: List<Int>,
    selectedOption1: Int,
    onSelected1: (Int) -> Unit,
    onCanceled: () -> Unit,
    onConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    options2: List<Int>? = null,
    selectedOption2: Int = Int.MIN_VALUE,
    onSelected2: (Int) -> Unit = { },
    @StringRes unit1ResId: Int? = null,
    @StringRes unit2ResId: Int? = null
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .widthIn(min = 280.dp, max = 560.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(vertical = 32.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = stringResource(titleResId),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    text = stringResource(descriptionResId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            UnitSwitch(
                items = units,
                selected = selectedUnit,
                itemDisplayText = { unitDisplayText(it) },
                onSelected = onSelectedUnits,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
            )

            Row {
                PickerColumn(
                    items = options1,
                    selected = selectedOption1,
                    onSelected = onSelected1,
                    unitResId = unit1ResId,
                    modifier = Modifier.weight(1f)
                )
                options2?.let { options2 ->
                    PickerColumn(
                        items = options2,
                        selected = selectedOption2,
                        onSelected = onSelected2,
                        unitResId = unit2ResId,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                TextButton(
                    onClick = onCanceled,
                    modifier = Modifier
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextButton(
                    onClick = onConfirmed,
                    modifier = Modifier
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 860)
@Composable
fun PickerDialogPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            PickerDialog(
                titleResId = R.string.height,
                descriptionResId = R.string.used_to_calculate_distance,
                units = listOf("cm", "ft/in"),
                selectedUnit = "ft/in",
                unitDisplayText = { it },
                onSelectedUnits = {},
                options1 = (-2..8).toList(),
                selectedOption1 = 5,
                onSelected1 = {},
                onCanceled = {},
                onConfirmed = {},
                options2 = (-2..13).toList(),
                selectedOption2 = 9,
                onSelected2 = {},
                unit1ResId = R.string.ft,
                unit2ResId = R.string.inches
            )
        }
    }
}