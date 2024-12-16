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
import timber.log.Timber

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