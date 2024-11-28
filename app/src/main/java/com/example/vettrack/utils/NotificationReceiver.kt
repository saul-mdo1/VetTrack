package com.example.vettrack.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.vettrack.R
import com.example.vettrack.presentation.utils.MESSAGE_KEY
import com.example.vettrack.presentation.utils.NOTIFICATION_CHANNEL_ID
import com.example.vettrack.presentation.utils.NOTIFICATION_ID
import com.example.vettrack.presentation.utils.TITLE_KEY
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("NotificationReceiver_TAG: onReceive: ")
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(intent.getStringExtra(TITLE_KEY))
            .setContentText(intent.getStringExtra(MESSAGE_KEY))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)

    }
}