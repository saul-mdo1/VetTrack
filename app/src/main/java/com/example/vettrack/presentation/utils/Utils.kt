package com.example.vettrack.presentation.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

fun wait(seconds: Int, afterDone: () -> Unit) = GlobalScope.launch {
    delay((seconds * 1000).toLong())
    withContext(Dispatchers.Main) {
        afterDone()
    }
}

fun formatToCurrency(value: String, locale: Locale = Locale.getDefault()): String {
    val number = value.toDoubleOrNull() ?: return value

    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    return currencyFormat.format(number)
}