package com.example.vettrack.models

data class ScheduleVisitModel(
    var id: String = "",
    val date: String = "",
    val petName: String = "",
    val clinicName: String = "",
    val userId: String = ""
)