package com.example.mesibajk

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(
    val rideTime: MutableMap<String, String>,
    val startDateView: TextView,
    val endDateView: TextView
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        when (this.tag) {
            "startDatePicker" -> {
                val yearString = year.toString()
                val monthString = formatToTwoPlaces(month)
                val dayString = formatToTwoPlaces(day)
                val startYMD = ("$yearString-$monthString-$dayString")
                rideTime["start_YYYY"] = yearString
                rideTime["start_MM"] = monthString
                rideTime["start_DD"] = dayString
                startDateView.text = startYMD
            }
            "endDatePicker" -> {
                val yearString = year.toString()
                val monthString = formatToTwoPlaces(month)
                val dayString = formatToTwoPlaces(day)
                val endYMD = ("$yearString-$monthString-$dayString")
                rideTime["end_YYYY"] = yearString
                rideTime["end_MM"] = monthString
                rideTime["end_DD"] = dayString
                endDateView.text = endYMD
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