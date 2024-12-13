package com.example.vettrack.presentation.pets.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.models.Gender
import com.example.vettrack.models.PetModel
import com.example.vettrack.repository.PetsRepository
import timber.log.Timber

class PetDetailsViewModel(private val petRepository: PetsRepository) : ViewModel() {
    val pet = MutableLiveData<PetModel?>()
    val loading = MutableLiveData(true)
    var documentId = ""

    val gender = MutableLiveData(Gender.UNKNOWN.stringId)
    val colorVisible = MutableLiveData(false)
    val breedVisible = MutableLiveData(false)

    fun getPetById() {
        Timber.d("PetDetailsViewModel_TAG: getPetById: ")
        petRepository.getPetById(
            documentId,
            onSuccess = { p ->
                pet.postValue(p)
                colorVisible.postValue(!(p?.color.isNullOrBlank()))
                breedVisible.postValue(!(p?.breed.isNullOrBlank()))
                gender.postValue(Gender.fromId(p?.gender ?: 0).stringId)
                loading.postValue(false)
            },
            onFailure = {
                loading.postValue(false)
            }
        )
    }
}