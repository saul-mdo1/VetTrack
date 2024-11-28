package com.example.vettrack.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vettrack.R
import com.example.vettrack.utils.saveFCMToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FCMService : FirebaseMessagingService() {

    private val CHANNEL_ID = "vetChannel"
    private val CHANNEL_NAME = "Vet Notifications"

    override fun onNewToken(token: String) {
        Timber.d("FCMService_TAG: onNewToken: ")
        super.onNewToken(token)
        saveFCMToken(this, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.d("FCMService_TAG: onMessageReceived: ")
        super.onMessageReceived(message)
        message.notification?.let {
            sendNotification(it.title ?: "Notification", it.body ?: "No content")
        }
    }

    private fun sendNotification(title: String, message: String) {
        Timber.d("FCMService_TAG: sendNotification: title: $title - message: $message ")
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(0, notification)
    }
}