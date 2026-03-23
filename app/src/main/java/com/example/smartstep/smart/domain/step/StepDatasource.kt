package com.example.smartstep.smart.domain.step

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface StepDatasource {
    fun observeLast7Steps(): Flow<List<Step>>
    fun observeFromToSteps(from: Instant, to: Instant): Flow<List<Step>>
    fun getStepsByDate(date: Instant): Flow<List<Step>>
    suspend fun upsertStep(step: Step)
}