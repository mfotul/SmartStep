package com.example.smartstep.smart.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.buttons.PrimaryButton
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun HomeFirstPermissionDialog(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_localtion),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.access_to_motion_sensors),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight(500),
            textAlign = TextAlign.Center
        )
        PrimaryButton(
            text = stringResource(R.string.allow_access),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun HomeFirstPermissionDialogPreview() {
    SmartStepTheme {
        HomeFirstPermissionDialog(
            {}
        )
    }
}