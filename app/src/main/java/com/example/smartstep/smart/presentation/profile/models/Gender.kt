package com.example.smartstep.smart.presentation.profile.models

import com.example.smartstep.R
import com.example.smartstep.core.presentation.util.UiText

enum class Gender(val value: UiText) {
    FEMALE(UiText.StringResource(R.string.female)),
    MALE(UiText.StringResource(R.string.male))
}
