package com.example.caledardialogcommander.model.ui

import java.util.Calendar

interface CalendarResponse

data class DateInfo(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val calendar: Calendar
) : CalendarResponse

data class TimeInfo(
    val hourOfDay: Int,
    val minute: Int,
    val calendar: Calendar
) : CalendarResponse