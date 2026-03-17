package com.example.smartstep.smart.data.step

import ai.koog.agents.core.agent.AIAgentService
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.agent.functionalStrategy
import ai.koog.agents.core.dsl.extension.asAssistantMessage
import ai.koog.agents.core.dsl.extension.containsToolCalls
import ai.koog.agents.core.dsl.extension.executeMultipleTools
import ai.koog.agents.core.dsl.extension.extractToolCalls
import ai.koog.agents.core.dsl.extension.requestLLMMultiple
import ai.koog.agents.core.dsl.extension.sendMultipleToolResults
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.tool
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.executor.ollama.client.OllamaModels
import ai.koog.prompt.params.LLMParams
import com.example.smartstep.BuildConfig
import com.example.smartstep.smart.domain.step.AiCoach
import com.example.smartstep.smart.domain.step.ChatMessage
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.update
import java.time.LocalTime

class OllamaAiCoach(
    private val stepTrackerManager: StepTrackerManager,
) : AiCoach {
    private val _chats = MutableStateFlow<List<ChatMessage>>(emptyList())
    override val chats = _chats.asStateFlow()

    private val _question = MutableStateFlow("")
    private val question = _question.asStateFlow()

    private val agentConfig = AIAgentConfig(
        prompt = prompt(
            id = "assistant",
            params = LLMParams(
                temperature = 0.0
            )
        ) {
            system(
                """
                You are a high-energy, witty fitness coach.
                
                IMPORTANT: Never call any functions except getUserData(), 
                there are no others functions.
                
                STRICTURES:
                1. NEVER EVER repeat raw numbers !!!
                2. Use analogies based on the 'Percent' and 'Time of day' provided.
                3. Keep it to 1-2 punchy sentences. 
                4. NO medical advice.
                5. NO json, yml, etc.

                TONE: Punchy, modern, and slightly competitive.
            """.trimIndent()
            )
        },
        model = OllamaModels.Meta.LLAMA_3_2_3B,
        maxAgentIterations = 10
    )

    private val agent = AIAgentService<String, Unit>(
        promptExecutor = simpleOllamaAIExecutor(BuildConfig.OLLAMA_BASEURL),
        agentConfig = agentConfig,
        toolRegistry = ToolRegistry {
            tool(::getUserData)
        },
        strategy = functionalStrategy { input ->
            var firstResponses = requestLLMMultiple(input)

            while (firstResponses.containsToolCalls()) {
                val pendingCalls = extractToolCalls(firstResponses)
                val results = executeMultipleTools(pendingCalls)
                firstResponses = sendMultipleToolResults(results)
            }

            val message = firstResponses.single().asAssistantMessage().content
            _chats.update {
                it + ChatMessage(
                    message = message,
                    isUser = false
                )
            }

            question.filterNot { it.isEmpty() }.collect { question ->
                var otherResponses = requestLLMMultiple(question)

                while (otherResponses.containsToolCalls()) {
                    val pendingCalls = extractToolCalls(otherResponses)
                    val results = executeMultipleTools(pendingCalls)
                    otherResponses = sendMultipleToolResults(results)
                }

                val message = otherResponses.single().asAssistantMessage().content
                _chats.update {
                    it + ChatMessage(
                        message = message,
                        isUser = false
                    )
                }
            }
        }
    )

    @Tool
    @LLMDescription("Ask user for current activity state")
    fun getUserData(): String {
        val currentSteps = stepTrackerManager.data.value.steps
        val goal = stepTrackerManager.data.value.goal
        val percent = (currentSteps / goal.toFloat() * 100).toInt()
        val timeOfDay = timeOfDay()
        return """
        Current activity state:
                Steps: $currentSteps
                Goal: $goal
                Percent: $percent
                Time of day: $timeOfDay
    """.trimIndent()
    }

    private fun timeOfDay(): String {
        val hour = LocalTime.now().hour
        return when (hour) {
            in 5..11 -> "morning"
            in 12..16 -> "afternoon"
            in 17..20 -> "evening"
            else -> "night"
        }
    }

    override suspend fun run(question: String) {
        try {
            _chats.update {
                emptyList()
            }
            agent.createAgentAndRun(question)
        } catch (e: Exception) {
            _chats.update{
                listOf(ChatMessage(
                    message = "Something went wrong",
                    isUser = false
                ))
            }
            e.printStackTrace()
        }
    }

    override suspend fun question(question: String) {
        _question.update {
            question
        }
        _chats.update {
            it + ChatMessage(
                message = question,
                isUser = true
            )
        }
    }
}

