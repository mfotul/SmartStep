package com.example.smartstep.smart.presentation.preview

import com.example.smartstep.smart.presentation.chat.ChatState
import com.example.smartstep.smart.domain.step.ChatMessage
import com.example.smartstep.smart.presentation.models.Units
import com.example.smartstep.smart.presentation.report.ReportState
import com.example.smartstep.smart.presentation.report.model.DailyData
import com.example.smartstep.smart.presentation.report.model.DataSource
import com.example.smartstep.smart.presentation.report.model.DataType
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

    val reportState = ReportState(
        cardValue = 1000,
        cardDailyAverageValue = 1000,
        week = "Nov 16 - Nov 22",
        isLeftArrowEnabled = true,
        isRightArrowEnabled = false,
        weekData = listOf(
            DailyData(
                date = "Monday",
                value = "1000",
                goal = "1000",
                dataSource = DataSource.DB,
                dataType = DataType.STEPS
            ),
            DailyData(
                date = "Tuesday",
                value = "1000",
                goal = "1000",
                dataSource = DataSource.DB,
                dataType = DataType.STEPS
            ),
            DailyData(
                date = "Wednesday",
                value = "0",
                goal = "1000",
                dataSource = DataSource.CURRENT_DATE,
                dataType = DataType.STEPS
            ),
            DailyData(
                date = "Thursday",
                value = "1000",
                goal = "1000",
                dataSource = DataSource.NONE,
                dataType = DataType.STEPS
            ),
            DailyData(
                date = "Friday",
                value = "1000",
                goal = "1000",
                dataSource = DataSource.NONE,
                dataType = DataType.STEPS
            ),
            DailyData(
                date = "Saturday",
                value = "1000",
                goal = "1000",
                dataSource = DataSource.NONE,
                dataType = DataType.STEPS
            ),
            DailyData(
                date = "Sunday",
                value = "1000",
                goal = "1000",
                dataSource = DataSource.NONE,
                dataType = DataType.STEPS
            ),
        )
    )
}