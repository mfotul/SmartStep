@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.smart.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
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
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun HomeAdaptiveSecondPermissionDialog(
    windowSizeClass: WindowSizeClass,
    content: @Composable () -> Unit
) {
    if (windowSizeClass.minWidthDp >= 840)
        HomeDialog(
            onDismissRequest = {}
        ) {
            content()
        }
    else
        HomeBottomSheet(
            onDismiss = {},
            dragHandle = null,
            sheetGesturesEnabled = false,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false,
                shouldDismissOnClickOutside = false
            ),
        ) {
            content()
        }
}

@Composable
fun HomeSecondPermissionDialog(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.emable_access_manualy),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight(500)
            )
            Text(
                text = stringResource(R.string.track_your_steps),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "1. Open Permissions",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight(500),
            )
            Text(
                text = "2. Tap Physical activity",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight(500),
            )
            Text(
                text = "3. Select Allow",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight(500),
            )
        }
        PrimaryButton(
            text = stringResource(R.string.open_settings),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun HomeSecondPermissionDialogPreview() {
    SmartStepTheme {
        HomeSecondPermissionDialog(
            {}
        )
    }
}