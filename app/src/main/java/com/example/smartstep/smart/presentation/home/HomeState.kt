package com.example.smartstep.smart.presentation.home

import com.example.smartstep.smart.presentation.home.models.Dialog
import com.example.smartstep.smart.presentation.home.models.Permission
import com.example.smartstep.smart.presentation.models.Units
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class HomeState(
    val permission: Permission = Permission.UNKNOWN,
    val isBackgroundAccessEnabled: Boolean = false,
    val dialogVisible: Dialog = Dialog.NONE,
    val goal: Int = 4000,
    val goalSelected: Int = Int.MIN_VALUE,
    val steps: Float = 0f,
    val date: ZonedDateTime = ZonedDateTime.now(),
    val distance: String= "",
    val calories: String = "",
    val activeMinutes: String = "",
    val isPaused: Boolean = false,
    val units: Units = Units.KG
) {
    val formattedDate: String
        get() {
            return DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(date)
        }
}
