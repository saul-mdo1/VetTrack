package com.example.vettrack.models

data class PetModel(
    var id: String = "",
    val name: String = "",
    val gender: Int = 0,
    val species: String = "",
    val birthdate: String? = "",
    val breed: String? = "",
    val color: String = ""
)