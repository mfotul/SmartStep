package com.example.smartstep.smart.data.step

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import com.example.smartstep.BuildConfig
import com.example.smartstep.smart.domain.step.AiCoach
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeminiAiCoach(
    private val stepTrackerManager: StepTrackerManager
) : AiCoach {

    private val agent = AIAgent(
        promptExecutor = simpleGoogleAIExecutor(BuildConfig.GOOGLE_API_KEY),
        systemPrompt = """You are a friendly fitness coach inside a step tracking app,
            be motivating or analytic:
                1. interpret current activity state
                2. do not medical advice
                3. do not repeat raw values
                """.trimIndent(),
        llmModel = GoogleModels.Gemini2_5Flash,
        temperature = 0.7,
    )

    override fun getActivityState(): Flow<String> {
        return flow {
            val currentSteps = stepTrackerManager.data.value.steps
            val goal = stepTrackerManager.data.value.goal
            val percent = (currentSteps / goal.toFloat() * 100).toInt()
            emit(
                agent.run("""
                Current activity state:
                Steps: $currentSteps
                Goal: $goal
                Percent: $percent
                Generate coaching message.""".trimIndent()
                )
            )
        }
    }
}