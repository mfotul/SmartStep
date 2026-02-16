@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.smart.presentation.home.components

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeBottomSheet(
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

//ModalBottomSheet(
//onDismissRequest = onDismiss,
//dragHandle = null,
//sheetGesturesEnabled = false,
//containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//properties = ModalBottomSheetProperties(
//shouldDismissOnBackPress = false,
//shouldDismissOnClickOutside = true
//),
//modifier = modifier,


//ModalBottomSheet(
//onDismissRequest = onDismiss,
//dragHandle = null,
//sheetGesturesEnabled = false,
//containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//properties = ModalBottomSheetProperties(
//shouldDismissOnBackPress = false,
//shouldDismissOnClickOutside = false
//),
//modifier = modifier,

//ModalBottomSheet(
//onDismissRequest = onDismiss,
//dragHandle = null,
//sheetGesturesEnabled = false,
//containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//properties = ModalBottomSheetProperties(
//shouldDismissOnBackPress = false,
//shouldDismissOnClickOutside = false
//),
//modifier = modifier,

//ModalBottomSheet(
//onDismissRequest = onDismiss,
//containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//modifier = modifier,
//) {