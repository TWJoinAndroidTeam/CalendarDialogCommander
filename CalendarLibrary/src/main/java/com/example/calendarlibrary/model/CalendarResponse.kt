package com.example.calendarlibrary.model

interface CalendarResponse

data class DataInfo(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int
) : CalendarResponse

data class TimeInfo(
    val hourOfDay: Int,
    val minute: Int
) : CalendarResponse