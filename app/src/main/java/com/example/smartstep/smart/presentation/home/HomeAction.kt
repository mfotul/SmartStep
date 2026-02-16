package com.example.smartstep.smart.presentation.home

import com.example.smartstep.smart.presentation.home.models.Permission

sealed interface HomeAction {
    data class OnSetPermission(val permission: Permission) : HomeAction
    data object OnBackgroundAccessDisabled : HomeAction
    data object OnFixStopCountingStepIssueClick : HomeAction
    data object OnStepGoalClick : HomeAction
    data object OnPersonalSettingsClick: HomeAction
    data object OnMenuExitClick: HomeAction
    data object OnExitConfirmClick: HomeAction
    data class OnStepGoalSelected(val goal: Int): HomeAction
    data object OnStepGoalSave: HomeAction
    data object OnStepGoalCancel: HomeAction
    data object OnStartStepSensor: HomeAction
}

