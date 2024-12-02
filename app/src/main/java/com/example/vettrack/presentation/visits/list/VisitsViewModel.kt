package com.example.vettrack.presentation.visits.list

import android.os.Build
import androidx.annotation.RequiresApi
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
            visits.filter { isVisitPending(it.date) }
        }

    private val pastList: LiveData<List<VisitModel>>
        get() = _visitsList.map { visits ->
            visits.filter { !isVisitPending(it.date) }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getVisits(uid: String) {
        Timber.d("VisitsViewModel_TAG: getVisits: ")
        visitsRepository.getVisits(
            uid,
            onSuccess = { visits ->
                _visitsList.postValue(visits.sortByDate())
                futureEnabled.postValue(false)
            },
            onFailure = {
                _visitsList.postValue(emptyList())
                visitsNum.postValue("0")
            }
        )
    }

    private fun filterVisitsByStatus(future: Boolean) {
        Timber.d("VisitsViewModel_TAG: filterVisitsByStatus: ")
        if (future) {
            visitsList.postValue(futureList.value)
            visitsNum.postValue(futureList.value?.size.toString())
        } else {
            visitsList.postValue(pastList.value)
            visitsNum.postValue(pastList.value?.size.toString())
        }
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