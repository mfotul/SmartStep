package com.example.smartstep.smart.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.smart.domain.step.StepDatasource
import com.example.smartstep.smart.presentation.preview.PreviewModels
import com.example.smartstep.smart.presentation.report.model.DailyData
import com.example.smartstep.smart.presentation.report.model.DataSource
import com.example.smartstep.smart.presentation.report.model.DataType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ReportViewModel(
    private val stepDatasource: StepDatasource
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
        val today = LocalDate.now()
        val week = getWeek(0)
        val monday = week.entries.first().key
        val sunday = week.entries.last().key
        val mondayInstant = monday.atStartOfDay(ZoneId.of("UTC")).toInstant()
        val sundayInstant = sunday.atStartOfDay(ZoneId.of("UTC")).toInstant()

        stepDatasource.observeFromToSteps(
            from = mondayInstant,
            to = sundayInstant
        ).onEach { steps ->
            val stepsMap = steps.associateBy { it.date.atZone(ZoneId.of("UTC")).toLocalDate() }
            val weekData = week.map { day ->
                val steps = stepsMap[day.key]
                val dataSource = when {
                    day == today -> DataSource.CURRENT_DATE
                    steps != null -> DataSource.DB
                    else -> DataSource.NONE
                }
                DailyData(
                    date = day.value,
                    value = steps?.steps?.toInt()?.toString() ?: "0",
                    goal = steps?.dailyGoal?.toString() ?: "0",
                    dataSource = dataSource,
                    dataType = DataType.STEPS
                )
            }

            val cardValue = steps.sumOf { it.steps.toInt() }
            val cardAverageValue = cardValue / 7

            _state.update {
                it.copy(
                    cardValue = cardValue,
                    cardDailyAverageValue = cardAverageValue,
                    weekData = weekData,
                )
            }

        }.launchIn(viewModelScope)
    }

    fun onAction(action: ReportAction) {
        when (action) {
            is ReportAction.OnDataTypeChange -> onDataTypeChange(action.dataType)

            else -> {}
        }
    }

    private fun onDataTypeChange(dataType: DataType) {
        _state.update { it.copy(selectedDataType = dataType) }
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
}