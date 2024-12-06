package com.example.vettrack.presentation.visits.scheduleVisits.register

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.vettrack.presentation.base.BaseVisitsViewModel
import com.example.vettrack.repository.VisitsRepository
import com.example.vettrack.utils.scheduleNotification
import timber.log.Timber

class ScheduleVisitViewModel(private val visitRepository: VisitsRepository) : BaseVisitsViewModel() {
    val successOperation = MutableLiveData<Boolean>()

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

    fun setNotification(context: Context, title: String, message: String, date: Long) {
        Timber.d("ScheduleVisitViewModel_TAG: schedule: ")
        scheduleNotification(context, title, message, date)
    }
}