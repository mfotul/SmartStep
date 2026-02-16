package com.example.smartstep.smart.presentation.profile

import com.example.smartstep.R
import com.example.smartstep.core.presentation.util.UiText
import com.example.smartstep.smart.presentation.profile.models.Gender
import com.example.smartstep.smart.presentation.profile.models.Units
import com.example.smartstep.smart.presentation.util.convertCmToFeetInches
import com.example.smartstep.smart.presentation.util.convertKgToLbs

data class ProfileState(
    val isInitialScreen: Boolean = false,
    val isHeightDialogVisible: Boolean = false,
    val isWeightDialogVisible: Boolean = false,
    val isGenderDropdownExpanded: Boolean = false,
    val gender: Gender = Gender.FEMALE,
    val height: Int = 170,
    val heightPicker: Int = 175,
    val heightPickerInches: Int = Int.MIN_VALUE,
    val heightUnit: Units = Units.CM,
    val weight: Int = 60,
    val weightPicker: Int = 60,
    val weightUnit: Units = Units.KG,

) {
    val formattedHeight: UiText
        get() {
            return if (heightUnit == Units.CM)
                UiText.StringResource(R.string.number_cm, arrayOf(height))
            else {
                val (feet, inch) = convertCmToFeetInches(height)
                UiText.StringResource(R.string.number_ft_in, arrayOf(feet, inch))
            }

        }
    val formattedWeight: UiText
        get() {
            return if (weightUnit == Units.KG)
                UiText.StringResource(R.string.number_kg, arrayOf(weight))
            else {
                val lbs = convertKgToLbs(weight)
                UiText.StringResource(R.string.number_lbs, arrayOf(lbs))
            }
        }
}


