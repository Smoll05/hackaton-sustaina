package com.hackaton.sustaina.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.repository.AuthRepository
import com.hackaton.sustaina.ui.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val result = authRepository.register(email, password)
            result.onSuccess { user ->
                _registerState.value = RegisterState.Success(user)
                Log.d("ACCOUNT SIGNED", "Successful")
            }.onFailure { exception ->
                _registerState.value = RegisterState.Error(exception.message ?: "Register failed")
                Log.d("ACCOUNT SIGNED", "Failed")
            }
        }
    }
}