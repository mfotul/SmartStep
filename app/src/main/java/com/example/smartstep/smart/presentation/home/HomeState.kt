package com.example.smartstep.smart.presentation.home

import com.example.smartstep.smart.presentation.home.models.Permission

data class HomeState(
    val permission: Permission = Permission.UNKNOWN,
    val isBackgroundAccessEnabled: Boolean = false,
    val isGoalPickerVisible: Boolean = false,
    val isExitDialogVisible: Boolean = false,
    val goal: Int = 4000,
    val goalSelected: Int = Int.MIN_VALUE,
    val steps: Float = 0f
)
