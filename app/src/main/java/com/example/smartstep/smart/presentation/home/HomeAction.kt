package com.example.smartstep.smart.presentation.home

import com.example.smartstep.smart.presentation.home.models.DateInput
import com.example.smartstep.smart.presentation.home.models.Dialog
import com.example.smartstep.smart.presentation.home.models.Permission

sealed interface HomeAction {
    data class OnSetPermission(val permission: Permission) : HomeAction
    data object OnBackgroundAccessDisabled : HomeAction
    data object OnFixStopCountingStepIssueClick : HomeAction
    data class OnDialogOpen(val dialog: Dialog) : HomeAction
    data object OnPersonalSettingsClick : HomeAction
    data class OnDateSelected(val dateInput: DateInput) : HomeAction
    data object OnEditStepsSave : HomeAction
    data object OnResetStepsConfirmClick : HomeAction
    data object OnExitConfirmClick : HomeAction
    data class OnStepGoalSelected(val goal: Int) : HomeAction
    data object OnStepGoalSave : HomeAction
    data object OnPauseClick : HomeAction
}

