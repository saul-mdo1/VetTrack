package com.example.vettrack.repository

import com.example.vettrack.models.PetModel
import com.example.vettrack.models.VisitModel
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

    fun updatePet(
        pet: HashMap<String, Any?>,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Timber.d("PetsRepository_TAG: updatePet: ID $documentId ")
        firestore.collection(PETS_COLLECTION)
            .document(documentId)
            .update(pet)
            .addOnSuccessListener {
                Timber.d("PetsRepository_TAG: registerPet:SUCCESS:")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.d("PetsRepository_TAG: registerPet: ${e.message}")
                onFailure()
            }
    }

    fun getPetById(id: String, onSuccess: (PetModel?) -> Unit, onFailure: () -> Unit) {
        Timber.d("PetsRepository_TAG: getPetById: ID $id")
        firestore.collection(PETS_COLLECTION).document(id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val vetVisit = document.toObject(PetModel::class.java)
                    onSuccess(vetVisit)
                } else {
                    Timber.d("PetsRepository_TAG: getPetById: ERROR: Document not found ")
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                Timber.d("PetsRepository_TAG: getPetById: ERROR: ${exception.message} ")
                onFailure()
            }
    }

    fun deletePet(
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Timber.d("PetsRepository_TAG: deleteVisit: visit $documentId")
        firestore.collection(PETS_COLLECTION)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                Timber.d("PetsRepository_TAG: deletePet: SUCCESS")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.d("PetsRepository_TAG: deletePet: ERROR: ${e.message} ")
                onFailure()
            }
    }
}