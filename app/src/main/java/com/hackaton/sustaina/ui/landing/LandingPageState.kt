package com.hackaton.sustaina.ui.landing

import com.hackaton.sustaina.domain.models.Campaign
import com.hackaton.sustaina.domain.models.User

data class LandingPageState (
    val user: User = User(),
    val progress: Float = 0f,
    val upcomingCampaigns: List<Campaign> = listOf()
)
