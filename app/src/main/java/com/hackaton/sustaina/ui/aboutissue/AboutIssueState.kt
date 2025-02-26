package com.hackaton.sustaina.ui.aboutissue

import java.time.LocalDateTime

data class CampaignState(
    val campaignId: String = "",
    val campaignName: String = "",
    val campaignOrganizer: String = "",
    val campaignStartDate: LocalDateTime? = null,
    val campaignAbout: String = "",
    val campaignVenue: String = "",
    val campaignAddress: String = ""
    )