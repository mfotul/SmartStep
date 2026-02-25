package com.example.smartstep.smart.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import com.example.smartstep.smart.presentation.home.models.DateInput
import com.example.smartstep.smart.presentation.home.models.Dialog
import com.example.smartstep.smart.presentation.home.models.Permission
import com.example.smartstep.smart.presentation.models.Units
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(
    private val settingPreferences: SettingPreferences,
    private val stepTrackerManager: StepTrackerManager,
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomeState())
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
            initialValue = HomeState()
        )

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    val goalTextFieldState = TextFieldState()

    private fun loadInitialData() {
        combine(
            settingPreferences.observeBackgroundAccessDisabled(),
            settingPreferences.observeProfileSettings(),
            stepTrackerManager.data
        ) { backgroundAccessDisabled, profile, data ->
            val units = Units.valueOf(profile.units)
            _state.update {
                val distance = if (units == Units.CM) data.distance / 1000f else data.distance / 1609.34f
                it.copy(
                    isBackgroundAccessEnabled = backgroundAccessDisabled,
                    goal = data.goal,
                    steps = data.steps,
                    distance = "%.1f".format(distance),
                    calories = "%.0f".format(data.calories),
                    activeMinutes = data.activeMillis.milliseconds.inWholeMinutes.toString(),
                    units = units
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnSetPermission -> onSetPermission(action.permission)
            HomeAction.OnBackgroundAccessDisabled -> onBackgroundAccessDisabled()
            HomeAction.OnFixStopCountingStepIssueClick -> onFixStopCountingStepIssueClick()
            is HomeAction.OnDialogOpen -> onDialogOpen(action.dialog)
            HomeAction.OnPersonalSettingsClick -> onPersonalSettingsClick()
            HomeAction.OnExitConfirmClick -> onExitConfirmClick()
            is HomeAction.OnStepGoalSelected -> onStepGoalSelected(action.goal)
            HomeAction.OnStepGoalSave -> onStepGoalSave()
            is HomeAction.OnDateSelected -> onDateSelected(action.dateInput)
            HomeAction.OnEditStepsSave -> onEditStepsSave()
            HomeAction.OnResetStepsConfirmClick -> onResetStepsConfirmClick()
            HomeAction.OnPauseClick -> onPauseClick()
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
            eventChannel.send(HomeEvent.OnNavSheetClose)
            settingPreferences.saveBackgroundAccessDisabled(false)
            onSetPermission(Permission.ALLOWED_REQUIRED_PERMISSIONS)
        }
    }

    private fun onDialogOpen(dialog: Dialog) {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OnNavSheetClose)
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
            eventChannel.send(HomeEvent.OnProfileScreenClick)
        }
    }

    private fun onExitConfirmClick() {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OnExitClick)
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
                it.copy(
                    dialogVisible = Dialog.NONE,
                )
            }
        }
    }

    private fun onEditStepsSave() {
        _state.update {
            it.copy(
                dialogVisible = Dialog.NONE
            )
        }
    }

    private fun onDateSelected(dateInput: DateInput) {
        runCatching {
            LocalDate.of(dateInput.year, dateInput.month, dateInput.day)
                .atStartOfDay(ZoneId.systemDefault())
        }.getOrNull()?.let { time ->
            _state.update {
                it.copy(
                    date = time,
                    dialogVisible = Dialog.EDIT_STEPS
                )
            }
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
}
