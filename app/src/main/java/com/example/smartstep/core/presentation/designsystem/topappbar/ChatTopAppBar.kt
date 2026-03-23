@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.core.presentation.designsystem.topappbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme

@Composable
fun SmartStepTopAppBar(
    text: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_left_24),
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun SmartStepTopAppBarPreview() {
    SmartStepTheme {
        SmartStepTopAppBar(
            text = stringResource(R.string.ai_coach),
            onBackClick = {}
        )
    }
}