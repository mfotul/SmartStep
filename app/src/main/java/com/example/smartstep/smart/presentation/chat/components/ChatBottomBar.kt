package com.example.smartstep.smart.presentation.chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.designsystem.theme.borderColor
import com.example.smartstep.core.presentation.util.UiText
import com.example.smartstep.smart.presentation.chat.ChatState

@Composable
fun ChatBottomBar(
    textState: TextFieldState,
    isSuggestionsVisible: Boolean,
    actionButtons: List<UiText>,
    isOffline: Boolean,
    onSendClick: () -> Unit,
    onQuickAskAiClick: (String) -> Unit,
    onSuggestionsSwitchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.borderColor
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .drawBehind {
                drawLine(
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    color = borderColor,
                    strokeWidth = 4f
                )
            }
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Column(
            modifier = modifier
                .widthIn(max = 400.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .imePadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.quick_suggestion),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                IconButton(
                    onClick = onSuggestionsSwitchClick
                ) {
                    Icon(
                        painter = painterResource(
                            if (isSuggestionsVisible)
                                R.drawable.baseline_keyboard_arrow_up_24
                            else
                                R.drawable.baseline_keyboard_arrow_down_24
                        ),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            val animSpec = tween<IntSize>(durationMillis = 250, easing = FastOutSlowInEasing)
            AnimatedVisibility(
                visible = isSuggestionsVisible,
                enter = expandVertically(
                    animationSpec = animSpec,
                    expandFrom = Alignment.Top
                ),
                exit = shrinkVertically(
                    animationSpec = animSpec,
                    shrinkTowards = Alignment.Top
                ),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    actionButtons.forEach { actionButton ->
                        val text = actionButton.asString()
                        TextButton(
                            enabled = !isOffline,
                            onClick = {
                                onQuickAskAiClick(text)
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.borderColor
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    state = textState,
                    shape = RoundedCornerShape(10.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    enabled = !isOffline,
                    placeholder = {
                        Text(
                            text = if (isOffline)
                                stringResource(R.string.online_connection_required)
                            else
                                stringResource(R.string.ask_me_anything),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    trailingIcon = {
                        if (isOffline)
                            Icon(
                                painter = painterResource(R.drawable.cloud_off),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                    },
                    lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.borderColor,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        onSendClick()
                    },
                    enabled = !isOffline,
                    shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.send_message),
                        contentDescription = stringResource(R.string.send_message),
                        tint = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 1400)
@Composable
private fun ChatBottomBarPreview() {
    SmartStepTheme {
        ChatBottomBar(
            textState = rememberTextFieldState(),
            actionButtons = ChatState().actionButtons,
            isSuggestionsVisible = false,
            isOffline = true,
            onQuickAskAiClick = {},
            onSuggestionsSwitchClick = {},
            onSendClick = {}
        )
    }
}