package com.example.smartstep.smart.presentation.util

import com.example.smartstep.settings.domain.Profile
import com.example.smartstep.smart.domain.profile.Gender

fun calculateActivityMetrics(
    steps: Float,
    profile: Profile,
): Pair<Float, Float> {
    val stepLength = profile.height * 0.415
    val distance = steps * stepLength / 100

    val gender = Gender.valueOf(profile.gender)
    val genderFactor = if (gender == Gender.FEMALE) 0.9f else 1f
    val kCalPerStep = profile.weight * 0.0005 * genderFactor
    val calories = steps * kCalPerStep

    return Pair(distance.toFloat(), calories.toFloat())
}
