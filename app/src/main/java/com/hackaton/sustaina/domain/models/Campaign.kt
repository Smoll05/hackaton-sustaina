package com.hackaton.sustaina.domain.models

import java.time.LocalDateTime

data class Campaign(
    val campaignId: String = "",
    val campaignAttendingUser: List<String> = listOf(),
    val campaignName: String = "",
    val campaignOrganizer: String = "",
    val campaignStartDate: LocalDateTime? = null,
    val campaignAbout: String = "",
    val campaignVenue: String = "",
    val campaignAddress: String = ""
)