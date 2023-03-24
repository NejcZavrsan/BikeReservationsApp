package com.example.mesibajk.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(
    var rideTime: MutableMap<String, String>,
    var startTimeView: TextView,
    var endTimeView: TextView
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        when (this.tag) {
            "startTimePicker" -> {
                val hourOfDayString = formatToTwoPlaces(hourOfDay)
                val minuteString = formatToTwoPlaces(minute)
                val startMMHH = ("$hourOfDayString:$minuteString")
                rideTime["start_hh"] = hourOfDayString
                rideTime["start_mm"] = minuteString
                startTimeView.text = startMMHH
            }
            "endTimePicker" -> {
                val hourOfDayString = formatToTwoPlaces(hourOfDay)
                val minuteString = formatToTwoPlaces(minute)
                val startMMHH = ("$hourOfDayString:$minuteString")
                rideTime["end_hh"] = hourOfDayString
                rideTime["end_mm"] = minuteString
                endTimeView.text = startMMHH
            }
        }
    }

    fun formatToTwoPlaces(timeDate: Int): String {
        return if (timeDate < 10) {
            "0$timeDate"
        } else
            timeDate.toString()
    }
}