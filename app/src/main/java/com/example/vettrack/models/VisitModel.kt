package com.example.vettrack.models

data class VisitModel(
    var id: String = "",
    val date: String = "",
    val reason: String = "",
    val petName: String = "",
    val dogWeight: String = "",
    val clinicName: String = "",
    val city: String? = "",
    val totalPaid: String = "",
    val observations: String? = "",
    val userId: String = "",
    val pending: Boolean = false
)