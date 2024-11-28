package com.example.vettrack.presentation.authentication.login

import androidx.lifecycle.MutableLiveData
import com.example.vettrack.presentation.base.BaseAuthViewModel
import com.example.vettrack.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class LoginViewModel(private val authRepository: AuthRepository) : BaseAuthViewModel() {
    val successLogin = MutableLiveData(false)
    val errorLogin = MutableLiveData(false)

    fun login() {
        Timber.d("LoginViewModel_TAG: login: ")
        errorLogin.postValue(false)

        authRepository.login(
            email = email.value.toString(),
            password = password.value.toString(),
            onSuccess = {
                successLogin.postValue(true)
                errorLogin.postValue(false)
            },
            onError = {
                Timber.d("LoginViewModel_TAG: login: error $it")
                errorLogin.postValue(true)
                successLogin.postValue(false)
            }
        )
    }
}