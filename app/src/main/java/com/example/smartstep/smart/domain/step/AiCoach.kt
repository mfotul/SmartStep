package com.example.smartstep.smart.domain.step

import kotlinx.coroutines.flow.Flow

interface AiCoach {
    fun getActivityState(): Flow<String>
}