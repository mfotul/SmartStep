package com.example.smartstep.smart.presentation.util

import kotlin.math.roundToInt

fun convertCmToFeetInches(cm: Int): Pair<Int, Int> {
    val totalInches = cm / 2.54
    val feet = (totalInches / 12).toInt()
    val inches = (totalInches % 12).roundToInt()

    return Pair(feet, inches)
}

fun convertFeetInchesToCm(feet: Int, inches: Int): Int {
    val totalInches = (feet * 12) + inches
    return (totalInches * 2.54).roundToInt()
}

fun convertKgToLbs(kg: Int): Int {
    return (kg * 2.204).roundToInt()
}

fun convertLbsToKg(lbs: Int): Int {
    return (lbs / 2.204).roundToInt()
}