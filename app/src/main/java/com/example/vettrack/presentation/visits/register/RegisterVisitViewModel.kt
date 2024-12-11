package com.example.vettrack.presentation.visits.register

import androidx.lifecycle.MutableLiveData
import com.example.vettrack.R
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.base.BaseVisitsViewModel
import com.example.vettrack.repository.VisitsRepository
import timber.log.Timber

class RegisterVisitViewModel(private val visitsRepository: VisitsRepository) : BaseVisitsViewModel() {
    val successOperation = MutableLiveData<Boolean>()

    val visit = MutableLiveData<VisitModel>()
    var isUpdate = false
    val buttonText = MutableLiveData(R.string.txt_register)

    //region VALIDATIONS
    init {
        buttonEnabled.addSource(date) { validateForm() }
        buttonEnabled.addSource(reason) { validateForm() }
        buttonEnabled.addSource(petWeight) { validateForm() }
        buttonEnabled.addSource(petName) { validateForm() }
        buttonEnabled.addSource(clinicName) { validateForm() }
        buttonEnabled.addSource(totalPaid) { validateForm() }
    }

    override fun validateForm() {
        Timber.d("RegisterVisitViewModel_TAG: validateForm: ")
        buttonEnabled.value = !date.value.isNullOrBlank()
                && !reason.value.isNullOrBlank()
                && !petWeight.value.isNullOrBlank()
                && !petName.value.isNullOrBlank()
                && !clinicName.value.isNullOrBlank()
                && !totalPaid.value.isNullOrBlank()
    }
    //endregion

    fun applyAction() {
        Timber.d("RegisterVisitViewModel_TAG: applyAction: ")
        loading.postValue(true)
        if (isUpdate)
            updateVisit()
        else
            registerVisit()
    }

    override fun registerVisit() {
        Timber.d("RegisterVisitViewModel_TAG: registerVisit: ")
        val visit = mapVisit()
        visitsRepository.registerVisit(
            visit,
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

    override fun updateVisit() {
        Timber.d("RegisterVisitViewModel_TAG: updateVisit: ")
        val visit = mapVisit()
        visitsRepository.updateVisit(
            documentId,
            visit,
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

    fun getVisitById() {
        Timber.d("VisitDetailsViewModel_TAG: getVisitById: ")
        visitsRepository.getVisitById(
            documentId,
            onSuccess = {
                isUpdate = true
                buttonText.postValue(R.string.txt_update)

                date.postValue(it?.date)
                reason.postValue(it?.reason)
                petWeight.postValue(it?.dogWeight)
                petName.postValue(it?.petName)
                clinicName.postValue(it?.clinicName)
                city.postValue(it?.city)
                observations.postValue(it?.observations)
                totalPaid.postValue(it?.totalPaid)

                visit.postValue(it)
                loading.postValue(false)
            },
            onFailure = {
                isUpdate = true
                buttonText.postValue(R.string.txt_update)
                loading.postValue(false)
            }
        )
    }
}