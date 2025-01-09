package com.example.caledardialogcommander.model.observe

import com.example.caledardialogcommander.model.ui.CalendarResponse
import com.example.caledardialogcommander.model.ui.TimeRequestType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class DateTimeRequester<I : CalendarResponse, T : TimeRequestType>: IDateTimeRequester<I, T> {

    private val _flowDateCalendarType = MutableSharedFlow<Pair<T, (I) -> Unit>>()

    override val flowDateCalendarType: SharedFlow<Pair<T, (I) -> Unit>> get() = _flowDateCalendarType

    open suspend fun emitDateTimeSelectionEvent(dateCalenderType: T, callback: (I) -> Unit) {

        val handlerDateCallback: (I) -> Unit = {
            callback.invoke(it)
        }

        _flowDateCalendarType.emit(Pair(dateCalenderType, handlerDateCallback))
    }
}