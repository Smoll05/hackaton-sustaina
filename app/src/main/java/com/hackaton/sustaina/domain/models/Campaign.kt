package com.hackaton.sustaina.domain.models

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class Campaign(
    val campaignId: String = "",
    val campaignAttendingUser: List<String> = listOf(),
    val campaignName: String = "",
    val campaignOrganizer: String = "",
    val campaignStartDate: Long = Instant.now().toEpochMilli(),
    val campaignAbout: String = "",
    val campaignVenue: String = "",
    val campaignAddress: String = ""
)

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}