package com.example.vettrack.presentation.pets.list

import com.example.vettrack.models.PetModel

class PetItemViewModel {
    var pet: PetModel? = null

    val id: String?
        get() = pet?.id
    val name: String
        get() = pet?.name ?: "---"
}