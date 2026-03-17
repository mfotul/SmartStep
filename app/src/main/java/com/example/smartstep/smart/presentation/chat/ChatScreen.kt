package com.example.smartstep.smart.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.designsystem.theme.borderColor
import com.example.smartstep.smart.presentation.chat.components.ChatAiMessage
import com.example.smartstep.smart.presentation.chat.components.ChatBottomBar
import com.example.smartstep.smart.presentation.chat.components.ChatTopAppBar
import com.example.smartstep.smart.presentation.chat.components.ChatUserMessage
import com.example.smartstep.smart.presentation.preview.PreviewModels
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreenRoot(
    viewModel: ChatViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChatScreen(
        state = state,
        messageFieldState = viewModel.messageFieldState,
        onAction = {
            when (it) {
                ChatAction.OnBackClick -> onBackClick()
                else -> viewModel.onAction(it)
            }
        }
    )
}

@Composable
fun ChatScreen(
    state: ChatState,
    messageFieldState: TextFieldState,
    onAction: (ChatAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    Scaffold(
        topBar = {
            ChatTopAppBar(
                onBackClick = { onAction(ChatAction.OnBackClick) }
            )
        },
        bottomBar = {
            ChatBottomBar(
                textState = messageFieldState,
                isSuggestionsVisible = state.isSuggestionsVisible,
                isOffline = state.isOffline,
                actionButtons = state.actionButtons,
                onSendClick = { onAction(ChatAction.OnAskAi) },
                onQuickAskAiClick = { onAction(ChatAction.OnQuickAskAi(it)) },
                onSuggestionsSwitchClick = { onAction(ChatAction.OnSuggestionsSwitchClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            HorizontalDivider(
                color = MaterialTheme.colorScheme.borderColor,
                thickness = 1.dp
            )
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true,
                modifier = Modifier
                    .widthIn(max = 600.dp)
            ) {
                items(items = state.messages.reversed()) { message ->
                    if (message.isUser)
                        ChatUserMessage(
                            windowSizeClass = windowSizeClass,
                            message = message.message
                        )
                    else
                        ChatAiMessage(
                            message = message.message
                        )
                }
            }
        }
    }

}

@Preview(widthDp = 1400)
@Composable
private fun ChatScreenPreview() {
    SmartStepTheme {
        ChatScreen(
            state = PreviewModels.chatState,
            messageFieldState = rememberTextFieldState(),
            onAction = {}
        )
    }
}