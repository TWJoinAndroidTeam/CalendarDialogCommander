package com.example.caledardialogcommander.model.ui

data class CalendarRequest(
    val timeRequestType: TimeRequestType? = null,
    val timeRequestTypeList: MutableList<TimeRequestType> = if (timeRequestType != null) mutableListOf(timeRequestType) else mutableListOf()
) {

    fun addNextRequest(timeRequestType: TimeRequestType): CalendarRequest {
        timeRequestTypeList.add(timeRequestType)

        return this
    }
}