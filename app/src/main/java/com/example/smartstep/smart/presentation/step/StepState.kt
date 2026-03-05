package com.example.smartstep.smart.presentation.step

import com.example.smartstep.smart.domain.step.ConnectivityObserver
import com.example.smartstep.smart.presentation.step.models.Dialog
import com.example.smartstep.smart.presentation.step.models.Permission
import com.example.smartstep.smart.presentation.models.Units
import com.example.smartstep.smart.presentation.step.models.StepUi
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class StepState(
    val permission: Permission = Permission.UNKNOWN,
    val isBackgroundAccessEnabled: Boolean = false,
    val dialogVisible: Dialog = Dialog.NONE,
    val goal: Int = 4000,
    val goalSelected: Int = Int.MIN_VALUE,
    val steps: Float = 0f,
    val date: ZonedDateTime = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.of("UTC")),
    val distance: String= "",
    val calories: String = "",
    val activeMinutes: String = "",
    val isPaused: Boolean = false,
    val units: Units = Units.KG,
    val weeklyStats: List<StepUi> = emptyList(),
    val averageDailySteps: Int = 0,
    val connectivityStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val aiResult: String = ""
) {
    val formattedDate: String
        get() {
            return DateTimeFormatter.ofPattern("yyyy/MM/dd")
                .format(date)
        }
}
