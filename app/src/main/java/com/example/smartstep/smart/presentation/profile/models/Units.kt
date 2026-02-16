package com.example.smartstep.smart.presentation.profile.models

import com.example.smartstep.R
import com.example.smartstep.core.presentation.util.UiText

enum class UnitType{
    HEIGHT,
    WEIGHT
}

enum class Units(
    val value: UiText,
    val unitType: UnitType,
    val range1: List<Int>,
    val range2: List<Int>?
) {
    CM(value = UiText.StringResource(R.string.cm), unitType = UnitType.HEIGHT, range1 = (48..214).toList(), range2 = null),
    FT(value = UiText.StringResource(R.string.ft_in), unitType = UnitType.HEIGHT, range1 = (-2..7).toList(), range2 = (-2..13).toList()),
    KG(value = UiText.StringResource(R.string.kg), unitType = UnitType.WEIGHT, range1 = (18..161).toList(), range2 = null),
    LB(value = UiText.StringResource(R.string.lb), unitType = UnitType.WEIGHT, range1 = (42..353).toList(), range2 = null)
}

