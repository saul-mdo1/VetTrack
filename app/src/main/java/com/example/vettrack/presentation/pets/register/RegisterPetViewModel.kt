package com.example.vettrack.presentation.pets.register

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.R
import com.example.vettrack.models.PetModel
import com.example.vettrack.repository.PetsRepository
import timber.log.Timber

class RegisterPetViewModel(private val petsRepository: PetsRepository) : ViewModel() {
    val loading = MutableLiveData(false)
    val successOperation = MutableLiveData<Boolean>()
    val pet = MutableLiveData<PetModel>()

    val name = MutableLiveData("")
    val gender = MutableLiveData(-1)
    val species = MutableLiveData("")
    val birthdate = MutableLiveData("")
    val breed = MutableLiveData("")
    val color = MutableLiveData("")
    var userId = ""

    var isUpdate = false
    var documentId = ""
    val buttonText = MutableLiveData(R.string.txt_register)
    val screenTitle = MutableLiveData(R.string.txt_register_pet)

    //region VALIDATIONS
    val buttonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(name) { validateForm() }
        addSource(gender) { validateForm() }
        addSource(species) { validateForm() }
    }

    private fun validateForm() {
        Timber.d("RegisterPetViewModel_TAG: validateForm: ")
        buttonEnabled.value = !name.value.isNullOrBlank()
                && gender.value != -1
                && !species.value.isNullOrBlank()
    }
    //endregion

    fun applyAction() {
        Timber.d("RegisterPetViewModel_TAG: applyAction: ")
        loading.postValue(true)
        if (!isUpdate)
            registerPet()
        else
            updatePet()
    }

    private fun registerPet() {
        Timber.d("RegisterPetViewModel_TAG: registerPet: ")
        petsRepository.registerPet(
            mapPet(),
            onSuccess = {
                successOperation.postValue(true)
                loading.postValue(false)
            },
            onFailure = {
                successOperation.postValue(false)
                loading.postValue(false)
            }
        )
    }

    private fun updatePet() {
        Timber.d("RegisterPetViewModel_TAG: updatePet: ")
        petsRepository.updatePet(
            mapPet(),
            documentId,
            onSuccess = {
                successOperation.postValue(true)
                loading.postValue(false)
            },
            onFailure = {
                successOperation.postValue(false)
                loading.postValue(false)
            }
        )
    }

    private fun mapPet(): HashMap<String, Any?> {
        Timber.d("RegisterPetViewModel_TAG: mapPet: ")
        return hashMapOf(
            "name" to (name.value ?: ""),
            "gender" to (gender.value ?: ""),
            "species" to (species.value ?: ""),
            "birthdate" to birthdate.value,
            "breed" to breed.value,
            "color" to (color.value ?: ""),
            "userId" to userId
        )
    }

    fun setPetData(pet: PetModel) {
        Timber.d("RegisterPetViewModel_TAG: setPetData: ")
        name.postValue(pet.name)
        gender.postValue(pet.gender)
        species.postValue(pet.species)
        birthdate.postValue(pet.birthdate)
        breed.postValue(pet.breed)
        color.postValue(pet.color)
    }
}