package com.kolosov.congratulations.ui.utils

import android.content.Context
import com.kolosov.congratulations.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object CustomDateFormat {

    private val month = arrayOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
    )

    private var dateArray: Array<String>? = null

    private fun checkFormat() {
        if (dateArray!![1].toInt() < 10) {
            dateArray!![1] = "0" + dateArray!![1]
        }
        if (dateArray!![0].toInt() < 10) {
            dateArray!![0] = "0" + dateArray!![0]
        }
    }

    fun getBigDate(context: Context): String {
        dateArray = getCurrentDate()?.split("/")?.toTypedArray()
        checkFormat()
        return context.getString(
            R.string.big_date, dateArray?.get(1),
            dateArray?.get(0)
        )
    }

    fun getTitleDate(context: Context): String {
        dateArray = getCurrentDate()?.split("/")?.toTypedArray()
        checkFormat()
        return context.getString(
            R.string.date, dateArray?.get(1), getMonth(
                dateArray?.get(0)!!.toInt()
            )
        )
    }

    fun getCurrentDate(): String? {
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("M/d/yy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun getMonth(monthNumber: Int): String {
        return month[monthNumber - 1]
    }
}