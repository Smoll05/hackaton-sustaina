package com.hackaton.sustaina.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.auth.AuthRepository
import com.hackaton.sustaina.data.user.UserRepository
import com.hackaton.sustaina.ui.campaigninfo.CampaignInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val userRepository: UserRepository
) : ViewModel() {
    val user = authRepository.getCurrentUser()

    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState = _logoutState.asStateFlow()

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (user != null) {
                Log.d("ProfileViewModel", "Fetching data for ${user.displayName} (${user.uid})")

                userRepository.fetchUser(user.uid) { userData ->
                    if (userData != null) {
                        _uiState.value = ProfileState(
                            userId = userData.userId,
                            userName = userData.userName,
                            email = userData.userEmail,
                            joinedCampaigns = userData.userUpcomingCampaigns,
                            level = userData.userLevel,
                            currentExp = userData.userExp,
                            loading = false
                        )
                    } else {
                        viewModelScope.launch {
                            delay(3000)
                            _uiState.update { it.copy(loading = false) }
                        }
                    }
                }
            } else {
                delay(3000)
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = LogoutState.Loading

            authRepository.logout()
            _logoutState.value = LogoutState.Success

            Log.d("ACCOUNT SIGNED OUT", "Successful")
        }
    }

    sealed class LogoutState {
        data object Idle : LogoutState()
        data object Loading : LogoutState()
        data object Success : LogoutState()
    }
}