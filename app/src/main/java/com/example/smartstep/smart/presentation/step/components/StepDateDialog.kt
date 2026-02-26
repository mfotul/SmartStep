package com.example.smartstep.smart.presentation.step.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.pickerdialogs.PickerColumn
import com.example.smartstep.core.presentation.designsystem.theme.Inter
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.step.models.DateInput


private val YEARS = (2000..2027).toList()
private val MONTHS = (-1..12).toList() + -2
private val DAYS = (-1..31).toList() + -2

@Composable
fun StepDateDialog(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    onCancelClick: () -> Unit,
    onSaveClick: (DateInput) -> Unit,
    modifier: Modifier = Modifier
) {
    var yearSelected by rememberSaveable { mutableIntStateOf(selectedYear) }
    var monthSelected by rememberSaveable { mutableIntStateOf(selectedMonth) }
    var daySelected by rememberSaveable { mutableIntStateOf(selectedDay) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(
            text = "Date",
            fontFamily = Inter,
            fontWeight = FontWeight(500),
            fontSize = 20.sp,
            lineHeight = 28.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row {
            PickerColumn(
                items = YEARS,
                selected = yearSelected,
                onSelected = {
                    yearSelected = it
                },
                unitResId = null,
                horizontalArrangement = Arrangement.Start,
                contentPadding = PaddingValues(start = 16.dp),
                modifier = Modifier.weight(1f)
            )
            PickerColumn(
                items = MONTHS,
                selected = monthSelected,
                onSelected = {
                    monthSelected = it
                },
                unitResId = null,
                modifier = Modifier.weight(1f)
            )
            PickerColumn(
                items = DAYS,
                selected = daySelected,
                onSelected = {
                    daySelected = it
                },
                unitResId = null,
                horizontalArrangement = Arrangement.End,
                contentPadding = PaddingValues(end = 16.dp),
                modifier = Modifier.weight(1f)
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
                onClick = { onSaveClick(DateInput(yearSelected, monthSelected, daySelected)) }
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

@Preview
@Composable
private fun StepDateDialogPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(Color.White)
                .padding(16.dp)
        ) {
            StepDateDialog(
                selectedYear = 2026,
                selectedMonth = 2,
                selectedDay = 17,
                onCancelClick = {},
                onSaveClick = {}
            )
        }
    }
}