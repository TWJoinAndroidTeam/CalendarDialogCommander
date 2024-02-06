package com.example.caledardialogcommander.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import com.example.caledardialogcommander.model.ui.CalendarRequest
import com.example.caledardialogcommander.model.ui.CalendarResponse
import com.example.caledardialogcommander.model.ui.DateCalenderType
import com.example.caledardialogcommander.model.ui.DateInfo
import com.example.caledardialogcommander.model.ui.TimeInfo
import com.example.caledardialogcommander.model.ui.TimePickerType
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

object CalendarDialogUtil {

    suspend fun showContinuousCalendar(context: Context, calendarRequest: CalendarRequest, onCancel: () -> Unit): MutableList<CalendarResponse> {
        val list = mutableListOf<CalendarResponse>()

        calendarRequest.timeRequestTypeList.forEach {

            when (it) {
                is DateCalenderType -> {
                    list.add(waitCalendarDateDialogResponse(context, it, onCancel))
                }

                is TimePickerType -> {
                    list.add(waitCalendarTimeDialogResponse(context, it, onCancel))
                }
            }
        }

        return list
    }

    suspend fun waitCalendarDateDialogResponse(context: Context, dateCalenderType: DateCalenderType, onCancel: (() -> Unit)? = null): DateInfo {
        return suspendCancellableCoroutine { continuation ->

            continuation.invokeOnCancellation {
                onCancel?.invoke()
            }

            val dialog = showCalendarDateDialog(context, dateCalenderType) { view, year, month, dayOfMonth ->

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                continuation.resume(DateInfo(year, month + 1, dayOfMonth, calendar))
            }

            dialog.setOnCancelListener {
                continuation.cancel()
            }
        }
    }

    suspend fun waitCalendarTimeDialogResponse(context: Context, timePickerType: TimePickerType, onCancel: (() -> Unit)? = null): TimeInfo {
        return suspendCancellableCoroutine { continuation ->

            continuation.invokeOnCancellation {
                onCancel?.invoke()
            }

            val dialog = showCalendarTimeDialog(context, timePickerType) { timePicker, hourOfDay, min ->

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, min)

                continuation.resume(TimeInfo(hourOfDay, min, calendar))
            }

            dialog.setOnCancelListener {
                continuation.cancel()
            }
        }
    }


    fun showCalendarDateDialog(context: Context, dateCalenderType: DateCalenderType, callback: DatePickerDialog.OnDateSetListener): DatePickerDialog {

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

        return datePickerDialog
    }

    fun showCalendarTimeDialog(context: Context, timePickerType: TimePickerType, callback: TimePickerDialog.OnTimeSetListener): TimePickerDialog {

        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)

        val timePicker = when (timePickerType) {

            is TimePickerType.NormalTimePicker -> {

                TimePickerDialog(context, timePickerType.themeResId, callback, hour, minute, timePickerType.is24Hours)
            }

            is TimePickerType.StartNowCustomTimePicker -> {

                RangeTimePickerDialog(context, timePickerType.themeResId, callback, hour, minute, timePickerType.is24Hours).apply {

                    setMin(hour, minute)

                    var max: Int? = null

                    if (timePickerType.hourRange != null) {

                        max = 60 * timePickerType.hourRange
                    }

                    if (timePickerType.minuteRange != null) {
                        max = (max ?: 0) + timePickerType.minuteRange
                    }

                    addMinuteAndSetMaxTimeRange(max, this)
                }
            }

            is TimePickerType.EndNowCustomTimePicker -> {

                RangeTimePickerDialog(context, timePickerType.themeResId, callback, hour, minute, timePickerType.is24Hours).apply {

                    var min: Int? = null

                    if (timePickerType.hourRange != null) {

                        min = 60 * timePickerType.hourRange
                    }

                    if (timePickerType.minuteRange != null) {
                        min = (min ?: 0) + timePickerType.minuteRange
                    }

                    minusMinuteAndSetMinTimeRange(min, this)

                    setMax(hour, minute)
                }
            }

            is TimePickerType.CustomRangeTimePicker -> {

                RangeTimePickerDialog(context, timePickerType.themeResId, callback, timePickerType.defaultHour, timePickerType.defaultMinute, timePickerType.is24Hours).apply {

                    val minRange = timePickerType.minMinuteRange
                    val maxRange = timePickerType.maxMinuteRange

                    if (minRange != null) {
                        minusMinuteAndSetMinTimeRange(minRange, this)
                    }

                    if (maxRange != null) {
                        addMinuteAndSetMaxTimeRange(maxRange, this)
                    }
                }
            }
        }

        timePicker.show()

        return timePicker
    }

    private fun minusMinuteAndSetMinTimeRange(minuteRange: Int?, rangeTimePickerDialog: RangeTimePickerDialog) {

        if (minuteRange == null) return

        val minCalendar = Calendar.getInstance()

        minCalendar.add(Calendar.MINUTE, -minuteRange)

        rangeTimePickerDialog.setMin(minCalendar.get(Calendar.HOUR_OF_DAY), minCalendar.get(Calendar.MINUTE))
    }

    private fun addMinuteAndSetMaxTimeRange(minuteRange: Int?, rangeTimePickerDialog: RangeTimePickerDialog) {

        if (minuteRange == null) return

        val maxCalendar = Calendar.getInstance()

        maxCalendar.add(Calendar.MINUTE, minuteRange)

        rangeTimePickerDialog.setMax(maxCalendar.get(Calendar.HOUR_OF_DAY), maxCalendar.get(Calendar.MINUTE))
    }
}

