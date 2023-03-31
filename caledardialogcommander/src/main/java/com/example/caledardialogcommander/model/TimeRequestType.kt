package com.example.caledardialogcommander.model

import java.util.*

interface TimeRequestType


sealed class DateCalenderType(open val themeResId: Int) : TimeRequestType {

    /**
     * 一般的 DatePicker
     * @param themeResId dialog style id
     */
    data class NormalCalender(override val themeResId: Int = 0) : DateCalenderType(themeResId)

    /**
     * 開始時間為現在的DatePicker
     * @param maxCalendar 日期區間最大值
     * @param themeResId dialog style id
     */
    data class StartNowCalender(val maxCalendar: Calendar? = null, override val themeResId: Int = 0) : DateCalenderType(themeResId)

    /**
     * 結束時間為現在的DatePicker
     * @param minCalender 日期區間最小值
     * @param themeResId dialog style id
     */
    data class EndNowCalender(val minCalender: Calendar? = null, override val themeResId: Int = 0) : DateCalenderType(themeResId)

    /**
     * 完全自定義區間的 DatePicker
     * @param minCalender 日期區間最小值
     * @param maxCalendar 日期區間最大值
     * @param defaultCalender 當前日期
     * @param themeResId dialog style id
     */
    data class CustomRangeCalender(val minCalender: Calendar? = null, val maxCalendar: Calendar? = null, val defaultCalender: Calendar, override val themeResId: Int) : DateCalenderType(themeResId)
}


sealed class TimePickerType(open val is24Hours: Boolean, open val themeResId: Int) : TimeRequestType {

    /**
     * 一般的timePicker
     * @param themeResId dialog style id
     */
    data class NormalTimePicker(override val is24Hours: Boolean, override val themeResId: Int = 0) : TimePickerType(is24Hours, themeResId)

    /**
     * 開始時間為現在的timePicker
     * @param minuteRange 分鐘區間最大值
     * @param themeResId dialog style id
     */
    data class StartNowCustomTimePicker(val minuteRange: Int, override val is24Hours: Boolean, override val themeResId: Int = 0) : TimePickerType(is24Hours, themeResId)

    /**
     * 結束時間為現在的timePicker
     * @param minuteRange 分鐘區間最小值
     * @param themeResId dialog style id
     */
    data class EndNowCustomTimePicker(val minuteRange: Int, override val is24Hours: Boolean, override val themeResId: Int = 0) : TimePickerType(is24Hours, themeResId)

    /**
     * 完全自定義區間的 TimePicker
     * @param defaultHour 預設當前小時
     * @param defaultMinute 預設當前分鐘
     * @param minMinuteRange 分鐘區間最小值
     * @param maxMinuteRange 分鐘區間最大值
     * @param themeResId dialog style id
     */
    data class CustomRangeTimePicker(
        val defaultHour: Int,
        val defaultMinute: Int,
        val minMinuteRange: Int? = null,
        val maxMinuteRange: Int? = null,
        override val is24Hours: Boolean,
        override val themeResId: Int = 0
    ) : TimePickerType(is24Hours, themeResId)
}
