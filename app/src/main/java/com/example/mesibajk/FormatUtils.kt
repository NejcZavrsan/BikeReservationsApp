package com.example.mesibajk

class FormatUtils {

    companion object {

        fun formatToTwoPlaces(timeDate: Int): String {
            return if (timeDate < 10) {
                "0$timeDate"
            } else
                timeDate.toString()
        }
    }
}