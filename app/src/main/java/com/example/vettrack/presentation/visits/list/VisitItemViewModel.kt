package com.example.vettrack.presentation.visits.list

import com.example.vettrack.models.VisitModel

class VisitItemViewModel {
    var visit: VisitModel? = null

    val id: String?
        get() = visit?.id

    val date: String
        get() = visit?.date ?: "---"

    val petName: String
        get() = visit?.petName ?: "---"

    val clinic: String
        get() = visit?.clinicName ?: "---"

    val reason: String
        get() = visit?.reason ?: "---"

    val enableComplete: Boolean
        get() = (visit?.pending == false && visit?.reason.isNullOrEmpty()) || visit?.pending == true
}