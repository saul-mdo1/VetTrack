package com.example.vettrack.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.repository.AuthRepository
import timber.log.Timber

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val signOutSuccess = MutableLiveData<Boolean>()

    fun signOut() {
        Timber.d("MainViewModel_TAG: signOut: ")
        authRepository.signOut {
            signOutSuccess.postValue(true)
        }
    }
}