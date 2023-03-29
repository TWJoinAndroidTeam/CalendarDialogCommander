package com.example.caledardialogcommander.model

import java.util.*

interface TimeRequestType


sealed class DateCalenderType(open val themeResId: Int) : TimeRequestType {

    data class NormalCalender(override val themeResId: Int) : DateCalenderType(themeResId)

    data class StartNowCalender(val maxCalendar: Calendar? = null, override val themeResId: Int) : DateCalenderType(themeResId)

    data class EndNowCalender(val minCalender: Calendar? = null, override val themeResId: Int) : DateCalenderType(themeResId)

    data class CustomRangeCalender(val minCalender: Calendar? = null, val maxCalendar: Calendar? = null, val defaultCalender: Calendar, override val themeResId: Int) : DateCalenderType(themeResId)
}


sealed class TimePickerType(open val is24Hours: Boolean, open val themeResId: Int) : TimeRequestType {

    data class NormalTimePicker(override val is24Hours: Boolean, override val themeResId: Int) : TimePickerType(is24Hours, themeResId)

    data class StartOrBeforeNowCustomTimePicker(val minuteRange: Int, override val is24Hours: Boolean, override val themeResId: Int) : TimePickerType(is24Hours, themeResId)

    /**
     * 完全自定義區間的 TimePicker
     */
    data class CustomRangeTimePicker(
        val defaultHour: Int,
        val defaultMinute: Int,
        val minMinuteRange: Int? = null,
        val maxMinuteRange: Int? = null,
        override val is24Hours: Boolean,
        override val themeResId: Int
    ) : TimePickerType(is24Hours, themeResId)
}
