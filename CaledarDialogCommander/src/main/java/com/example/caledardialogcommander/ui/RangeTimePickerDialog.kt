package com.example.caledardialogcommander.ui

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker


class RangeTimePickerDialog(context: Context?, dialogTheme: Int, callBack: OnTimeSetListener?, hourOfDay: Int, minute: Int, is24HourView: Boolean) :
    TimePickerDialog(context, dialogTheme, callBack, hourOfDay, minute, is24HourView) {

    private var minHour = -1
    private var minMinute = -1
    private var maxHour = 100
    private var maxMinute = 100
    private var currentHour = 0
    private var currentMinute = 0


    fun setMin(hour: Int, minute: Int) {
        minHour = hour
        minMinute = minute
    }

    fun setMax(hour: Int, minute: Int) {
        maxHour = hour
        maxMinute = minute
    }

    override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        super.onTimeChanged(view, hourOfDay, minute)
        when {
            //當小時選擇小於等於最小限制
            hourOfDay <= minHour -> {
                currentHour = minHour

                currentMinute = if (minute >= minMinute) {
                    minute
                } else {
                    minMinute
                }
            }

            //當小時選擇大於等於最大限制
            hourOfDay >= maxHour -> {
                currentHour = maxHour
                currentMinute = if (minute <= maxMinute) {
                    minute
                } else {
                    maxMinute
                }
            }

            else -> {
                currentHour = hourOfDay
                currentMinute = minute
            }
        }

        updateTime(currentHour, currentMinute)
    }
}