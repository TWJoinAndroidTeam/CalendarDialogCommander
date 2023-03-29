package com.example.caledardialogcommander.model

interface CalendarResponse

data class DateInfo(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int
) : CalendarResponse

data class TimeInfo(
    val hourOfDay: Int,
    val minute: Int
) : CalendarResponse