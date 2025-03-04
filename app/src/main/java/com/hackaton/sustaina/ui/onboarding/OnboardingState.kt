package com.hackaton.sustaina.ui.onboarding

sealed class OnboardingState {
    data object Idle : OnboardingState()
    data object Loading : OnboardingState()
    data object Success : OnboardingState()
    data object UserExists : OnboardingState()
    data class Error(val message: String) : OnboardingState()
}