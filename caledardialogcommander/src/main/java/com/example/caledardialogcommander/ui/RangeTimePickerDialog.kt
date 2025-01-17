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


    private companion object {
        const val NO_MATCH_VALUE = -1
    }

    private var minHour = 0
    private var minMinute = 0
    private var maxHour = 23
    private var maxMinute = 59
    private var currentHour: Int = 0
    private var currentMinute: Int = 0


    fun setMin(
        hour: Int?,
        minute: Int?
    ) {
        this.minHour = hour ?: NO_MATCH_VALUE
        minMinute = minute ?: NO_MATCH_VALUE
    }

    fun setMax(
        hour: Int?,
        minute: Int?
    ) {
        maxHour = hour ?: NO_MATCH_VALUE
        maxMinute = minute ?: NO_MATCH_VALUE
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
            (maxHour != NO_MATCH_VALUE) && hourOfDay > maxHour -> {
                newHour = maxHour
                newMinute = maxMinute
            }

            //當 maxMinute為無限制時，不檢查
            (maxMinute != NO_MATCH_VALUE) && hourOfDay == maxHour && minute > maxMinute -> {
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