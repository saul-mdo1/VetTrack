package com.example.vettrack.presentation.visits.list

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.utils.isVisitPending
import com.example.vettrack.presentation.utils.sortByDate
import com.example.vettrack.repository.VisitsRepository
import timber.log.Timber

class VisitsViewModel(private val visitsRepository: VisitsRepository) : ViewModel() {
    private val _visitsList = MutableLiveData(emptyList<VisitModel>())

    private val futureList: LiveData<List<VisitModel>>
        get() = _visitsList.map { visits ->
            visits.filter { it.pending }
        }

    private val pastList: LiveData<List<VisitModel>>
        get() = _visitsList.map { visits ->
            visits.filter { !it.pending }
        }

    val loading = MutableLiveData(true)
    val visitsNum = MutableLiveData("0")
    val deleted = MutableLiveData<Boolean>()
    val futureEnabled = MutableLiveData<Boolean>()

    val visitsList = MediatorLiveData<List<VisitModel>>().apply {
        addSource(futureEnabled) { future ->
            filterVisitsByStatus(future)
        }
        addSource(_visitsList) {
            filterVisitsByStatus(futureEnabled.value ?: false)
        }
    }

    val emptyListVisible = MediatorLiveData<Boolean>().apply {
        addSource(visitsList) { visits ->
            value = visits.isNullOrEmpty()
        }
    }

    fun getVisits(uid: String) {
        Timber.d("VisitsViewModel_TAG: getVisits: ")
        loading.postValue(true)
        visitsRepository.getVisits(
            uid,
            onSuccess = { visits ->
                Timber.d("VisitsViewModel_TAG: getVisits: SUCCESS: items ${visits.size}")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    validatePendingStatus(visits.sortByDate())
                } else
                    validatePendingStatus(visits)
                futureEnabled.postValue(false)
            },
            onFailure = {
                Timber.d("VisitsViewModel_TAG: getVisits: ERROR ")
                _visitsList.postValue(emptyList())
                visitsNum.postValue("0")
            }
        )
        loading.postValue(false)
    }

    private fun validatePendingStatus(listResponse: List<VisitModel>) {
        Timber.d("VisitsViewModel_TAG: validatePendingStatus: ")
        val list = listResponse.map { visit ->
            if (visit.pending && !isVisitPending(visit.date))
                visit.copy(pending = false)
            else
                visit
        }

        _visitsList.postValue(list)
    }

    private fun filterVisitsByStatus(future: Boolean) {
        Timber.d("VisitsViewModel_TAG: filterVisitsByStatus: ")
        val list = if (future) futureList.value else pastList.value
        visitsList.postValue(list)
        visitsNum.postValue(list?.size.toString())
    }

    fun filterList(query: String): List<VisitModel>? {
        Timber.d("VisitsViewModel_TAG: filterList: query: $query")
        val list = visitsList.value?.filter { visit ->
            visit.petName.contains(query, ignoreCase = true)
                    || visit.clinicName.lowercase().contains(query, ignoreCase = true)
                    || visit.date.lowercase().contains(query, ignoreCase = true)
                    || visit.reason.lowercase().contains(query, ignoreCase = true)
        }
        visitsNum.postValue(list?.size.toString())
        return list
    }

    fun deleteVisit(id: String) {
        Timber.d("VisitsViewModel_TAG: deleteVisit: ")
        loading.postValue(true)
        visitsRepository.deleteVisit(
            id,
            onSuccess = {
                deleted.postValue(true)
                loading.postValue(false)
            },
            onFailure = {
                deleted.postValue(false)
                loading.postValue(false)
            }
        )
    }
}