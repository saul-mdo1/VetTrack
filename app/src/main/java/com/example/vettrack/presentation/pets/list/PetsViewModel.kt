package com.example.vettrack.presentation.pets.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vettrack.repository.VisitsRepository

class PetsViewModel(private val visitsRepository: VisitsRepository) : ViewModel() {
    val loading = MutableLiveData(false)

    val petsNum = MutableLiveData("0")
    val searchQuery = MutableLiveData<String?>()
}