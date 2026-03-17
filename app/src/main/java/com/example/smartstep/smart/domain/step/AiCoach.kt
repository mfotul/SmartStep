package com.example.smartstep.smart.domain.step

import kotlinx.coroutines.flow.StateFlow

interface AiCoach {
    val chats: StateFlow<List<ChatMessage>>
    suspend fun run(question: String)
    suspend fun question(question: String)
}