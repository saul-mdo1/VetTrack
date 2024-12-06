package com.example.vettrack.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.vettrack.presentation.utils.MESSAGE_KEY
import com.example.vettrack.presentation.utils.NOTIFICATION_ID
import com.example.vettrack.presentation.utils.TITLE_KEY
import timber.log.Timber

fun scheduleNotification(context: Context, title: String, message: String, date: Long) {
    Timber.d("NotificationsUtils_TAG: scheduleNotification: ")
    val intent = Intent(context, NotificationReceiver::class.java)
    intent.putExtra(TITLE_KEY, title)
    intent.putExtra(MESSAGE_KEY, message)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        date,
        pendingIntent
    )
}