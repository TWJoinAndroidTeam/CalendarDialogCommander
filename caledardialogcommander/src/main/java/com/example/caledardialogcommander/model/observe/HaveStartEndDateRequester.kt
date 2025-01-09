package com.example.caledardialogcommander.model.observe

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.caledardialogcommander.model.ui.DateCalenderType
import com.example.caledardialogcommander.model.ui.DateInfo
import com.example.caledardialogcommander.ui.CalendarDialogUtil
import java.util.Calendar

enum class DatePickerStateType {
    Start, End
}

class HaveStartEndDateRequester(
    private var allRangeMinCalender: Calendar? = null,
    private var allRangeMaxCalender: Calendar? = null,
    private var startDateCalendar: Calendar? = null,
    private var endDateCalendar: Calendar? = null,
) : DateTimeRequester<DateInfo, DateCalenderType>() {

    fun clearRememberData() {
        startDateCalendar = null
        endDateCalendar = null
    }

    fun updateStartDateCalendar(startDateCalendar: Calendar?) {
        this.startDateCalendar = startDateCalendar
    }

    fun updateEndDateCalendar(endDateCalendar: Calendar?) {
        this.endDateCalendar = endDateCalendar
    }

    fun updateAllRangeMinCalender(allRangeMinCalender: Calendar?) {
        this.allRangeMinCalender = allRangeMinCalender
    }

    fun updateAllRangeMaxCalender(allRangeMaxCalender: Calendar?) {
        this.allRangeMaxCalender = allRangeMaxCalender
    }

    suspend fun requestDateTimeSelection(dateTimePickerStateType: DatePickerStateType, callback: (DateInfo) -> Unit) {

        val startDateCalendar = this.startDateCalendar
        val endDateCalendar = this.endDateCalendar
        val allRangeMinCalender = this.allRangeMinCalender
        val allRangeMaxCalender = this.allRangeMaxCalender

        //建立選擇器結束日期的最小可選日期
        val endMinCalendar = when {

            startDateCalendar != null && allRangeMinCalender != null -> {
                if (startDateCalendar.after(allRangeMinCalender)) startDateCalendar else allRangeMinCalender
            }

            startDateCalendar != null -> {
                startDateCalendar
            }

            else -> allRangeMinCalender
        }

        //建立選擇器起始日期的最大可選日期
        val startMaxCalender = when {

            endDateCalendar != null && allRangeMaxCalender != null -> {
                if (endDateCalendar.before(allRangeMaxCalender)) endDateCalendar else allRangeMaxCalender
            }

            endDateCalendar != null -> {
                endDateCalendar
            }

            else -> allRangeMaxCalender
        }

        //根據dateTimePickerStateType產生結束或起始日期選擇器
        val rangeCalendar = when (dateTimePickerStateType) {

            DatePickerStateType.Start -> {
                DateCalenderType.CustomRangeCalender(minCalender = allRangeMinCalender, maxCalendar = startMaxCalender, defaultCalender = startDateCalendar ?: Calendar.getInstance(), 0)
            }

            DatePickerStateType.End -> {
                DateCalenderType.CustomRangeCalender(minCalender = endMinCalendar, maxCalendar = allRangeMaxCalender, defaultCalender = endDateCalendar ?: Calendar.getInstance(), 0)
            }
        }

        val handlerDateCallback: (DateInfo) -> Unit = {

            when (dateTimePickerStateType) {
                DatePickerStateType.Start -> {
                    this.startDateCalendar = it.calendar
                }

                DatePickerStateType.End -> {
                    this.endDateCalendar = it.calendar
                }
            }

            callback.invoke(it)
        }

        super.emitDateTimeSelectionEvent(rangeCalendar, handlerDateCallback)
    }
}

fun Pair<DateCalenderType, (DateInfo) -> Unit>.showCalendarDialog(lifecycleOwner: LifecycleOwner) {

    val context = when (lifecycleOwner) {
        is Activity -> lifecycleOwner
        is Fragment -> lifecycleOwner.requireContext()
        else -> throw IllegalArgumentException("lifecycleOwner must be Activity or Fragment")
    }

    CalendarDialogUtil.showCalendarDateDialog(context, this@showCalendarDialog.first) { view, year, month, dayOfMonth ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        second.invoke(DateInfo(year, month + 1, dayOfMonth, calendar))
    }
}