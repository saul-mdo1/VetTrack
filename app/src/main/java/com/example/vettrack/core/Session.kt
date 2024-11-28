package com.example.vettrack.core

import com.google.firebase.auth.FirebaseUser

object Session {
    var user: FirebaseUser? = null
    var deviceId: String? = null
}