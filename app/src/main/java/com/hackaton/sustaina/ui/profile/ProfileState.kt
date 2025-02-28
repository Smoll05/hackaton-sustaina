package com.hackaton.sustaina.ui.profile

data class User(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val joinedCampaigns: List<String>,
    val level: Int,
    val currentExp: Int,
    val profilePicUrl: String? = null,
    val gallery: List<String>
)
