package com.hackaton.sustaina.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authRepository.login(email, password)
            result.onSuccess { user ->
                _loginState.value = LoginState.Success(user)
                Log.d("ACCOUNT SIGNED", "Successful")
            }.onFailure { exception ->
                _loginState.value = LoginState.Error(exception.message ?: "Login failed")
                Log.d("ACCOUNT SIGNED", "Failed")
            }
        }
    }
}