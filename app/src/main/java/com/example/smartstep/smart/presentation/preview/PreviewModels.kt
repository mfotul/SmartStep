package com.example.smartstep.smart.presentation.preview

import com.example.smartstep.smart.presentation.chat.ChatState
import com.example.smartstep.smart.domain.step.ChatMessage
import com.example.smartstep.smart.presentation.models.Units
import com.example.smartstep.smart.presentation.step.StepState
import com.example.smartstep.smart.presentation.step.models.StepUi

data object PreviewModels {
    val stepState = StepState(
        goal = 1000,
        steps = 0f,
        distance = "3.2",
        calories = "3",
        activeMinutes = "2",
        isPaused = false,
        units = Units.KG,
        isBackgroundAccessEnabled = false,
        weeklyStats = listOf(
            StepUi(
                day = "Mon",
                steps = 0f,
                dailyGoal = 100
            ),
            StepUi(
                day = "Tue",
                steps = 0f,
                dailyGoal = 100
            ),
            StepUi(
                day = "Wen",
                steps = 0f,
                dailyGoal = 100
            ),
            StepUi(
                day = "Thu",
                steps = 0f,
                dailyGoal = 100
            ),
            StepUi(
                day = "Fri",
                steps = 0f,
                dailyGoal = 100
            ),
            StepUi(
                day = "Sat",
                steps = 0f,
                dailyGoal = 100
            ),
            StepUi(
                day = "Sun",
                steps = 0f,
                dailyGoal = 100
            ),
        ),
        averageDailySteps = 1000
    )

    val chatState = ChatState(
        messages = listOf(
            ChatMessage(
                message = "Hi, how can I help you?",
                isUser = false
            ),
            ChatMessage(
                message = "How am i doing so far ?",
                isUser = true
            ),
            ChatMessage(
                message = "I'm sorry, I don't have the answer to that question.",
                isUser = false
            ),
            ChatMessage(
                message = "Hi, how can I help you?",
                isUser = false
            ),
            ChatMessage(
                message = "How am i doing so far ?",
                isUser = true
            ),
            ChatMessage(
                message = "I'm sorry, I don't have the answer to that question.",
                isUser = false
            ),
            ChatMessage(
                message = "Hi, how can I help you?",
                isUser = false
            ),
            ChatMessage(
                message = "How am i doing so far ?",
                isUser = true
            ),
            ChatMessage(
                message = "I'm sorry, I don't have the answer to that question.",
                isUser = false
            ),
            ChatMessage(
                message = "Hi, how can I help you?",
                isUser = false
            ),
            ChatMessage(
                message = "How am i doing so far ?",
                isUser = true
            ),
            ChatMessage(
                message = "I'm sorry, I don't have the answer to that question.",
                isUser = false
            ),
            ChatMessage(
                message = "Hi, how can I help you?",
                isUser = false
            ),
            ChatMessage(
                message = "How am i doing so far ?",
                isUser = true
            ),
            ChatMessage(
                message = "I'm sorry, I don't have the answer to that question.",
                isUser = false
            ),
        )
    )
}