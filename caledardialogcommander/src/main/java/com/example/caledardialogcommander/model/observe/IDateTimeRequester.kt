package com.example.caledardialogcommander.model.observe

import com.example.caledardialogcommander.model.ui.CalendarResponse
import com.example.caledardialogcommander.model.ui.TimeRequestType
import kotlinx.coroutines.flow.SharedFlow
import java.util.Calendar

interface IDateTimeRequester<I : CalendarResponse, T : TimeRequestType> {

    val flowDateCalendarType: SharedFlow<Pair<T, (I) -> Unit>>

    companion object {

        fun buildNormalDateRequester() = NormalDateRequester()

        fun buildHaveStartEndDateRequester(
            allRangeMinCalender: Calendar? = null,
            allRangeMaxCalender: Calendar? = null,
            startDateCalendar: Calendar? = null,
            endDateCalendar: Calendar? = null,
        ) = HaveStartEndDateRequester(allRangeMinCalender, allRangeMaxCalender, startDateCalendar, endDateCalendar)

    }
}
