package com.example.vettrack.presentation.pets.list

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
    val petsNum = MutableLiveData("0")
    val searchQuery = MutableLiveData<String?>()

    val petsList = MediatorLiveData<List<PetModel>>().apply {
        addSource(_petsList) {
            this.postValue(it)
        }
        addSource(searchQuery) { searchQuery ->
            if (searchQuery != null)
                filterPets(searchQuery)
            else
                this.postValue(_petsList.value)
        }
    }

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

    private fun filterPets(query: String) {
        Timber.d("PetsViewModel_TAG: filterPets: ")
        val list = petsList.value?.filter { visit ->
            visit.name.contains(query, ignoreCase = true)
                    || visit.species.lowercase().contains(query, ignoreCase = true)
                    || visit.breed?.lowercase()?.contains(query, ignoreCase = true) == true
                    || visit.color?.lowercase()?.contains(query, ignoreCase = true) == true
        }
        petsList.postValue(list)
    }
}