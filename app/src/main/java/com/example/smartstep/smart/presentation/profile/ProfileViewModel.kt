package com.example.smartstep.smart.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.settings.domain.Profile
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.presentation.profile.models.Gender
import com.example.smartstep.smart.presentation.models.Units
import com.example.smartstep.smart.presentation.profile.models.Dialog
import com.example.smartstep.smart.presentation.util.convertCmToFeetInches
import com.example.smartstep.smart.presentation.util.convertFeetInchesToCm
import com.example.smartstep.smart.presentation.util.convertKgToLbs
import com.example.smartstep.smart.presentation.util.convertLbsToKg
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val settingsPreferences: SettingPreferences,
) : ViewModel() {
    private var hasLoadedInitialData = false

    private var _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProfileState()
        )
    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun loadInitialData() {
        settingsPreferences.observeProfileSettings()
            .take(1)
            .onEach { profileSettings ->
                _state.update {
                    val units = Units.entries.find { units -> units.name == profileSettings.units }
                        ?: Units.CM
                    val height =
                        if (profileSettings.height != Int.MIN_VALUE) profileSettings.height else it.height
                    val weight =
                        if (profileSettings.weight != Int.MIN_VALUE) profileSettings.weight else it.weight

                    it.copy(
                        isInitialScreen = profileSettings.height == Int.MIN_VALUE,
                        defaultUnits = units,
                        heightUnit = if (units == Units.CM) Units.CM else Units.FT,
                        weightUnit = if (units == Units.CM) Units.KG else Units.LB,
                        gender = Gender.entries.find { gender -> gender.name == profileSettings.gender }
                            ?: it.gender,
                        height = height,
                        weight = weight,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnCancel -> onCancel()
            is ProfileAction.OnConfirm -> onConfirm(action.units)
            ProfileAction.OnGenderClick -> onGenderClick()
            is ProfileAction.OnGenderSelected -> onGenderSelected(action.gender)
            is ProfileAction.OnUnitSelected -> onUnitSelected(action.unit)
            is ProfileAction.OnHeightSelected -> onHeightSelected(action.height)
            is ProfileAction.OnHeightInchesSelected -> onHeightInchesSelected(action.height)
            is ProfileAction.OnDialogOpen -> onDialogOpen(action.dialog)
            is ProfileAction.OnWeightSelected -> onWeightSelected(action.weight)
            ProfileAction.OnStarButtonClick -> onStarButtonClick()
            ProfileAction.OnSkip -> onSkip()
        }
    }

    private fun onCancel() {
        _state.update {
            if (it.heightUnit != it.defaultUnits)
                onUnitSelected(it.defaultUnits)
            it.copy(dialogVisible = Dialog.NONE)
        }
    }

    private fun onConfirm(units: Units) {
        _state.update {
            when (units) {
                Units.CM, Units.FT -> it.copy(
                    height = if (it.heightUnit == Units.FT)
                        convertFeetInchesToCm(it.heightPicker, it.heightPickerInches)
                    else
                        it.heightPicker,
                    dialogVisible = Dialog.NONE
                )

                Units.KG, Units.LB -> it.copy(
                    weight = if (it.weightUnit == Units.LB)
                        convertLbsToKg(it.weightPicker)
                    else
                        it.weightPicker,
                    dialogVisible = Dialog.NONE
                )
            }
        }
    }

    private fun onGenderClick() {
        _state.update {
            it.copy(
                isGenderDropdownExpanded = true
            )
        }
    }

    private fun onGenderSelected(gender: Gender) {
        _state.update {
            it.copy(
                gender = gender,
                isGenderDropdownExpanded = false
            )
        }
    }

    private fun onUnitSelected(unit: Units) {
        _state.update {
            when (unit) {
                Units.CM, Units.KG -> {
                    val cm = convertFeetInchesToCm(it.heightPicker, it.heightPickerInches)
                    val kg = convertLbsToKg(it.weightPicker)
                    it.copy(
                        heightPicker = cm,
                        heightPickerInches = Int.MIN_VALUE,
                        heightUnit = Units.CM,
                        weightPicker = kg,
                        weightUnit = Units.KG,
                    )
                }

                Units.FT, Units.LB -> {
                    val (feet, inches) = convertCmToFeetInches(it.heightPicker)
                    val lbs = convertKgToLbs(it.weightPicker)
                    it.copy(
                        heightPicker = feet,
                        heightPickerInches = inches,
                        heightUnit = Units.FT,
                        weightPicker = lbs,
                        weightUnit = Units.LB,
                    )
                }
            }
        }
    }

    private fun onDialogOpen(dialog: Dialog) {
        _state.update {
            if (dialog == Dialog.NONE) return
            when (dialog) {
                Dialog.HEIGHT -> {
                    if (it.heightUnit == Units.FT) {
                        val (feet, inches) = convertCmToFeetInches(it.height)
                        it.copy(
                            dialogVisible = Dialog.HEIGHT,
                            heightPicker = feet,
                            heightPickerInches = inches
                        )
                    } else
                        it.copy(
                            dialogVisible = Dialog.HEIGHT,
                            heightPicker = it.height
                        )
                }

                Dialog.WEIGHT -> {
                    if (it.weightUnit == Units.LB)
                        it.copy(
                            dialogVisible = Dialog.WEIGHT,
                            weightPicker = convertKgToLbs(it.weight)
                        )
                    else
                        it.copy(
                            dialogVisible = Dialog.WEIGHT,
                            weightPicker = it.weight
                        )
                }
            }
        }
    }

    private fun onHeightSelected(height: Int) {
        _state.update {
            it.copy(heightPicker = height)
        }
    }

    private fun onHeightInchesSelected(height: Int) {
        _state.update {
            it.copy(heightPickerInches = height)
        }
    }

    private fun onWeightSelected(weight: Int) {
        _state.update {
            it.copy(weightPicker = weight)
        }
    }

    private fun onStarButtonClick() {
        viewModelScope.launch {
            settingsPreferences.saveProfileSettings(
                Profile(
                    units = state.value.heightUnit.name,
                    gender = state.value.gender.name,
                    height = state.value.height,
                    weight = state.value.weight,
                )
            )
            eventChannel.send(ProfileEvent.OnStart)
        }
    }

    private fun onSkip() {
        viewModelScope.launch {
            val initialProfile = ProfileState()
            settingsPreferences.saveProfileSettings(
                Profile(
                    units = state.value.defaultUnits.name,
                    gender = initialProfile.gender.name,
                    height = initialProfile.height,
                    weight = initialProfile.weight,
                )
            )
            eventChannel.send(ProfileEvent.OnStart)
        }
    }
}
