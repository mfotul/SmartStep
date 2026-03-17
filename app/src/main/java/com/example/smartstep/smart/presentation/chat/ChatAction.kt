package com.example.smartstep.smart.presentation.chat

sealed interface ChatAction {
    data class OnQuickAskAi(val question: String) : ChatAction
    data object OnAskAi: ChatAction
    data object OnBackClick: ChatAction
    data object OnSuggestionsSwitchClick: ChatAction

}