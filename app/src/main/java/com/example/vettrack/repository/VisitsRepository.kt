package com.example.vettrack.repository

import com.example.vettrack.models.VisitModel
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class VisitsRepository(private val firestore: FirebaseFirestore) {

    companion object {
        private const val VET_VISITS_COLLECTION = "vetVisits"
    }

    fun registerVisit(
        visit: HashMap<String, Any?>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Timber.d("FirestoreRepository_TAG: registerVisit: visit: $visit ")
        firestore.collection(VET_VISITS_COLLECTION)
            .add(visit)
            .addOnSuccessListener {
                Timber.d("FirestoreRepository_TAG: registerVisit: SUCCESS: ${it.id} ")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.d("FirestoreRepository_TAG: registerVisit: ERROR: ${e.message} ")
                onFailure()
            }
    }

    fun getVisits(userId: String, onSuccess: (List<VisitModel>) -> Unit, onFailure: () -> Unit) {
        Timber.d("FirestoreRepository_TAG: getVisits: ")
        firestore.collection(VET_VISITS_COLLECTION)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                Timber.d("FirestoreRepository_TAG: getVisits: success: ${result.size()} ")
                val vetVisits = result.documents.mapNotNull { document ->
                    document.toObject(VisitModel::class.java)?.copy(id = document.id)
                }
                onSuccess(vetVisits)
            }
            .addOnFailureListener { e ->
                Timber.d("FirestoreRepository_TAG: getVisits: ERROR: ${e.message} ")
                onFailure()
            }
    }

    fun getVisitById(id: String, onSuccess: (VisitModel?) -> Unit, onFailure: () -> Unit) {
        Timber.d("FirestoreRepository_TAG: getVisitById: ID: $id ")
        firestore.collection(VET_VISITS_COLLECTION).document(id).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val vetVisit = document.toObject(VisitModel::class.java)
                    onSuccess(vetVisit)
                } else {
                    Timber.d("FirestoreRepository_TAG: getVisitById: ERROR: Document not found ")
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                Timber.d("FirestoreRepository_TAG: getVisitById: ERROR: ${exception.message} ")
                onFailure()
            }
    }

    fun updateVisit(
        documentId: String,
        visit: HashMap<String, Any?>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Timber.d("FirestoreRepository_TAG: updateVisit: visit: $visit ")
        firestore.collection(VET_VISITS_COLLECTION)
            .document(documentId)
            .update(visit)
            .addOnSuccessListener {
                Timber.d("FirestoreRepository_TAG: updateVisit: SUCCESS ")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.d("FirestoreRepository_TAG: updateVisit: ERROR: ${e.message} ")
                onFailure()
            }
    }

    fun deleteVisit(
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Timber.d("FirestoreRepository_TAG: deleteVisit: visit $documentId")
        firestore.collection(VET_VISITS_COLLECTION)
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                Timber.d("FirestoreRepository_TAG: deleteVisit: SUCCESS ")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Timber.d("FirestoreRepository_TAG: deleteVisit: ERROR: ${e.message} ")
                onFailure()
            }
    }
}