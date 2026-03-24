package com.example.smartstep.smart.presentation.util

import com.example.smartstep.settings.domain.Profile
import com.example.smartstep.smart.domain.profile.Gender
import com.example.smartstep.smart.presentation.models.Units

fun calculateCaloriesFromSteps(
    steps: Float,
    profile: Profile,
): Float {
    val gender = Gender.valueOf(profile.gender)
    val genderFactor = if (gender == Gender.FEMALE) 0.9f else 1f
    val kCalPerStep = profile.weight * 0.0005 * genderFactor
    val calories = steps * kCalPerStep

    return calories.toFloat()
}

fun calculateDistanceFromSteps(
    steps: Float,
    profile: Profile
): Float {
    val stepLength = profile.height * 0.415
    val distance = steps * stepLength / 100
    val units = Units.valueOf(profile.units)

    return (if (units == Units.CM) distance / 1000f else distance / 1609.34f).toFloat()
}
