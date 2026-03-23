package com.example.smartstep.core.presentation.util

import java.util.Calendar

fun calculateInitialTimeDelay(): Long {
    val dueDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 15)
        set(Calendar.SECOND, 0)
    }

    val currentDate = Calendar.getInstance()

    if (dueDate.before(currentDate)) {
        dueDate.add(Calendar.HOUR_OF_DAY, 24)
    }

    return dueDate.timeInMillis - currentDate.timeInMillis
}