package com.example.vettrack.presentation.authentication.signup

import androidx.lifecycle.MutableLiveData
import com.example.vettrack.R
import com.example.vettrack.presentation.base.BaseAuthViewModel
import com.example.vettrack.repository.AuthRepository
import timber.log.Timber

class SignUpViewModel(private val authRepository: AuthRepository) : BaseAuthViewModel() {
    val successRegister = MutableLiveData(false)
    val errorRegister = MutableLiveData(false)

    val iconResource = MutableLiveData(R.drawable.ic_success)
    val iconVisible = MutableLiveData(false)
    val cancelEnabled = MutableLiveData(true)

    fun createUser() {
        Timber.d("SignUpViewModel_TAG: createUser: ")
        resetDesign()

        authRepository.createUser(
            email = email.value.toString(),
            password = password.value.toString(),
            onSuccess = {
                Timber.d("SignUpViewModel_TAG: createUser: SUCCESS")
                successRegister.postValue(true)
                iconResource.postValue(R.drawable.ic_success)
                iconVisible.postValue(true)
                cancelEnabled.postValue(false)
                buttonEnabled.postValue(false)
            },
            onError = {
                Timber.d("SignUpViewModel_TAG: createUser: ERROR $it")
                errorRegister.postValue(true)
                iconResource.postValue(R.drawable.ic_error)
                iconVisible.postValue(true)
                cancelEnabled.postValue(true)
            }
        )
    }

    private fun resetDesign() {
        Timber.d("SignUpViewModel_TAG: resetDesign: ")
        iconVisible.postValue(false)
        successRegister.postValue(false)
        errorRegister.postValue(false)
    }
}