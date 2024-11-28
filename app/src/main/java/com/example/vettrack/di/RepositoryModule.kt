package com.example.vettrack.di

import com.example.vettrack.repository.AuthRepository
import com.example.vettrack.repository.VisitsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

fun createAuthFirebase() = FirebaseAuth.getInstance()
fun createFirestore() = FirebaseFirestore.getInstance()

val authRepositoryModule = module {
    single { createAuthFirebase() }
    single { AuthRepository(get()) }
}

val visitsRepositoryModule = module {
    single { createFirestore() }
    single { VisitsRepository(get()) }
}

val repositoryModule = listOf(
    authRepositoryModule,
    visitsRepositoryModule
)
