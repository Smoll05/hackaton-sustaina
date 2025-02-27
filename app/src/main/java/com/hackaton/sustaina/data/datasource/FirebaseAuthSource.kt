package com.hackaton.sustaina.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthSource {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() : FirebaseUser? {
        return auth.currentUser
    }

    suspend fun registerUser(email: String, password: String) : FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun loginUser(email: String, password: String) : FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        auth.signOut()
    }
}