package com.example.calendarlibrary.date_time

import java.util.*


interface ITimePattern {
    val pattern: String
    val timeZone: TimeZone
    val locale: Locale
}

data class TimePattern(
    override val pattern: String,
    override val timeZone: TimeZone,
    override val locale: Locale,
) : ITimePattern
