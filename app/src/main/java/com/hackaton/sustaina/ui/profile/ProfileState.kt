package com.hackaton.sustaina.ui.profile

import kotlin.random.Random

data class ProfileState(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val joinedCampaigns: List<String> = emptyList(),
    val level: Int = 0,
    val currentExp: Int = 0,
    val profilePicUrl: String? = getRandomProfilePicture(),
    val gallery: List<String> = emptyList(),
    val loading: Boolean = true
) {
    companion object {
        private fun getRandomProfilePicture(): String {
            val profileNumber = Random.nextInt(1, 6)
            return "profile_$profileNumber"
        }
    }
}