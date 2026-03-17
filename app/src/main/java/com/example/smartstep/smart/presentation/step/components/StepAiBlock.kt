package com.example.smartstep.smart.presentation.step.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.step.models.AiResult

@Composable
fun StepAiBlock(
    isOffline: Boolean,
    aiOutput: AiResult,
    onMoreClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ai_artificial_intelligence),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isOffline)
                        stringResource(R.string.try_again)
                    else
                        stringResource(R.string.more),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight(500),
                    color = MaterialTheme.colorScheme.primary,
                )
                IconButton(
                    onClick = {
                        if (isOffline)
                            onTryAgainClick()
                        else
                            onMoreClick()
                    }
                ) {
                    Icon(
                        painter = if (isOffline)
                            painterResource(R.drawable.reload)
                        else
                            painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }

        if (isOffline) {
            Text(
                text = stringResource(R.string.connect_to_internet),
                style = MaterialTheme.typography.bodyLarge,
            )
        } else
            when (aiOutput) {
                is AiResult.Success -> {
                    Text(
                        text = aiOutput.result,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                AiResult.Loading -> {
                    Loading()
                }
            }
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = alpha),
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = alpha),
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = alpha),
                )
        )
    }
}

@Preview
@Composable
private fun StepAiBlockPreview() {
    SmartStepTheme {
        StepAiBlock(
            isOffline = true,
//            text = "Not even close to your goal, but that's okay! Let's crush this morning workout and get those steps climbing!",
            aiOutput = AiResult.Loading,
            onMoreClick = {},
            onTryAgainClick = {}
        )
    }
}