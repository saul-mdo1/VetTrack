package com.example.vettrack.presentation.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class SplashViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val userLogged = MutableLiveData<FirebaseUser?>()

    fun getLoggedUser() {
        Timber.d("LoginViewModel_TAG: getLoggedUser: ")
        authRepository.getCurrentUser(
            onUserLogged = {
                userLogged.postValue(it)
            },
            onNotUserFound = {
                userLogged.postValue(null)
            }
        )
    }

}