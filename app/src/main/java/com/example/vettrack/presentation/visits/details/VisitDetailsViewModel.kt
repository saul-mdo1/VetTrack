package com.example.vettrack.presentation.visits.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.utils.formatToCurrency
import com.example.vettrack.repository.VisitsRepository
import timber.log.Timber

class VisitDetailsViewModel(private val visitsRepository: VisitsRepository) : ViewModel() {
    val loading = MutableLiveData(true)
    val visit = MutableLiveData<VisitModel>()

    val cityVisible = MutableLiveData(true)
    val observationsVisible = MutableLiveData(true)
    val totalPaidFormatted = MutableLiveData("")

    fun getVisitById(id: String) {
        Timber.d("VisitDetailsViewModel_TAG: getVisitById: ")
        visitsRepository.getVisitById(
            id,
            onSuccess = {
                val total = formatToCurrency(it?.totalPaid ?: "0")
                totalPaidFormatted.postValue(total)

                if(it?.city.isNullOrBlank())
                    cityVisible.postValue(false)

                if(it?.observations.isNullOrBlank())
                    observationsVisible.postValue(false)

                visit.postValue(it)
                loading.postValue(false)
            },
            onFailure = {
                loading.postValue(false)
            }
        )
    }
}