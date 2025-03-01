package com.hackaton.sustaina.ui.profile

data class ProfileState(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val joinedCampaigns: List<String> = emptyList(),
    val level: Int = 0,
    val currentExp: Int = 0,
    val profilePicUrl: String? = null,
    val gallery: List<String> = emptyList(),
    val loading: Boolean = true
)
