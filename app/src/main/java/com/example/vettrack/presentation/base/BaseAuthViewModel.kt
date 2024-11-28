package com.example.vettrack.presentation.base

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

open class BaseAuthViewModel : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    //region Data Validators
    val buttonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(email) { validateForm() }
        addSource(password) { validateForm() }
    }

    private fun validateForm() {
        Timber.d("SignUpViewModel_TAG: validateForm: ")
        val isValidEmail = email.value?.let {
            it.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(it).matches()
        } ?: true

        val isPasswordValid = password.value?.let {
            it.isNotBlank() && it.length >= 6
        } ?: true

        buttonEnabled.value = isValidEmail && isPasswordValid
    }
    //endregion
}