package com.example.smartstep.smart.presentation.report.model

data class DailyData(
    val date: String,
    val value: String,
    val goal: String,
    val dataSource: DataSource,
    val dataType: DataType
)
