package com.example.calendarlibrary.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import com.example.calendarlibrary.model.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object CalendarDialogUtil {


    suspend fun showContinuousCalendar(context: Context, calenderRequestList: List<TimeRequestType>) {
        calenderRequestList.forEach {

            when (it) {

                is DateCalenderType -> {
                    waitCalendarDateDialogResponse(context, it)
                }

                is TimePickerType -> {
                    waitCalendarTimeDialogResponse(context, it)
                }
            }


        }
    }

    private suspend fun waitCalendarDateDialogResponse(context: Context, dateCalenderType: DateCalenderType): DataInfo {
        return suspendCoroutine { continuation ->
            showCalendarDateDialog(context, dateCalenderType) { view, year, month, dayOfMonth ->
                continuation.resume(DataInfo(year, month, dayOfMonth))
            }
        }
    }

    private suspend fun waitCalendarTimeDialogResponse(context: Context, timePickerType: TimePickerType): TimeInfo {
        return suspendCoroutine { continuation ->
            showCalendarTimeDialog(context, timePickerType) { timePicker, hourOfDay, min ->
                continuation.resume(TimeInfo(hourOfDay, min))
            }
        }
    }


    fun showCalendarDateDialog(context: Context, dateCalenderType: DateCalenderType, callback: DatePickerDialog.OnDateSetListener) {

        val datePickerDialog = when (dateCalenderType) {
            is DateCalenderType.NormalCalender -> {
                val calendar = Calendar.getInstance()
                DatePickerDialog(context, dateCalenderType.themeResId, callback, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            }

            is DateCalenderType.StartNowCalender -> {
                val calendar = Calendar.getInstance()
                val maxCalender = dateCalenderType.maxCalendar
                DatePickerDialog(context, dateCalenderType.themeResId, callback, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).apply {
                    if (maxCalender != null) datePicker.maxDate = maxCalender.timeInMillis
                    datePicker.minDate = calendar.timeInMillis

                }
            }
            is DateCalenderType.EndNowCalender -> {
                val calendar = Calendar.getInstance()
                val minCalender = dateCalenderType.minCalender
                DatePickerDialog(context, dateCalenderType.themeResId, callback, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).apply {
                    if (minCalender != null) datePicker.minDate = minCalender.timeInMillis
                    datePicker.maxDate = calendar.timeInMillis
                }
            }

            is DateCalenderType.CustomRangeCalender -> {

                val showTimeCalender = dateCalenderType.defaultCalender

                val minCalender = dateCalenderType.minCalender

                val maxCalender = dateCalenderType.maxCalendar

                DatePickerDialog(
                    context, dateCalenderType.themeResId, callback, showTimeCalender.get(Calendar.YEAR), showTimeCalender.get(Calendar.MONTH), showTimeCalender.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    if (minCalender != null) datePicker.minDate = minCalender.timeInMillis
                    if (maxCalender != null) datePicker.maxDate = maxCalender.timeInMillis
                }
            }
        }

        datePickerDialog.show()
    }

    fun showCalendarTimeDialog(context: Context, timePickerType: TimePickerType, callback: TimePickerDialog.OnTimeSetListener) {

        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val min = calender.get(Calendar.MINUTE)

        val timePicker = when (timePickerType) {

            is TimePickerType.NormalTimePicker -> {

                TimePickerDialog(context, timePickerType.themeResId, callback, hour, min, timePickerType.is24Hours)
            }

            is TimePickerType.StartOrBeforeNowCustomTimePicker -> {

                RangeTimePickerDialog(context, timePickerType.themeResId, callback, hour, min, timePickerType.is24Hours).apply {

                    val hourRange = timePickerType.minuteRange.div(60)

                    val minuteRange = timePickerType.minuteRange.rem(60)

                    if (hourRange < 0) {
                        setMin(hourRange, minuteRange)
                    } else {
                        setMax(hourRange, minuteRange)
                    }
                }
            }

            is TimePickerType.CustomRangeTimePicker -> {

                RangeTimePickerDialog(context, timePickerType.themeResId, callback, timePickerType.defaultHour, timePickerType.defaultMinute, timePickerType.is24Hours).apply {

                    val calendar = Calendar.getInstance()

                    val minHourRange = timePickerType.minMinuteRange?.div(60)
                    val minMinuteRange = timePickerType.minMinuteRange?.rem(60)
                    val maxHourRange = timePickerType.maxMinuteRange?.div(60)
                    val maxMinuteRange = timePickerType.maxMinuteRange?.rem(60)

                    if (minMinuteRange != null && minHourRange != null) {
                        setMin(calendar.get(Calendar.HOUR_OF_DAY) - minHourRange, minMinuteRange)
                    }

                    if (maxHourRange != null && maxMinuteRange != null) {
                        setMax(calendar.get(Calendar.HOUR_OF_DAY) + maxHourRange, maxMinuteRange)
                    }
                }
            }
        }

        timePicker.show()
    }
}

