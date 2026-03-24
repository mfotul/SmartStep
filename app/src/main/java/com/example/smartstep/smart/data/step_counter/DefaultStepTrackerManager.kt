package com.example.smartstep.smart.data.step_counter

import android.os.SystemClock
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.domain.step_counter.StepTrackerData
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import com.example.smartstep.smart.presentation.util.calculateCaloriesFromSteps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

private const val ACTIVE_THRESHOLD = 15_000L

class DefaultStepTrackerManager(
    private val settingPreferences: SettingPreferences,
    applicationScope: CoroutineScope
) : StepTrackerManager {
    private val _data = MutableStateFlow(StepTrackerData())

    override val data = combine(
        _data,
        settingPreferences.observeGoal(),
        settingPreferences.observeProfileSettings()
    ) { data, goal, profile ->
        val calories = calculateCaloriesFromSteps(data.steps, profile)
        data.copy(
            goal = goal,
            calories = calories
        )
    }.stateIn(
        scope = applicationScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StepTrackerData()
    )


    override fun updateSteps(count: Float) {
        _data.update { data ->
            val currentTime = SystemClock.elapsedRealtime()
            var newActiveMillis = data.activeMillis
            var newOffset = data.stepOffset
            var newLastStepTime = data.lastStepTime


            if (newLastStepTime != 0L) {
                val delta = currentTime - newLastStepTime
                if (delta < ACTIVE_THRESHOLD && !data.isPaused)
                    newActiveMillis += delta
            }
            newLastStepTime = currentTime

            if (newOffset == 0f)
                newOffset = count

            if (data.isPaused)
                newOffset += count - newOffset - data.steps

            data.copy(
                steps = count - newOffset,
                activeMillis = newActiveMillis,
                lastStepTime = newLastStepTime,
                stepOffset = newOffset
            )
        }
    }

    override fun editSteps(newSteps: Float) {
        _data.update { data ->
            val totalSteps = data.steps + data.stepOffset
            data.copy(
                steps = newSteps,
                stepOffset = totalSteps - newSteps
            )
        }
    }

    override fun reset() {
        _data.update {
            StepTrackerData()
        }
    }

    override fun pause() {
        _data.update {
            it.copy(isPaused = !it.isPaused)
        }
    }

    override fun currentStep(): Int = data.value.steps.toInt()
}