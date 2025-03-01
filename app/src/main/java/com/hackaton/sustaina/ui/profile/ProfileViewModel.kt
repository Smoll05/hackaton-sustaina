package com.hackaton.sustaina.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState = _logoutState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = LogoutState.Loading
            authRepository.logout()
            Log.d("ACCOUNT SIGNED OUT", "Successful")
            _logoutState.value = LogoutState.Success
        }
    }

    sealed class LogoutState {
        data object Idle : LogoutState()
        data object Loading : LogoutState()
        data object Success : LogoutState()
    }
}
