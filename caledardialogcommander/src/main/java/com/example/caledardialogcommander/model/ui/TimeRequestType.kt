package com.example.caledardialogcommander.model.ui

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
    data class StartNowCalender(
        val maxCalendar: Calendar? = null,
        override val themeResId: Int = 0
    ) : DateCalenderType(themeResId)

    /**
     * 結束時間為現在的DatePicker
     * @param minCalender 日期區間最小值
     * @param themeResId dialog style id
     */
    data class EndNowCalender(
        val minCalender: Calendar? = null,
        override val themeResId: Int = 0
    ) : DateCalenderType(themeResId)

    /**
     * 完全自定義區間的 DatePicker
     * @param minCalender 日期區間最小值
     * @param maxCalendar 日期區間最大值
     * @param defaultCalender 當前日期
     * @param themeResId dialog style id
     */
    data class CustomRangeCalender(
        val minCalender: Calendar? = null,
        val maxCalendar: Calendar? = null,
        val defaultCalender: Calendar,
        override val themeResId: Int
    ) : DateCalenderType(themeResId)
}


sealed class TimePickerType(
    open val is24Hours: Boolean,
    open val themeResId: Int,
) : TimeRequestType {

    /**
     * 一般的timePicker
     * @param themeResId dialog style id
     */
    data class NormalTimePicker(
        override val is24Hours: Boolean,
        override val themeResId: Int = 0
    ) : TimePickerType(is24Hours, themeResId)


    /**
     * 開始時間為現在的timePicker
     * @property maxCalendar Calendar?  時間區間最大值
     * @property is24Hours Boolean
     * @property themeResId Int
     * @constructor
     */
    data class StartNowCustomTimePicker(
        val maxCalendar: Calendar? = null,
        override val is24Hours: Boolean,
        override val themeResId: Int = 0
    ) :
        TimePickerType(is24Hours, themeResId) {
        /**
         * @param totalMinute Int 往後加總分鐘數
         * @param is24Hours Boolean
         * @param themeResId Int
         * @constructor
         */
        constructor(
            totalMinute: Int,
            is24Hours: Boolean,
            themeResId: Int = 0
        ) : this(addMinuteAndSetMaxTimeRange(totalMinute / 60, totalMinute % 60), is24Hours, themeResId)


        /**
         * @param hourRange 小時區間最大值
         * @param minuteRange 分鐘區間最大值
         * @param themeResId dialog style id
         */
        constructor(
            hourRange: Int? = null,
            minuteRange: Int? = null,
            is24Hours: Boolean,
            themeResId: Int = 0
        ) : this(addMinuteAndSetMaxTimeRange(hourRange, minuteRange), is24Hours, themeResId)
    }


    /**
     * 結束時間為現在的timePicker
     * @property minCalender Calendar? 時間區間最小值
     * @property is24Hours Boolean
     * @property themeResId Int
     * @constructor
     */
    data class EndNowCustomTimePicker(
        val minCalender: Calendar? = null,
        override val is24Hours: Boolean,
        override val themeResId: Int = 0
    ) : TimePickerType(is24Hours, themeResId) {

        /**
         * @param minTotalMinute Int 往前扣總分鐘數
         * @param is24Hours Boolean
         * @param themeResId Int
         * @constructor
         */
        constructor(
            minTotalMinute: Int,
            is24Hours: Boolean,
            themeResId: Int = 0
        ) : this(minusMinuteAndSetMinTimeRange(minTotalMinute / 60, minTotalMinute % 60), is24Hours, themeResId)

        /**
         * @param hourRange 小時區間最小值
         * @param minuteRange 分鐘區間最小值
         * @param themeResId dialog style id
         */
        constructor(
            hourRange: Int?,
            minuteRange: Int?,
            is24Hours: Boolean,
            themeResId: Int = 0
        ) : this(minusMinuteAndSetMinTimeRange(hourRange, minuteRange), is24Hours, themeResId)
    }


    /**
     * 完全自定義區間的 TimePicker
     * @property defaultHour Int?
     * @property defaultMinute Int?
     * @property minCalendar Calendar? 時間區間最小值
     * @property maxCalendar Calendar? 時間區間最大值
     * @property is24Hours Boolean
     * @property themeResId Int
     * @constructor
     */
    data class CustomRangeTimePicker(
        val defaultHour: Int? = null,
        val defaultMinute: Int? = null,
        val minCalendar: Calendar?,
        val maxCalendar: Calendar?,
        override val is24Hours: Boolean,
        override val themeResId: Int = 0
    ) : TimePickerType(is24Hours, themeResId) {


        /**
         * @param defaultHour 預設當前小時
         * @param defaultMinute 預設當前分鐘
         * @param minMinute 分鐘區間最小值
         * @param maxMinute 分鐘區間最大值
         */
        constructor(
            defaultHour: Int? = null,
            defaultMinute: Int? = null,
            minHour: Int? = null,
            minMinute: Int? = null,
            maxHour: Int? = null,
            maxMinute: Int? = null,
        ) : this(defaultHour, defaultMinute, minusMinuteAndSetMinTimeRange(minHour, minMinute), addMinuteAndSetMaxTimeRange(maxHour, maxMinute), false)

        /**
         * @param defaultHour Int?
         * @param defaultMinute Int?
         * @param minTotalMinute Int? 往前扣總分鐘數
         * @param maxTotalMinute Int? 往後加總分鐘數
         * @param is24Hours Boolean
         * @param themeResId Int
         * @constructor
         */
        constructor(
            defaultHour: Int? = null,
            defaultMinute: Int? = null,
            minTotalMinute: Int? = null,
            maxTotalMinute: Int? = null,
            is24Hours: Boolean,
            themeResId: Int = 0
        ) : this(
            defaultHour,
            defaultMinute,
            minusMinuteAndSetMinTimeRange(minTotalMinute?.div(60), minTotalMinute?.rem(60)),
            addMinuteAndSetMaxTimeRange(maxTotalMinute?.div(60), maxTotalMinute?.rem(60)),
            is24Hours,
            themeResId
        )

    }
}

private fun minusMinuteAndSetMinTimeRange(
    hourRange: Int?,
    minuteRange: Int?,
): Calendar? {

    var min: Int? = null

    if (hourRange != null) {

        min = 60 * hourRange
    }

    if (minuteRange != null) {
        min = (min ?: 0) + minuteRange
    }

    if (min == null) return null

    val minCalendar = Calendar.getInstance()

    minCalendar.add(Calendar.MINUTE, -min)

    return minCalendar
}

private fun addMinuteAndSetMaxTimeRange(
    hourRange: Int?,
    minuteRange: Int?,
): Calendar? {

    var max: Int? = null

    if (hourRange != null) {

        max = 60 * hourRange
    }

    if (minuteRange != null) {
        max = (max ?: 0) + minuteRange
    }

    if (max == null) return null

    val maxCalendar = Calendar.getInstance()

    maxCalendar.add(Calendar.MINUTE, max)

    return maxCalendar
}

