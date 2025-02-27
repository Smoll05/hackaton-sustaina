package com.hackaton.sustaina.ui.register

import com.google.firebase.auth.FirebaseUser

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: FirebaseUser?) : RegisterState()
    data class Error(val message: String) : RegisterState()
}