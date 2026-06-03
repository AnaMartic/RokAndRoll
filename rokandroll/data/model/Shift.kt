package com.orwima.rokandroll.data.model

data class Shift(
    val id: String = "",
    val userId: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val hours: Double = 0.0,
    val hourlyRate: Double = 0.0,
    val earnings: Double = 0.0
)