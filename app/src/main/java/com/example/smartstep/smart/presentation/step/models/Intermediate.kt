package com.example.smartstep.smart.presentation.step.models

import com.example.smartstep.settings.domain.Profile
import com.example.smartstep.smart.domain.step.Step
import com.example.smartstep.smart.domain.step_counter.StepTrackerData

data class Intermediate(
    val backgroundAccessDisabled: Boolean,
    val profile: Profile,
    val data: StepTrackerData,
    val steps: List<Step>,
)
