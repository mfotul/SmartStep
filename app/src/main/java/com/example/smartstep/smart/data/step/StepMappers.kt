package com.example.smartstep.smart.data.step

import com.example.smartstep.core.database.step.StepEntity
import com.example.smartstep.smart.domain.step.Step
import java.time.Instant

fun StepEntity.toStep(): Step {
    return Step(
        date = Instant.ofEpochMilli(date),
        steps = steps,
        dailyGoal = dailyGoal
    )
}

fun Step.toStepEntity(): StepEntity {
    return StepEntity(
        date = date.toEpochMilli(),
        steps = steps,
        dailyGoal = dailyGoal
    )
}