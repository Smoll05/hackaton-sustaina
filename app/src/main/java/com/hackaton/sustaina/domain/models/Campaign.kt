package com.hackaton.sustaina.domain.models

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class Campaign(
    val campaignId: String = "",
    val campaignAttendingUser: List<String> = listOf(),
    val campaignName: String = "",
    val campaignOrganizerId: String = "",
    val campaignOrganizer: String = "",
    val campaignStartDate: Long = Instant.now().toEpochMilli(),
    val campaignDescription: String = "",
    val campaignVenue: String = "",
    val campaignAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}