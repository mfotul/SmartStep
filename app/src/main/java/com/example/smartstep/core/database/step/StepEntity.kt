package com.example.smartstep.core.database.step

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StepEntity(
    @PrimaryKey val date: Long,
    val steps: Float,
    val dailyGoal: Int,
)
