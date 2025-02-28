package com.hackaton.sustaina.data.user

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.hackaton.sustaina.domain.models.User
import javax.inject.Inject

class UserDataSource @Inject constructor(
    database: DatabaseReference
){
    private val usersRef = database.child("user")

    fun addUser(user: User, onComplete: (Boolean, String?) -> Unit) {
        usersRef.child(user.userId).setValue(user)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    fun getUser(userId: String, onComplete: (User?) -> Unit) {
        Log.d("USER DETAILS", userId)
        usersRef.child(userId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    Log.d("USER DETAILS", "User found: $user")
                    onComplete(user)
                } else {
                    Log.d("USER DETAILS", "User not found in database")
                    onComplete(null)
                }

                val user = snapshot.getValue(User::class.java)
                Log.d("USER DETAILS", user.toString())
                onComplete(user)
            }
            .addOnFailureListener { onComplete(null) }
    }
}

