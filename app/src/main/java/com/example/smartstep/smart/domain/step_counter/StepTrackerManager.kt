package com.example.smartstep.smart.domain.step_counter

import kotlinx.coroutines.flow.StateFlow

interface StepTrackerManager {
    val data: StateFlow<StepTrackerData>
    fun updateSteps(count: Float)
    fun editSteps(newSteps: Float)
    fun reset()
    fun pause()
    fun currentStep(): Int
}