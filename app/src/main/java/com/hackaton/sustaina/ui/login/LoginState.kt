package com.hackaton.sustaina.ui.login

import com.google.firebase.auth.FirebaseUser

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: FirebaseUser?) : LoginState()
    data class Error(val message: String) : LoginState()
}