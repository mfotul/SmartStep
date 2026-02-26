package com.example.smartstep.smart.presentation.step

import com.example.smartstep.smart.presentation.step.models.DateInput
import com.example.smartstep.smart.presentation.step.models.Dialog
import com.example.smartstep.smart.presentation.step.models.Permission

sealed interface StepAction {
    data class OnSetPermission(val permission: Permission) : StepAction
    data object OnBackgroundAccessDisabled : StepAction
    data object OnFixStopCountingStepIssueClick : StepAction
    data class OnDialogOpen(val dialog: Dialog) : StepAction
    data object OnPersonalSettingsClick : StepAction
    data class OnDateSelected(val dateInput: DateInput) : StepAction
    data object OnEditStepsSave : StepAction
    data object OnResetStepsConfirmClick : StepAction
    data object OnExitConfirmClick : StepAction
    data class OnStepGoalSelected(val goal: Int) : StepAction
    data object OnStepGoalSave : StepAction
    data object OnPauseClick : StepAction
}

