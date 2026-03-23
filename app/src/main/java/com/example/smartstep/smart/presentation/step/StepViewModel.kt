package com.example.smartstep.smart.presentation.step

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.domain.step.AiCoach
import com.example.smartstep.smart.domain.step.ConnectivityObserver
import com.example.smartstep.smart.domain.step.Step
import com.example.smartstep.smart.domain.step.StepDatasource
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import com.example.smartstep.smart.presentation.models.Units
import com.example.smartstep.smart.presentation.step.models.AiResult
import com.example.smartstep.smart.presentation.step.models.DateInput
import com.example.smartstep.smart.presentation.step.models.Dialog
import com.example.smartstep.smart.presentation.step.models.Permission
import com.example.smartstep.smart.presentation.step.models.StepUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class StepViewModel(
    private val settingPreferences: SettingPreferences,
    private val stepTrackerManager: StepTrackerManager,
    private val stepDatasource: StepDatasource,
    private val connectivityObserver: ConnectivityObserver,
    private val aiCoach: AiCoach
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(StepState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StepState()
        )

    private val eventChannel = Channel<StepEvent>()
    val events = eventChannel.receiveAsFlow()

    val goalTextFieldState = TextFieldState()

    private fun loadInitialData() {
        val weekEndingToday = getWeekEndingTodayMap()
        combine(
            settingPreferences.observeBackgroundAccessDisabled(),
            settingPreferences.observeProfileSettings(),
            stepTrackerManager.data,
            stepDatasource.observeLast7Steps(),
            aiCoach.chats
        ) { backgroundAccessDisabled, profile, data, steps, chats ->
            val stepsMap = steps.associateBy { it.date.atZone(ZoneId.of("UTC")).toLocalDate() }
            val weeklyStats = weekEndingToday.entries.mapIndexed { index, day ->
                val step = stepsMap[day.key]
                if (index == 6)
                    StepUi(
                        day = day.value,
                        steps = data.steps,
                        dailyGoal = data.goal
                    )
                else
                    StepUi(
                        day = day.value,
                        steps = step?.steps ?: 0f,
                        dailyGoal = step?.dailyGoal ?: 0
                    )
            }
            val units = Units.valueOf(profile.units)
            val averageDailySteps = (steps.sumOf { it.steps.toDouble() } / 7).toInt()
            val chat = chats.firstOrNull()
            val distance =
                if (units == Units.CM) data.distance / 1000f else data.distance / 1609.34f
            _state.update {
                it.copy(
                    isBackgroundAccessEnabled = backgroundAccessDisabled,
                    goal = data.goal,
                    steps = data.steps,
                    distance = "%.1f".format(distance),
                    calories = "%.0f".format(data.calories),
                    activeMinutes = data.activeMillis.milliseconds.inWholeMinutes.toString(),
                    units = units,
                    weeklyStats = weeklyStats,
                    averageDailySteps = averageDailySteps,
                    aiResult = if (chat != null)
                        AiResult.Success(chat.message)
                    else
                        AiResult.Loading
                )
            }
        }.flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)

        connectivityObserver
            .observer()
            .onEach { connectivityObserverStatus ->
                if (connectivityObserverStatus == ConnectivityObserver.Status.Lost)
                    _state.update { state -> state.copy(isOffline = true) }
                else
                    aiCoachMessage()
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: StepAction) {
        when (action) {
            is StepAction.OnSetPermission -> onSetPermission(action.permission)
            StepAction.OnBackgroundAccessDisabled -> onBackgroundAccessDisabled()
            StepAction.OnFixStopCountingStepIssueClick -> onFixStopCountingStepIssueClick()
            is StepAction.OnDialogOpen -> onDialogOpen(action.dialog)
            StepAction.OnPersonalSettingsClick -> onPersonalSettingsClick()
            StepAction.OnExitConfirmClick -> onExitConfirmClick()
            is StepAction.OnStepGoalSelected -> onStepGoalSelected(action.goal)
            StepAction.OnStepGoalSave -> onStepGoalSave()
            is StepAction.OnDateSelected -> onDateSelected(action.dateInput)
            StepAction.OnEditStepsSave -> onEditStepsSave()
            StepAction.OnResetStepsConfirmClick -> onResetStepsConfirmClick()
            StepAction.OnPauseClick -> onPauseClick()
            StepAction.OnTryAgainClick -> onTryAgainClick()
            StepAction.OnMoreClick, StepAction.OnReportClick -> {}
        }
    }


    private fun onSetPermission(permission: Permission) {
        viewModelScope.launch {
            if (permission == Permission.ALLOWED_ALL)
                settingPreferences.saveBackgroundAccessDisabled(false)
            _state.update {
                it.copy(
                    permission = permission
                )
            }
        }
    }

    private fun onBackgroundAccessDisabled() {
        viewModelScope.launch {
            settingPreferences.saveBackgroundAccessDisabled(true)
        }
    }

    private fun onFixStopCountingStepIssueClick() {
        viewModelScope.launch {
            eventChannel.send(StepEvent.OnNavSheetClose)
            settingPreferences.saveBackgroundAccessDisabled(false)
            onSetPermission(Permission.ALLOWED_REQUIRED_PERMISSIONS)
        }
    }

    private fun onDialogOpen(dialog: Dialog) {
        viewModelScope.launch {
            eventChannel.send(StepEvent.OnNavSheetClose)
            _state.update {
                when (dialog) {
                    Dialog.GOAL_PICKER -> it.copy(
                        dialogVisible = Dialog.GOAL_PICKER,
                        goalSelected = it.goal
                    )

                    Dialog.EDIT_STEPS -> it.copy(dialogVisible = Dialog.EDIT_STEPS)
                    Dialog.EDIT_STEP_WITH_DATE -> it.copy(dialogVisible = Dialog.EDIT_STEP_WITH_DATE)
                    Dialog.RESET_TODAY_STEPS -> it.copy(dialogVisible = Dialog.RESET_TODAY_STEPS)
                    Dialog.EXIT -> it.copy(dialogVisible = Dialog.EXIT)
                    Dialog.NONE -> it.copy(dialogVisible = Dialog.NONE)
                }
            }
        }
    }

    private fun onPersonalSettingsClick() {
        viewModelScope.launch {
            eventChannel.send(StepEvent.OnProfileScreenClick)
        }
    }

    private fun onExitConfirmClick() {
        viewModelScope.launch {
            eventChannel.send(StepEvent.OnExitClick)
        }
    }

    private fun onStepGoalSelected(goal: Int) {
        _state.update {
            it.copy(goalSelected = goal)
        }
    }

    private fun onStepGoalSave() {
        viewModelScope.launch {
            _state.update {
                settingPreferences.saveGoal(it.goalSelected)
                it.copy(dialogVisible = Dialog.NONE)
            }
        }
    }

    private fun onEditStepsSave() {
        viewModelScope.launch {
            _state.update {
                val currentDate = Instant.now().truncatedTo(ChronoUnit.DAYS)
                val date = it.date.toInstant()
                val steps = goalTextFieldState.text.toString().toFloatOrNull() ?: 0f
                if (currentDate == date)
                    stepTrackerManager.editSteps(steps)
                stepDatasource.upsertStep(
                    Step(
                        date = date,
                        steps = steps,
                        dailyGoal = it.goal
                    )
                )
                it.copy(dialogVisible = Dialog.NONE)
            }
            aiCoachMessage()
        }
    }

    private fun onDateSelected(dateInput: DateInput) {
        runCatching {
            LocalDate.of(dateInput.year, dateInput.month, dateInput.day)
                .atStartOfDay(ZoneId.of("UTC"))
        }.getOrNull()?.let { date ->
            _state.update { it.copy(date = date, dialogVisible = Dialog.EDIT_STEPS) }
        }
    }

    private fun onResetStepsConfirmClick() {
        _state.update {
            stepTrackerManager.reset()
            it.copy(
                dialogVisible = Dialog.NONE
            )
        }

    }

    private fun onPauseClick() {
        _state.update {
            stepTrackerManager.pause()
            it.copy(
                isPaused = !it.isPaused
            )
        }
    }

    private fun getWeekEndingTodayMap(): Map<LocalDate, String> {
        return (6 downTo 0).map { dayInPast ->
            LocalDate.now().minusDays(dayInPast.toLong())
        }.associateWith { date ->
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
        }
    }

    private fun onTryAgainClick() {
        viewModelScope.launch {
            val status = connectivityObserver.observer().first()
            if (status != ConnectivityObserver.Status.Lost) {
                _state.update { it.copy(isOffline = false) }
                aiCoachMessage()
            }
        }
    }

    private suspend fun aiCoachMessage() {
        _state.update { it.copy(aiResult = AiResult.Loading) }
        aiCoach.run("Check my stats and tell me short how I'm doing")
    }
}
