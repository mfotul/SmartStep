package com.example.smartstep.smart.domain.step

import java.time.Instant

data class Step(
    val date: Instant,
    val steps: Float,
    val dailyGoal: Int,
)
