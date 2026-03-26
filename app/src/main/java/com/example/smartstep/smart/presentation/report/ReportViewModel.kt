@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.smartstep.smart.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.settings.domain.Profile
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.domain.step.Step
import com.example.smartstep.smart.domain.step.StepDatasource
import com.example.smartstep.smart.domain.step_counter.StepTrackerData
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import com.example.smartstep.smart.presentation.report.model.DailyData
import com.example.smartstep.smart.presentation.report.model.DataSource
import com.example.smartstep.smart.presentation.report.model.DataType
import com.example.smartstep.smart.presentation.util.calculateCaloriesFromSteps
import com.example.smartstep.smart.presentation.util.calculateDistanceFromSteps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ReportViewModel(
    private val stepDatasource: StepDatasource,
    private val settingPreferences: SettingPreferences,
    private val stepTrackerManager: StepTrackerManager
) : ViewModel() {
    var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ReportState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReportState()
        )


    private fun loadInitialData() {
        state
            .distinctUntilChanged { old, new ->
                old.minusWeeks == new.minusWeeks && old.selectedDataType == new.selectedDataType
            }
            .flatMapLatest { state ->
                val today = LocalDate.now()
                val week = getWeek(state.minusWeeks)
                val monday = week.entries.first().key
                val sunday = week.entries.last().key
                val mondayInstant = monday.atStartOfDay(ZoneId.of("UTC")).toInstant()
                val sundayInstant = sunday.atStartOfDay(ZoneId.of("UTC")).toInstant()

                if (today in monday..sunday)
                    combine(
                        stepDatasource.observeFromToSteps(mondayInstant, sundayInstant),
                        settingPreferences.observeProfileSettings(),
                        stepTrackerManager.data
                    ) { steps, profile, stepTrackerData ->
                        getNewState(
                            today = today,
                            week = week,
                            state = state,
                            profile = profile,
                            steps = steps,
                            stepTrackerData = stepTrackerData
                        )
                    }
                else
                    combine(
                        stepDatasource.observeFromToSteps(mondayInstant, sundayInstant),
                        settingPreferences.observeProfileSettings(),
                    ) { steps, profile ->
                        getNewState(
                            today = today,
                            week = week,
                            state = state,
                            profile = profile,
                            steps = steps
                        )
                    }
            }.onEach { newState ->
                _state.update {
                    newState
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ReportAction) {
        when (action) {
            ReportAction.OnBackClick -> {}
            is ReportAction.OnDataTypeChange -> onDataTypeChange(action.dataType)
            ReportAction.OnLeftArrowClick -> onLeftArrowClick()
            ReportAction.OnRightArrowClick -> onRightArrowClick()
        }
    }

    private fun onDataTypeChange(dataType: DataType) {
        _state.update { it.copy(selectedDataType = dataType) }
    }

    private fun onLeftArrowClick() {
        _state.update { it.copy(minusWeeks = it.minusWeeks + 1, isRightArrowEnabled = true) }
    }

    private fun onRightArrowClick() {
        _state.update {
            if (it.minusWeeks == 0L) return@update it
            val isRightArrowEnabled = it.minusWeeks > 1
            it.copy(minusWeeks = it.minusWeeks - 1, isRightArrowEnabled = isRightArrowEnabled)
        }
    }

    private fun getWeek(minusWeeks: Long): Map<LocalDate, String> {
        val monday = LocalDate.now()
            .minusWeeks(minusWeeks)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        return (0..6).map { day ->
            monday.plusDays(day.toLong())
        }.associateWith { day ->
            day.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
        }
    }

    private fun convertMillisecondsToMinutes(milliseconds: Long): Float {
        if (milliseconds == 0L) return 0f
        val minutes = milliseconds / 60000
        return minutes.toFloat()
    }

    private fun getDailyData(
        today: LocalDate,
        day: Map.Entry<LocalDate, String>,
        state: ReportState,
        profile: Profile,
        steps: List<Step>,
        stepTrackerData: StepTrackerData? = null
    ): DailyData {
        val stepsMap =
            steps.associateBy { it.date.atZone(ZoneId.of("UTC")).toLocalDate() }

        val step = stepsMap[day.key]
        val dataSource = when {
            day.key == today -> DataSource.CURRENT_DATE
            step != null -> DataSource.DB
            else -> DataSource.NONE
        }

        val value = if (day.key == today && stepTrackerData != null) {
            when (state.selectedDataType) {
                DataType.STEPS -> stepTrackerData.steps
                DataType.CALORIES ->
                    calculateCaloriesFromSteps(stepTrackerData.steps, profile)

                DataType.TIME ->
                    convertMillisecondsToMinutes(stepTrackerData.activeMillis)

                DataType.DISTANCE -> calculateDistanceFromSteps(
                    stepTrackerData.steps,
                    profile
                )
            }
        } else
            when (state.selectedDataType) {
                DataType.STEPS -> step?.steps ?: 0f
                DataType.CALORIES ->
                    calculateCaloriesFromSteps(step?.steps ?: 0f, profile)

                DataType.TIME -> step?.let {
                    convertMillisecondsToMinutes(it.activeMillis)
                } ?: 0f

                DataType.DISTANCE -> calculateDistanceFromSteps(
                    step?.steps ?: 0f,
                    profile
                )
            }

        val goal = if (day.key == today && stepTrackerData != null)
            stepTrackerData.goal
        else
            step?.dailyGoal ?: 0

        return DailyData(
            date = day.value,
            value = value,
            goal = goal,
            dataSource = dataSource,
            dataType = state.selectedDataType
        )
    }

    private fun getCardValue(
        steps: List<Step>,
        dataType: DataType,
        profile: Profile,
        stepTrackerData: StepTrackerData? = null
    ): Float {
        val stepsPerWeek =
            steps.sumOf { it.steps.toDouble() } + (stepTrackerData?.steps ?: 0f)

        return when (dataType) {
            DataType.STEPS -> stepsPerWeek.toFloat()
            DataType.CALORIES -> calculateCaloriesFromSteps(
                stepsPerWeek.toFloat(),
                profile
            )

            DataType.TIME -> convertMillisecondsToMinutes(
                steps.sumOf { it.activeMillis } + (stepTrackerData?.activeMillis ?: 0L)
            )

            DataType.DISTANCE -> calculateDistanceFromSteps(
                stepsPerWeek.toFloat(),
                profile
            )
        }
    }

    private fun getNewState(
        today: LocalDate,
        week: Map<LocalDate, String>,
        state: ReportState,
        profile: Profile,
        steps: List<Step>,
        stepTrackerData: StepTrackerData? = null
    ): ReportState {
        val formatter = DateTimeFormatter.ofPattern("MMM dd")
        val monday = week.entries.first().key
        val sunday = week.entries.last().key

        val weekData = week.map { day ->
            getDailyData(
                today = today,
                day = day,
                state = state,
                profile = profile,
                steps = steps,
                stepTrackerData = stepTrackerData
            )
        }

        val cardValue = getCardValue(steps, state.selectedDataType, profile, stepTrackerData)

        return state.copy(
            cardValue = cardValue,
            cardDailyAverageValue = cardValue / 7,
            weekData = weekData,
            weekTitle = monday.format(formatter) + " - " + sunday.format(formatter)
        )
    }
}