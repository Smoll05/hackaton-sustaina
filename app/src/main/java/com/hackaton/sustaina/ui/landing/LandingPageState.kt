package com.hackaton.sustaina.ui.landing

import com.hackaton.sustaina.domain.models.Campaign

data class LandingPageState (
    // TODO: user-related data should be retrieved from repo but is yet to be defined
    val level: Int = 23,
    val exp: Int = 420,
    val expToNextLvl: Int = 1000,
    val progress: Float = (exp.toFloat() / expToNextLvl),

    val upcomingCampaigns: List<Campaign> = listOf()
    )
