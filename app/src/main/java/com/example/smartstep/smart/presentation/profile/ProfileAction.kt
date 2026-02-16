package com.example.smartstep.smart.presentation.profile

import com.example.smartstep.smart.presentation.profile.models.Gender
import com.example.smartstep.smart.presentation.profile.models.Units

sealed interface ProfileAction {
    data class OnCancel(val units: Units) : ProfileAction
    data class OnConfirm(val units: Units) : ProfileAction
    data object OnGenderClick : ProfileAction
    data class OnGenderSelected(val gender: Gender) : ProfileAction
    data class OnUnitSelected(val unit: Units) : ProfileAction
    data object OnHeightClick : ProfileAction
    data class OnHeightSelected(val height: Int) : ProfileAction
    data class OnHeightInchesSelected(val height: Int) : ProfileAction
    data object OnWeightClick : ProfileAction
    data class OnWeightSelected(val weight: Int) : ProfileAction
    data object OnStarButtonClick : ProfileAction
    data object OnSkip : ProfileAction
}