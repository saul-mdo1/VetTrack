package com.example.vettrack.repository

import com.example.vettrack.models.PetModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class PetsRepository(private val firestore: FirebaseFirestore) {
    companion object {
        private const val PETS_COLLECTION = "pets"
        private const val USER_ID_FIELD = "userId"
    }

    fun registerPet(
        pet: HashMap<String, Any?>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Timber.d("PetsRepository_TAG: registerPet: ")
        firestore.collection(PETS_COLLECTION)
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

    fun getPets(userId: String, onSuccess: (List<PetModel>) -> Unit, onFailure: () -> Unit) {
        Timber.d("PetsRepository_TAG: getVisits: ")
        firestore.collection(PETS_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .get()
            .addOnSuccessListener { result ->
                Timber.d("PetsRepository_TAG: getPets: success: ${result.size()} ")
                val pets = result.documents.mapNotNull { document ->
                    document.toObject(PetModel::class.java)?.copy(id = document.id)
                }
                onSuccess(pets)
            }
            .addOnFailureListener { e ->
                Timber.d("PetsRepository_TAG: getPets: ERROR: ${e.message} ")
                onFailure()
            }
    }
}