package com.example.vettrack.repository

import com.example.vettrack.core.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class AuthRepository(private val firebaseAuth: FirebaseAuth) {
    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        Timber.d("AuthRepository_TAG: login: ")
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    Session.user = task.result.user
                } else {
                    val error = task.exception?.message
                    onError("Login failed, error: $error")
                }
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Unknown error")
            }
    }

    fun createUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Timber.d("AuthRepository_TAG: createUser: ")
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val error = task.exception?.message
                    onError("Create user failed, error: $error")
                }
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Unknown error")
            }
    }

    fun getCurrentUser(
        onUserLogged: (FirebaseUser) -> Unit,
        onNotUserFound: () -> Unit
    ) {
        Timber.d("AuthRepository_TAG: getCurrentUser: ")
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            onUserLogged(currentUser)
            Session.user = currentUser
        } else {
            onNotUserFound()
            Session.user = null
        }
    }

    fun signOut(onSignOut: () -> Unit) {
        Timber.d("AuthRepository_TAG: signOut: ")
        firebaseAuth.signOut()
        onSignOut()
    }
}