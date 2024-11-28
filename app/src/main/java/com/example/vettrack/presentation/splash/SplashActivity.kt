package com.example.vettrack.presentation.splash

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vettrack.core.Session
import com.example.vettrack.presentation.authentication.login.LoginActivity
import com.example.vettrack.presentation.home.MainActivity
import com.example.vettrack.presentation.utils.NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.vettrack.presentation.utils.NOTIFICATION_CHANNEL_ID
import com.example.vettrack.presentation.utils.NOTIFICATION_CHANNEL_NAME
import com.example.vettrack.utils.saveFCMToken
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("SplashActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        getFCMToken()
        validateLoggedUser()
    }

    private fun validateLoggedUser() {
        Timber.d("SplashActivity_TAG: validateLoggedUser: ")
        viewModel.getLoggedUser()

        viewModel.userLogged.observe(this) { user ->
            if (user != null)
                navigateTo(MainActivity::class.java)
            else
                navigateTo(LoginActivity::class.java)
        }
    }

    private fun <T> navigateTo(activity: Class<T>) {
        Timber.d("SplashActivity_TAG: navigateToHome: ")
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()
    }

    private fun getFCMToken() {
        Timber.d("SplashActivity_TAG: getFCMToken: ")
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Timber.d("SplashActivity_TAG: getFCMToken: SUCCESS: $token")
                saveFCMToken(this, token)
                Session.deviceId = token
            } else {
                Timber.d("SplashActivity_TAG: getFCMToken: ERROR:")
            }
        }
    }

    private fun createNotificationChannel() {
        Timber.d("SplashActivity_TAG: createNotificationChannel: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            channel.description = NOTIFICATION_CHANNEL_DESCRIPTION
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}