package com.example.smartstep.smart.presentation.home

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.presentation.home.models.Permission
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

class HomeViewModel(
    private val settingPreferences: SettingPreferences,
    application: Application
) : ViewModel(), SensorEventListener {
    private var hasLoadedInitialData = false
    private var stepOffset: Float? = null
    private var isListening = false

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

    private val sensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val detectSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    private fun loadInitialData() {
        combine(
            settingPreferences.observeBackgroundAccessDisabled(),
            settingPreferences.observeGoal()
        ) { backgroundAccessDisabled, goal ->
            _state.update {
                it.copy(
                    isBackgroundAccessEnabled = backgroundAccessDisabled,
                    goal = goal
                )
            }

        }.launchIn(viewModelScope)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                val totalStepsSinceBoot = event.values[0]
                if (stepOffset == null) {
                    stepOffset = totalStepsSinceBoot
                }
                _state.update {
                    it.copy(steps = totalStepsSinceBoot - (stepOffset ?: totalStepsSinceBoot))
                }
            }

            Sensor.TYPE_STEP_DETECTOR -> {
                _state.update {
                    it.copy(steps = it.steps + 1)
                }
            }
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnSetPermission -> onSetPermission(action.permission)
            HomeAction.OnBackgroundAccessDisabled -> onBackgroundAccessDisabled()
            HomeAction.OnFixStopCountingStepIssueClick -> onFixStopCountingStepIssueClick()
            HomeAction.OnStepGoalClick -> onStepGoalClick()
            HomeAction.OnPersonalSettingsClick -> onPersonalSettingsClick()
            HomeAction.OnMenuExitClick -> onMenuExitClick()
            HomeAction.OnExitConfirmClick -> onExitConfirmClick()
            is HomeAction.OnStepGoalSelected -> onStepGoalSelected(action.goal)
            HomeAction.OnStepGoalSave -> onStepGoalSave()
            HomeAction.OnStepGoalCancel -> onStepGoalCancel()
            HomeAction.OnStartStepSensor -> onSensorStart()
        }
    }

    private fun onSetPermission(permission: Permission) {
        viewModelScope.launch {
            if (permission == Permission.ALLOWED_BOTH)
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
            onSetPermission(Permission.ALLOWED_MOTION_SENSORS)
        }
    }

    private fun onStepGoalClick() {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OnNavSheetClose)
            _state.update {
                it.copy(
                    isGoalPickerVisible = true,
                    goalSelected = it.goal
                )
            }
        }
    }

    private fun onPersonalSettingsClick() {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OnProfileScreenClick)
        }
    }

    private fun onMenuExitClick() {
        viewModelScope.launch {
            _state.update {
                eventChannel.send(HomeEvent.OnNavSheetClose)
                it.copy(
                    isExitDialogVisible = true
                )
            }
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
                    isGoalPickerVisible = false,
                )
            }
        }
    }

    private fun onStepGoalCancel() {
        _state.update {
            it.copy(
                isGoalPickerVisible = false
            )
        }
    }

    private fun onSensorStart() {
        if (isListening) return
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)

        }
        detectSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
        isListening = true
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}
