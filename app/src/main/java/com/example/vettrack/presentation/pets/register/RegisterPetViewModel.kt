package com.example.vettrack.presentation.pets.register

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.R
import com.example.vettrack.repository.PetsRepository
import timber.log.Timber

class RegisterPetViewModel(private val petsRepository: PetsRepository) : ViewModel() {
    val loading = MutableLiveData(false)
    val successOperation = MutableLiveData<Boolean>()

    val name = MutableLiveData("")
    val gender = MutableLiveData(-1)
    val species = MutableLiveData("")
    val birthdate = MutableLiveData("")
    val breed = MutableLiveData("")
    val color = MutableLiveData("")
    var userId = ""

    var isUpdate = false
    val buttonText = MutableLiveData(R.string.txt_register)

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

}