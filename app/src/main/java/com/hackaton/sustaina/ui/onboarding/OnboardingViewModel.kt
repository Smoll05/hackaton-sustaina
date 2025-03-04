package com.hackaton.sustaina.ui.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.auth.AuthRepository
import com.hackaton.sustaina.data.user.UserRepository
import com.hackaton.sustaina.domain.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val TAG = "OnboardingViewModel"

    private val _uiState = MutableStateFlow<OnboardingState>(OnboardingState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        Log.wtf(TAG, "OnboardingViewModel initialized")
    }

    fun checkUserStatus(onComplete: () -> Unit) {
        val user = authRepository.getCurrentUser() ?: return
        var hasResumed = false

        viewModelScope.launch {
            val userExists = suspendCoroutine { continuation ->
                userRepository.fetchUser(user.uid) { userStatus ->
                    if (!hasResumed) {
                        hasResumed = true
                        continuation.resume(userStatus != null)
                    }
                }
            }

            if (userExists) {
                Log.wtf(TAG, "User already exists!")
                _uiState.value = OnboardingState.UserExists
                onComplete()
            } else {
                Log.wtf(TAG, "User does not exist in database.")
                _uiState.value = OnboardingState.Idle
            }
        }
    }


    fun createUser(userName: String) {
        val user = authRepository.getCurrentUser()

        _uiState.value = OnboardingState.Loading

        if (user != null) {
            user.email?.let { User(userId = user.uid, userName = userName, userEmail = it) }?.let {
                userRepository.registerUser(it) { success, error ->
                    if (success) {
                        Log.wtf(TAG, "User created successfully!")
                        _uiState.value = OnboardingState.Success
                    } else {
                        Log.wtf(TAG, "Error creating user: $error")
                        _uiState.value = OnboardingState.Error(error.toString())
                    }
                }
            }
        }
    }
}