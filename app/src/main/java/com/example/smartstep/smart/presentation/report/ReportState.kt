package com.example.smartstep.smart.presentation.report

import com.example.smartstep.smart.presentation.report.model.DailyData
import com.example.smartstep.smart.presentation.report.model.DataType

data class ReportState(
    val cardValue: Float = 0f,
    val cardDailyAverageValue: Float = 0f,
    val weekTitle: String = "",
    val isLeftArrowEnabled: Boolean = true,
    val isRightArrowEnabled: Boolean = false,
    val weekData: List<DailyData> = emptyList(),
    val selectedDataType: DataType = DataType.STEPS,
    val minusWeeks: Long = 0
)
