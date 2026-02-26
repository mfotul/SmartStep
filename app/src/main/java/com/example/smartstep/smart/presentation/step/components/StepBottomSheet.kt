@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.smart.presentation.step.components

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StepBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    sheetGesturesEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = dragHandle,
        sheetGesturesEnabled = sheetGesturesEnabled,
        properties = properties,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
    ) {
        content()
    }
}