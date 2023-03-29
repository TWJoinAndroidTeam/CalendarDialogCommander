package com.example.calendarlibrary

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.caledardialogcommander.model.*
import com.example.caledardialogcommander.ui.CalendarDialogUtil
import com.example.calendarlibrary.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding


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
            CalendarDialogUtil.showCalendarDateDialog(
                this, DateCalenderType.StartNowCalender(themeResId = R.style.DatePickerDialogTheme)
            ) { view, year, month, dayOfMonth ->
                setDate(year, month, dayOfMonth)
            }
        }

        viewBinding.btnTimeCalendar.setOnClickListener {
            CalendarDialogUtil.showCalendarTimeDialog(
                this, TimePickerType.NormalTimePicker(true, themeResId = R.style.CustomTimePickerDialog)
            ) { view, hourOfDay, minute ->
                setTime(hourOfDay, minute)
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
                    Toast.makeText(this@MainActivity, "cancel by user!!", Toast.LENGTH_SHORT).show()
                }

                responseList.forEach {
                    when (it) {

                        is DateInfo -> {
                            setDate(it.year, it.month, it.dayOfMonth)
                        }

                        is TimeInfo -> {
                            setTime(it.hourOfDay, it.minute)
                        }
                    }

                }
            }


        }
    }

    private fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        viewBinding.txtDate.text = "${year}/${month + 1}/${dayOfMonth}"
    }

    private fun setTime(hourOfDay: Int, minute: Int) {
        viewBinding.txtTime.text = "${hourOfDay}:${minute}"
    }

}