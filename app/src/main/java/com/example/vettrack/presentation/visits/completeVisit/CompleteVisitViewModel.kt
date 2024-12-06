package com.example.vettrack.presentation.visits.completeVisit

import androidx.lifecycle.MutableLiveData
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.base.BaseVisitsViewModel
import com.example.vettrack.repository.VisitsRepository
import timber.log.Timber

class CompleteVisitViewModel(private val visitsRepository: VisitsRepository) : BaseVisitsViewModel() {
    val successOperation = MutableLiveData<Boolean>()

    //region DATA VALIDATIONS
    init {
        buttonEnabled.addSource(reason) { validateForm() }
        buttonEnabled.addSource(petWeight) { validateForm() }
        buttonEnabled.addSource(totalPaid) { validateForm() }
    }

    override fun validateForm() {
        Timber.d("CompleteVisitViewModel_TAG: validateForm: ")
        buttonEnabled.value = !reason.value.isNullOrBlank()
                && !petWeight.value.isNullOrBlank()
                && !totalPaid.value.isNullOrBlank()
    }
    //endregion

    override fun updateVisit() {
        Timber.d("CompleteVisitViewModel_TAG: updateVisit: ")
        val visit = mapVisit()
        visitsRepository.updateVisit(
            documentId,
            visit,
            onSuccess = {
                successOperation.postValue(true)
            },
            onFailure = {
                successOperation.postValue(false)
            }
        )
    }

    fun setVisitData(visit: VisitModel) {
        Timber.d("CompleteVisitViewModel_TAG: setVisitData: ")
        documentId = visit.id
        date.postValue(visit.date)
        petName.postValue(visit.petName)
        clinicName.postValue(visit.clinicName)
    }
}