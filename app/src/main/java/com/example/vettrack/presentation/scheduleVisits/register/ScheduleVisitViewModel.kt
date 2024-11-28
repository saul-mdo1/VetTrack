package com.example.vettrack.presentation.scheduleVisits.register

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.presentation.utils.MESSAGE_KEY
import com.example.vettrack.presentation.utils.NOTIFICATION_ID
import com.example.vettrack.presentation.utils.TITLE_KEY
import com.example.vettrack.presentation.utils.isVisitPending
import com.example.vettrack.repository.VisitsRepository
import com.example.vettrack.utils.NotificationReceiver
import timber.log.Timber

class ScheduleVisitViewModel(private val visitRepository: VisitsRepository) : ViewModel() {
    val successOperation = MutableLiveData(false)
    val errorOperation = MutableLiveData(false)
    val loading = MutableLiveData(false)

    //region DATA
    val date = MutableLiveData("")
    val reason = MutableLiveData("")
    val petWeight = MutableLiveData("")
    val petName = MutableLiveData("")
    val clinicName = MutableLiveData("")
    val city = MutableLiveData<String?>(null)
    val totalPaid = MutableLiveData("")
    val observations = MutableLiveData<String?>(null)
    var userId = ""
    //endregion

    //region Data Validators
    val buttonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(date) { validateForm() }
        addSource(petName) { validateForm() }
        addSource(clinicName) { validateForm() }
    }

    private fun validateForm() {
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
                errorOperation.postValue(true)
                loading.postValue(false)
            }
        )
    }

    private fun mapVisit(): HashMap<String, Any?> {
        Timber.d("ScheduleVisitViewModel_TAG: mapVisit: ")
        return hashMapOf(
            "date" to (date.value ?: ""),
            "reason" to (reason.value ?: ""),
            "petName" to (petName.value ?: ""),
            "dogWeight" to (petWeight.value ?: ""),
            "clinicName" to (clinicName.value ?: ""),
            "city" to city.value,
            "totalPaid" to (totalPaid.value ?: "0"),
            "observations" to observations.value,
            "pending" to isVisitPending(date.value),
            "userId" to userId
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