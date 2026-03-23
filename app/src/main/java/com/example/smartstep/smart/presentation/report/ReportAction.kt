package com.example.smartstep.smart.presentation.report

import com.example.smartstep.smart.presentation.report.model.DataType

sealed interface ReportAction {
    data object OnBackClick: ReportAction
    data object OnLeftArrowClick: ReportAction
    data object OnRightArrowClick: ReportAction
    data class OnDataTypeChange(val dataType: DataType): ReportAction
}