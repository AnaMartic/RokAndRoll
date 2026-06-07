package com.orwima.rokandroll.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val hourlyRate: Double = 0.0,
    val city: String = "",
    val dailySteps: Int = 0,
    val stepsDate: String = ""
)