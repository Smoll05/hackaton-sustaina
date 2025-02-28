package com.hackaton.sustaina.data.auth

import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: AuthDataSource
) {
    fun getCurrentUser() : FirebaseUser? = authDataSource.getCurrentUser()

    suspend fun login(email: String, password: String): Result<FirebaseUser?> {
        return authDataSource.loginUser(email, password)
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser?> {
        return authDataSource.registerUser(email, password)
    }

    val currentUserId: String
        get() = authDataSource.currentUserId

    fun hasUser(): Boolean {
        return authDataSource.hasUser()
    }

    fun logout() {
        authDataSource.logout()
    }
}