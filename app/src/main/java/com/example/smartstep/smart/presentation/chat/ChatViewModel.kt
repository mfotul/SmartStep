package com.example.smartstep.smart.presentation.chat

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.smart.domain.step.AiCoach
import com.example.smartstep.smart.domain.step.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val aiCoach: AiCoach,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private var hasLoadedInitialData = false

    private var _state = MutableStateFlow(ChatState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChatState()
        )

    val messageFieldState = TextFieldState()


    private fun loadInitialData() {
        viewModelScope.launch {
            aiCoach.run("Introduce yourself, how can you help me ?")
        }
        combine(
            aiCoach.chats,
            connectivityObserver.observer()
        ) {  chats, observer ->
            _state.update {
                it.copy(
                    messages = chats,
                    isOffline = observer == ConnectivityObserver.Status.Lost
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ChatAction) {
        when (action) {
            ChatAction.OnAskAi -> onAskAi()
            ChatAction.OnBackClick -> {}
            is ChatAction.OnQuickAskAi -> onQuickAskAi(action.question)
            ChatAction.OnSuggestionsSwitchClick -> onSuggestionsSwitchClick()
        }
    }

    private fun onAskAi() {
        _state.update { it.copy(isSuggestionsVisible = false) }
        viewModelScope.launch {
            val question = messageFieldState.text.toString()
            messageFieldState.clearText()
            aiCoach.question(question)
        }
    }

    private fun onQuickAskAi(question: String) {
        _state.update { it.copy(isSuggestionsVisible = false) }
        viewModelScope.launch {
            aiCoach.question(question)
        }
    }

    private fun onSuggestionsSwitchClick() {
        _state.update { it.copy(isSuggestionsVisible = !it.isSuggestionsVisible) }
    }
}