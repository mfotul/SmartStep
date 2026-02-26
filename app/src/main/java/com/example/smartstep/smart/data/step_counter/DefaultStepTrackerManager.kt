package com.example.smartstep.smart.data.step_counter

import android.os.SystemClock
import com.example.smartstep.settings.domain.Profile
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.domain.profile.Gender
import com.example.smartstep.smart.domain.step_counter.StepTrackerData
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
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
        settingPreferences.observeProfileSettings(),
        settingPreferences.observeGoal(),
    ) { data, profile, goal ->
        Triple(data, profile, goal)
    }.scan(MetricAccumulator()) { accumulator, (data, profile, goal) ->
        val shouldRecalculate = accumulator.lastCalculatedSteps == -1f ||
                (accumulator.lastCalculatedSteps + 10 < data.steps || accumulator.lastCalculatedSteps - 10 > data.steps)

        if (shouldRecalculate) {
            val (newDistance, newCalories) = calculateActivityMetrics(data.steps, profile)
            MetricAccumulator(
                lastCalculatedSteps = data.steps,
                distance = newDistance,
                calories = newCalories,
                data = data.copy(
                    distance = newDistance,
                    calories = newCalories,
                    goal = goal
                )
            )
        } else
            accumulator.copy(
                data = data.copy(
                    distance = accumulator.distance,
                    calories = accumulator.calories,
                    goal = goal
                )
            )
    }.map {
        it.data
    }.stateIn(
        scope = applicationScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StepTrackerData()
    )

    private fun calculateActivityMetrics(
        steps: Float,
        profile: Profile,
    ): Pair<Float, Float> {
        val stepLength = profile.height * 0.415
        val distance = steps * stepLength / 100

        val gender = Gender.valueOf(profile.gender)
        val genderFactor = if (gender == Gender.FEMALE) 0.9f else 1f
        val kCalPerStep = profile.weight * 0.0005 * genderFactor
        val calories = steps * kCalPerStep

        return Pair(distance.toFloat(), calories.toFloat())
    }

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
}