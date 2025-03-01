package com.hackaton.sustaina.domain.usecases

import com.hackaton.sustaina.domain.models.Campaign

class GetCampaignUseCase {
    private val campaigns = listOf(
        Campaign(
            campaignId = "event_001",
            campaignAttendingUser = listOf("user_456", "user_789"), // Sample attending users
            campaignName = "IL CORSO Clean-up Drive in Rizal Park",
            campaignOrganizerId = "org_001",
            campaignOrganizer = "Green Earth Society",
            campaignStartDate = 1709056800,
            campaignDescription = "Join us in cleaning up!",
            campaignVenue = "Rizal Park",
            campaignAddress = "IL Corso, 10.266363, 123.877388",
            10.266363,
            123.877388
        ),
        Campaign(
            campaignId = "event_002",
            campaignAttendingUser = listOf("user_321", "user_654"), // Sample attending users
            campaignName = "SEASIDE Tree Planting Activity",
            campaignOrganizerId = "org_002",
            campaignOrganizer = "Tree Huggers Inc.",
            campaignStartDate = 1709060000,
            campaignDescription = "Help plant trees in our community!",
            campaignVenue = "Seaside Community Grounds",
            campaignAddress = "Seaside, 10.282110, 123.883115",
            10.282110,
            123.883115
        ),
        Campaign(
            campaignId = "event_003",
            campaignAttendingUser = listOf("user_987", "user_123"), // Sample attending users
            campaignName = "CSCR TUNNEL Recycling Workshop",
            campaignOrganizerId = "org_003",
            campaignOrganizer = "Recycle Now Foundation",
            campaignStartDate = 1709063200,
            campaignDescription = "Learn how to recycle properly.",
            campaignVenue = "CSCR Tunnel Community Center",
            campaignAddress = "CSCR Tunnel, 10.290376, 123.900082",
            10.290376,
            123.900082
        ),
        Campaign(
            campaignId = "event_004",
            campaignAttendingUser = listOf("user_456", "user_321"), // Sample attending users
            campaignName = "FORT SAN PEDRO Beach Clean-up",
            campaignOrganizerId = "org_004",
            campaignOrganizer = "Ocean Savers Group",
            campaignStartDate = 1709066400,
            campaignDescription = "Volunteers needed for beach cleanup.",
            campaignVenue = "Fort San Pedro Beach",
            campaignAddress = "Fort San Pedro, 10.293207, 123.905030",
            10.293207,
            123.905030
        ),
        Campaign(
            campaignId = "event_005",
            campaignAttendingUser = listOf("user_654", "user_987"), // Sample attending users
            campaignName = "STO NIÑO PARISH Community Composting",
            campaignOrganizerId = "org_005",
            campaignOrganizer = "Sustainable Living Collective",
            campaignStartDate = 1709069600,
            campaignDescription = "Join us in sustainable waste management.",
            campaignVenue = "Sto Niño Parish Grounds",
            campaignAddress = "Sto Niño Parish, 10.299741, 123.896808",
            10.299741,
            123.896808
        ),
        Campaign(
            campaignId = "event_006", // Changed duplicate ID from "event_005" to "event_006"
            campaignAttendingUser = listOf("user_789", "user_123"), // Sample attending users
            campaignName = "FATIMA Hotspot",
            campaignOrganizerId = "org_005",
            campaignOrganizer = "Sustainable Living Collective",
            campaignStartDate = 1709069600,
            campaignDescription = "Join us in sustainable waste management.",
            campaignVenue = "Fatima Community Area",
            campaignAddress = "Fatima, 10.294067, 123.882511",
            10.294067,
            123.882511,
        )
    )
    fun allCampaigns(): List<Campaign> = campaigns

    fun campaignsInArea(latMin: Double, latMax: Double, lonMin: Double, lonMax: Double): List<Campaign> =
        campaigns.filter { it.latitude in latMin..latMax && it.longitude in lonMin..lonMax }

    fun campaignsEvents(since: Long): List<Campaign> =
        campaigns.filter { it.campaignStartDate >= since }
}