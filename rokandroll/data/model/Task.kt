package com.orwima.rokandroll.data.model

data class Task(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val type: String = "",
    val deleted: Boolean = false
)