package com.example.vettrack.presentation.visits.register

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.R
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.utils.isVisitPending
import com.example.vettrack.repository.VisitsRepository
import timber.log.Timber

class RegisterVisitViewModel(private val visitsRepository: VisitsRepository) : ViewModel() {
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
        addSource(reason) { validateForm() }
        addSource(petWeight) { validateForm() }
        addSource(petName) { validateForm() }
        addSource(clinicName) { validateForm() }
        addSource(totalPaid) { validateForm() }
    }
    //endregion

    val visit = MutableLiveData<VisitModel>()
    var isUpdate = false
    var documentId = ""
    val buttonText = MutableLiveData(R.string.txt_register)

    private fun validateForm() {
        Timber.d("SignUpViewModel_TAG: validateForm: ")
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
        val visit = mapVisit()
        if (isUpdate)
            updateVisit(visit)
        else
            registerVisit(visit)
    }

    private fun registerVisit(visit: HashMap<String, Any?>) {
        Timber.d("RegisterVisitViewModel_TAG: registerVisit: ")
        visitsRepository.registerVisit(
            visit,
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

    private fun updateVisit(visit: HashMap<String, Any?>) {
        Timber.d("RegisterVisitViewModel_TAG: updateVisit: ")
        visitsRepository.updateVisit(
            documentId,
            visit,
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