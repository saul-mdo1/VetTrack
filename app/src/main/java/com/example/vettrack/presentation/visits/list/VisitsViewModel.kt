package com.example.vettrack.presentation.visits.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.utils.sortByDate
import com.example.vettrack.repository.VisitsRepository
import timber.log.Timber

class VisitsViewModel(private val visitsRepository: VisitsRepository) : ViewModel() {
    private val _visitsList = MutableLiveData(emptyList<VisitModel>())
    val visitsList: LiveData<List<VisitModel>> get() = _visitsList

    val loading = MutableLiveData(true)
    val visitsNum = MutableLiveData("0")

    val deleted = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getVisits(uid: String) {
        Timber.d("VisitsViewModel_TAG: getVisits: ")
        visitsRepository.getVisits(
            uid,
            onSuccess = { visits ->
                try {
                    _visitsList.postValue(visits.sortByDate())
                    visitsNum.postValue(visits.size.toString())
                } catch (e: Exception) {
                    _visitsList.postValue(visits)
                    visitsNum.postValue("0")
                }
            },
            onFailure = {
                _visitsList.postValue(emptyList())
                visitsNum.postValue("0")
            }
        )
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