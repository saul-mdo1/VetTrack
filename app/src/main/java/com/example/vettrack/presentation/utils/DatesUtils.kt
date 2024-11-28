package com.example.vettrack.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vettrack.models.VisitModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertMillisToDate(millis: Long): String {
    return try {
        val formatter = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.format(Date(millis))
    } catch (e: Exception) {
        Timber.d("DatesUtils_TAG: convertMillisToDate: ")
        currentDate()
    }
}

fun convertDateToMills(dateString: String): Long {
    return try {
        val formatter = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        val date = formatter.parse(dateString)
        date?.time ?: MaterialDatePicker.todayInUtcMilliseconds()
    } catch (e: Exception) {
        e.printStackTrace()
        MaterialDatePicker.todayInUtcMilliseconds()
    }
}

fun convertDateTimeToMills(dateTimeString: String, offsetHours: Int): Long? {
    return try {
        val formatter = SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.getDefault())
        val date = formatter.parse(dateTimeString)
        date?.let {
            it.time - offsetHours * 60 * 60 * 1000
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun parseTimeToHourMinute(timeString: String): Pair<Int, Int> {
    return try {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = formatter.parse(timeString)
        val calendar = Calendar.getInstance()
        calendar.time = date!!

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        Pair(hour, minute)
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(0, 0)
    }
}

fun getTime(timePicker: MaterialTimePicker): String {
    return try {
        val selectedHour = timePicker.hour
        val selectedMinute = timePicker.minute

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        timeFormat.format(calendar.time)
    } catch (e: Exception) {
        Timber.d("DatesUtils_TAG: getTime: error ${e.message} ")
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        timeFormat.format(Date())
    }
}

fun currentDate(): String {
    return try {
        val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        dateFormat.format(Date())
    } catch (e: Exception) {
        Timber.d("DatesUtils_TAG: currentDate: error ${e.message} ")
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun List<VisitModel>.sortByDate(): List<VisitModel> {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy h:mm a", Locale.getDefault())

        this.sortedByDescending { dateTimeString ->
            LocalDateTime.parse(dateTimeString.date, formatter)
        }
    } catch (e: Exception) {
        Timber.d("DatesUtils_TAG: sortByDate: ERROR: ${e.message} ")
        this
    }
}

fun isVisitPending(dateString: String?): Boolean {
    val dateFormat = SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.getDefault())
    return try {
        val visitDate = dateString?.let { dateFormat.parse(it) }
        visitDate?.after(Date()) ?: false
    } catch (e: Exception) {
        false
    }
}