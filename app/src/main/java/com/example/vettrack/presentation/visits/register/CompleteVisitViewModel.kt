package com.example.vettrack.presentation.visits.register

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.repository.VisitsRepository
import timber.log.Timber

class CompleteVisitViewModel(private val visitsRepository: VisitsRepository) : ViewModel() {
    val successOperation = MutableLiveData(false)
    val errorOperation = MutableLiveData(false)
    var documentId = ""

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
        addSource(reason) { validateForm() }
        addSource(petWeight) { validateForm() }
        addSource(petName) { validateForm() }
        addSource(clinicName) { validateForm() }
        addSource(totalPaid) { validateForm() }
    }
    //endregion

    private fun validateForm() {
        Timber.d("CompleteVisitViewModel_TAG: validateForm: ")
        buttonEnabled.value = !date.value.isNullOrBlank()
                && !reason.value.isNullOrBlank()
                && !petWeight.value.isNullOrBlank()
                && !petName.value.isNullOrBlank()
                && !clinicName.value.isNullOrBlank()
                && !totalPaid.value.isNullOrBlank()
    }

    private fun updateVisit() {
        Timber.d("RegisterVisitViewModel_TAG: updateVisit: ")
        val visit = mapVisit()
        visitsRepository.updateVisit(
            documentId,
            visit,
            onSuccess = {
                successOperation.postValue(true)
            },
            onFailure = {
                errorOperation.postValue(true)
            }
        )
    }

    private fun mapVisit(): HashMap<String, Any?> {
        Timber.d("CompleteVisitViewModel_TAG: mapVisit: ")
        return hashMapOf(
            "date" to (date.value ?: ""),
            "reason" to (reason.value ?: ""),
            "petName" to (petName.value ?: ""),
            "dogWeight" to (petWeight.value ?: ""),
            "clinicName" to (clinicName.value ?: ""),
            "city" to city.value,
            "totalPaid" to (totalPaid.value ?: "0"),
            "observations" to observations.value,
            "pending" to false,
            "userId" to userId
        )
    }
}