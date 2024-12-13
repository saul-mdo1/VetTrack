package com.example.vettrack.presentation.pets.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.models.PetModel
import com.example.vettrack.repository.PetsRepository
import timber.log.Timber

class PetsViewModel(private val petsRepository: PetsRepository) : ViewModel() {
    private val _petsList = MutableLiveData(emptyList<PetModel>())
    val loading = MutableLiveData(true)
    val deleted = MutableLiveData<Boolean>()

    val petsList: LiveData<List<PetModel>>
        get() = _petsList

    val petsNum = MutableLiveData("0")
    val searchQuery = MutableLiveData<String?>()

    val emptyListVisible = MediatorLiveData<Boolean>().apply {
        addSource(petsList) { visits ->
            value = visits.isNullOrEmpty()
        }
    }

    fun getPets(uid: String) {
        Timber.d("PetsViewModel_TAG: getPets: ")
        petsRepository.getPets(
            uid,
            onSuccess = { pets ->
                val sortedList = pets.sortedBy { it.name }
                _petsList.postValue(sortedList)
                petsNum.postValue(pets.size.toString())
                loading.postValue(false)
            },
            onFailure = {
                _petsList.postValue(emptyList())
                petsNum.postValue("0")
                loading.postValue(false)
            }
        )
    }

    fun deleteVisit(id: String) {
        Timber.d("VisitsViewModel_TAG: deleteVisit: ")
        loading.postValue(true)
        petsRepository.deletePet(
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