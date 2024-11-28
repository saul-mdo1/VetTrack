package com.example.vettrack.models

data class PetModel(
    val id: Int,
    val species: String,
    val genre: String,
    val name: String,
    val birthdate: String,
    val breed: String,
    val color: String
)