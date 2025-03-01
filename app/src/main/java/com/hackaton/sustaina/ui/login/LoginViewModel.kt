package com.hackaton.sustaina.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
            _loginState.update { LoginState.Loading }
            val startTime = System.currentTimeMillis()

            val result = authRepository.login(email, password)

            val elapsedTime = System.currentTimeMillis() - startTime
            val remainingTime = 1000L - elapsedTime

            if (remainingTime > 0) {
                delay(remainingTime)
            }
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