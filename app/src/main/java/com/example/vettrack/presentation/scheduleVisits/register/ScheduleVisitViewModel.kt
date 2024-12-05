package com.example.vettrack.presentation.scheduleVisits.register

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.vettrack.presentation.base.BaseVisitsViewModel
import com.example.vettrack.presentation.utils.MESSAGE_KEY
import com.example.vettrack.presentation.utils.NOTIFICATION_ID
import com.example.vettrack.presentation.utils.TITLE_KEY
import com.example.vettrack.repository.VisitsRepository
import com.example.vettrack.utils.NotificationReceiver
import timber.log.Timber

class ScheduleVisitViewModel(private val visitRepository: VisitsRepository) : BaseVisitsViewModel() {
    val successOperation = MutableLiveData<Boolean>()
    val loading = MutableLiveData(false)

    //region DATA VALIDATIONS
    init {
        buttonEnabled.addSource(date) { validateForm() }
        buttonEnabled.addSource(petName) { validateForm() }
        buttonEnabled.addSource(clinicName) { validateForm() }
    }
    override fun validateForm() {
        Timber.d("ScheduleVisitViewModel_TAG: validateForm: ")
        buttonEnabled.value = !date.value.isNullOrBlank()
                && !petName.value.isNullOrBlank()
                && !clinicName.value.isNullOrBlank()
    }
    //endregion

    fun scheduleVisit() {
        Timber.d("ScheduleVisitViewModel_TAG: registerVisit: ")
        val scheduledVisit = mapVisit()
        visitRepository.registerVisit(
            scheduledVisit,
            onSuccess = {
                successOperation.postValue(true)
                loading.postValue(false)
            },
            onFailure = {
                successOperation.postValue(false)
                loading.postValue(false)
            }
        )
    }

    fun scheduleNotification(context: Context, title: String, message: String, date: Long) {
        Timber.d("ScheduleVisitViewModel_TAG: schedule: ")
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
}