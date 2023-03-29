package com.example.calendarlibrary.model

data class CalendarRequest(
    val timeRequestTypeList: MutableList<TimeRequestType> = mutableListOf()
) {

    fun addNextRquest(timeRequestType: TimeRequestType) {
        timeRequestTypeList.add(timeRequestType)
    }
}