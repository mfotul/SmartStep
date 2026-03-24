package com.example.smartstep.smart.presentation.report.model

data class DailyData(
    val date: String,
    val value: Float,
    val goal: Int,
    val dataSource: DataSource,
    val dataType: DataType
)
