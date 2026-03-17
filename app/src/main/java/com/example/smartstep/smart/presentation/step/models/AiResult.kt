package com.example.smartstep.smart.presentation.step.models

sealed interface AiResult {
    data class Success(val result: String) : AiResult
    data object Loading : AiResult
}