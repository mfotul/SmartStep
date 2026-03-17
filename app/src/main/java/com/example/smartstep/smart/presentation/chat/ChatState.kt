package com.example.smartstep.smart.presentation.chat

import com.example.smartstep.R
import com.example.smartstep.core.presentation.util.UiText
import com.example.smartstep.smart.domain.step.ChatMessage

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val actionButtons: List<UiText> = listOf(
        UiText.StringResource(R.string.recommended_workout),
        UiText.StringResource(R.string.explain_today_trend),
        UiText.StringResource(R.string.how_to_reach_today_s_goal)
    ),
    val isSuggestionsVisible: Boolean = false,
    val isOffline: Boolean = false
)
