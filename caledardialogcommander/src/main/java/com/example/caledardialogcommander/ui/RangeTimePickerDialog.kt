package com.example.caledardialogcommander.ui

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker


class RangeTimePickerDialog(
    context: Context?,
    dialogTheme: Int,
    callBack: OnTimeSetListener?,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) :
    TimePickerDialog(context, dialogTheme, callBack, hourOfDay, minute, is24HourView) {

    private var minHour = 0
    private var minMinute = 0
    private var maxHour = 23
    private var maxMinute = 59


    fun setMin(
        hour: Int?,
        minute: Int?
    ) {
        if (hour != null) {
            this.minHour = hour
        }
        if (minute != null) {
            minMinute = minute
        }
    }

    fun setMax(
        hour: Int?,
        minute: Int?
    ) {
        if (hour != null) {
            maxHour = hour
        }
        if (minute != null) {
            maxMinute = minute
        }
    }

    override fun onTimeChanged(
        view: TimePicker,
        hourOfDay: Int,
        minute: Int
    ) {
        super.onTimeChanged(view, hourOfDay, minute)

        getTimeAfterCheck(hourOfDay, minute)

    }

    private fun getTimeAfterCheck(
        hourOfDay: Int,
        minute: Int
    ) {
        val newHour: Int
        val newMinute: Int

        when {
            hourOfDay < minHour -> {
                newHour = minHour
                newMinute = minMinute
            }

            hourOfDay == minHour && minute < minMinute -> {
                newHour = minHour
                newMinute = minMinute
            }

            //當 maxHour為無限制時，不檢查
            hourOfDay > maxHour -> {
                newHour = maxHour
                newMinute = maxMinute
            }

            //當 maxMinute為無限制時，不檢查
            hourOfDay == maxHour && minute > maxMinute -> {
                newHour = maxHour
                newMinute = maxMinute
            }

            else -> {
                newHour = hourOfDay
                newMinute = minute
            }
        }

        updateTime(newHour, newMinute)
    }
}