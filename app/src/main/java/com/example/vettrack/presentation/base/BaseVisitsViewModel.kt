package com.example.vettrack.presentation.base

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.presentation.utils.isVisitPending
import timber.log.Timber

abstract class BaseVisitsViewModel : ViewModel() {
    var documentId = ""
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

    //region VALIDATORS
    val buttonEnabled = MediatorLiveData<Boolean>()
    protected abstract fun validateForm()
    //endregion

    protected fun mapVisit(): HashMap<String, Any?> {
        Timber.d("BaseVisitsViewModel_TAG: mapVisit: ")
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
}