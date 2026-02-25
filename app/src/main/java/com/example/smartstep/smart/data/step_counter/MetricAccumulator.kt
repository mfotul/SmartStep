package com.example.smartstep.smart.data.step_counter

import com.example.smartstep.smart.domain.step_counter.StepTrackerData

data class MetricAccumulator(
    val lastCalculatedSteps: Float = -1f,
    val distance: Float = 0f,
    val calories: Float = 0f,
    val data: StepTrackerData = StepTrackerData()
)
