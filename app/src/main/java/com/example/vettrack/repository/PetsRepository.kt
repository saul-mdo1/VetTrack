package com.example.vettrack.repository

import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class PetsRepository(private val firestore: FirebaseFirestore) {
    companion object {
        private const val PETs_COLLECTION = "pets"
    }

    fun registerPet(
        pet: HashMap<String, Any?>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Timber.d("PetsRepository_TAG: registerPet: ")
        firestore.collection(PETs_COLLECTION)
            .add(pet)
            .addOnSuccessListener {
                Timber.d("PetsRepository_TAG: registerPet:SUCCESS: ${it.id}")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.d("PetsRepository_TAG: registerPet: ${e.message}")
                onFailure()
            }
    }
}