package com.example.smartstep.smart.presentation.report

import com.example.smartstep.smart.presentation.report.model.DailyData
import com.example.smartstep.smart.presentation.report.model.DataType

data class ReportState(
    val cardValue: Int = 0,
    val cardDailyAverageValue: Int = 0,
    val week: String = "",
    val isLeftArrowEnabled: Boolean = false,
    val isRightArrowEnabled: Boolean = false,
    val weekData: List<DailyData> = emptyList(),
    val selectedDataType: DataType = DataType.STEPS
)
