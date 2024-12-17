package com.example.vettrack.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentManager
import com.example.vettrack.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import timber.log.Timber
import java.util.Calendar

fun showAlertDialog(
    context: Context,
    title: String,
    message: String,
    hasCancelButton: Boolean,
    isCancelable: Boolean,
    icon: Drawable?,
    onPositiveButtonClicked: () -> Unit
) {
    Timber.d("DialogsUtils_TAG: showConfirmDelete: ")
    val dialog = MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(isCancelable)
        .setPositiveButton(context.resources.getString(R.string.txt_accept)) { _, _ ->
            onPositiveButtonClicked()
        }

    if (hasCancelButton)
        dialog.setNegativeButton(context.resources.getString(R.string.txt_cancel), null)

    if (icon != null)
        dialog.setIcon(icon)

    dialog.create().show()
}

fun openDatePicker(
    dateMills: Long,
    futureConstraints: Boolean,
    supportFragmentManager: FragmentManager,
    onPositiveButtonClicked: (Long) -> Unit
) {
    Timber.d("DialogUtils_TAG: openDatePicker: ")

    val constraints = if (futureConstraints)
        CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build()
    else
        CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build()

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setCalendarConstraints(constraints)
        .setSelection(dateMills)
        .build()

    datePicker.addOnPositiveButtonClickListener { dateLong ->
        onPositiveButtonClicked(dateLong)
    }

    datePicker.show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
}

fun openTimePicker(
    defaultDate: String?,
    isFutureConstrained: Boolean,
    supportFragmentManager: FragmentManager,
    onPositiveButtonClicked: (String) -> Unit,
    onNegativeButtonClicked: () -> Unit
) {
    Timber.d("DialogUtils_TAG: openTimePicker: ")
    var hour = 12
    var minute = 0

    val calendar = Calendar.getInstance()
    if (isFutureConstrained) {
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
    }

    try {
        defaultDate?.let {
            if (it.isNotBlank()) {
                val timeString = it.split(" ")[1] + " " + it.split(" ")[2]
                val (h, m) = parseTimeToHourMinute(timeString)
                hour = h
                minute = m
            }
        }
    } catch (e: Exception) {
        Timber.d("DialogUtils_TAG: openTimePicker: ERROR set time: ${e.message} ")
    }

    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .setHour(hour)
        .setMinute(minute)
        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
        .build()

    timePicker.addOnPositiveButtonClickListener {
        val formattedTime = getTime(timePicker)
        onPositiveButtonClicked(formattedTime)
    }

    timePicker.addOnNegativeButtonClickListener {
        onNegativeButtonClicked()
    }

    timePicker.show(supportFragmentManager, TIME_PICKER_DIALOG_TAG)
}
