package com.example.vettrack.presentation.utils

import android.content.Context
import com.example.vettrack.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

fun showConfirmDeleteDialog(
    context: Context,
    title: String,
    message: String,
    onPositiveButtonClicked: () -> Unit
) {
    Timber.d("DialogsUtils_TAG: showConfirmDelete: ")
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(context.resources.getString(R.string.txt_accept)) { _, _ ->
            onPositiveButtonClicked()
        }
        .setNegativeButton(context.resources.getString(R.string.txt_cancel), null)
        .create()
        .show()
}