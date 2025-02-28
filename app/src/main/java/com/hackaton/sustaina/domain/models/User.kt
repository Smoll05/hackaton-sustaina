package com.hackaton.sustaina.domain.models

data class User(
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userUpcomingCampaigns: List<String> = emptyList(),
    val userLevel: Int = 0,
    val userExp: Int = 0
)