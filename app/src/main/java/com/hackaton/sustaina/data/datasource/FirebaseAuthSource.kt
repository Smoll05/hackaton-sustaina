package com.hackaton.sustaina.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(
    private val auth : FirebaseAuth
){
    fun getCurrentUser() : FirebaseUser? {
        return auth.currentUser
    }

    suspend fun registerUser(email: String, password: String) : Result<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String) : Result<FirebaseUser?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    fun hasUser(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }
}