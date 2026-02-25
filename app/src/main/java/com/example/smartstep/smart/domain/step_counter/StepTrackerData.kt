package com.example.smartstep.smart.domain.step_counter

data class StepTrackerData(
    val steps: Float = 0f,
    val stepOffset: Float = 0f,
    val calories: Float = 0f,
    val distance: Float =0f,
    val activeMillis: Long = 0L,
    val lastStepTime: Long = 0L,
    val goal: Int = 0,
    val isPaused: Boolean = false
)
