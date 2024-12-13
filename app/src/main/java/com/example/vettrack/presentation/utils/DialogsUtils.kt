package com.example.vettrack.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.example.vettrack.R
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