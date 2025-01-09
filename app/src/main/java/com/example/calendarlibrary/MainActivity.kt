package com.example.calendarlibrary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.caledardialogcommander.model.ui.CalendarRequest
import com.example.caledardialogcommander.model.ui.DateCalenderType
import com.example.caledardialogcommander.model.ui.DateInfo
import com.example.caledardialogcommander.model.ui.TimeInfo
import com.example.caledardialogcommander.model.ui.TimePickerType
import com.example.caledardialogcommander.ui.CalendarDialogUtil
import com.example.calendarlibrary.databinding.ActivityMainBinding
import com.example.calendarlibrary.date_time.getCalenderString
import com.example.calendarlibrary.date_time.toTimePattern
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    companion object {
        val dateTimePattern = "yyyy/MM/dd".toTimePattern()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        setUI()
    }

    private fun setUI() {

        val calender = Calendar.getInstance()

        setDate(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH))

        setTime(calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE))

        viewBinding.btnDateCalendar.setOnClickListener {
            lifecycleScope.launch {
                val dateInfo = CalendarDialogUtil.waitCalendarDateDialogResponse(
                    this@MainActivity, DateCalenderType.StartNowCalender(themeResId = R.style.DatePickerDialogTheme)
                ) {
                    Toast.makeText(this@MainActivity, "cancel date by user!!", Toast.LENGTH_SHORT).show()
                }

                setDate(dateInfo.calendar)
            }
        }

        viewBinding.btnTimeCalendar.setOnClickListener {
            lifecycleScope.launch {

                val minCalendar = Calendar.getInstance()
                val maxCalendar = Calendar.getInstance()
                maxCalendar.add(Calendar.MINUTE, 80)
                minCalendar.add(Calendar.MINUTE, -80)

                val timeInfo = CalendarDialogUtil.waitCalendarTimeDialogResponse(
                    this@MainActivity, TimePickerType.CustomRangeTimePicker(maxCalendar = maxCalendar, minCalendar = minCalendar , is24Hours = true, themeResId = R.style.CustomTimePickerDialog)
                ) {
                    Toast.makeText(this@MainActivity, "cancel time by user!!", Toast.LENGTH_SHORT).show()
                }

                setTime(timeInfo.hourOfDay, timeInfo.minute)
            }
        }

        viewBinding.btnDateAndTimeCalendar.setOnClickListener {
            lifecycleScope.launch {
                val responseList = CalendarDialogUtil.showContinuousCalendar(
                    this@MainActivity,
                    CalendarRequest()
                        .addNextRequest(DateCalenderType.StartNowCalender(themeResId = R.style.DatePickerDialogTheme))
                        .addNextRequest(TimePickerType.NormalTimePicker(true, R.style.CustomTimePickerDialog))
                ) {
                    Toast.makeText(this@MainActivity, "cancel date and time by user!!", Toast.LENGTH_SHORT).show()
                }

                responseList.forEach {
                    when (it) {

                        is DateInfo -> {
                            setDate(it.calendar)
                        }

                        is TimeInfo -> {
                            setTime(it.hourOfDay, it.minute)
                        }
                    }

                }
            }
        }
    }

    private fun setDate(calendar: Calendar) {
        viewBinding.txtDate.text = dateTimePattern.getCalenderString(calendar)
    }

    private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        viewBinding.txtDate.text = "${year}/${month}/${dayOfMonth}"
    }

    private fun setTime(hourOfDay: Int, minute: Int) {
        viewBinding.txtTime.text = "${hourOfDay}:${minute}"
    }

}