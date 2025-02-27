package com.hackaton.sustaina.data.repository

import com.google.firebase.auth.FirebaseUser
import com.hackaton.sustaina.data.datasource.FirebaseAuthSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuthSource: FirebaseAuthSource
) {
    fun getCurrentUser() : FirebaseUser? = firebaseAuthSource.getCurrentUser()

    suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthSource.loginUser(email, password)
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser?> {
        return firebaseAuthSource.registerUser(email, password)
    }

    val currentUserId: String
        get() = firebaseAuthSource.currentUserId

    fun hasUser(): Boolean {
        return firebaseAuthSource.hasUser()
    }

    fun logout() {
        firebaseAuthSource.logout()
    }
}