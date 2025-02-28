package com.hackaton.sustaina.ui.map

data class SustainaCampaign(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val description: String,
    val timestamp: Long,
    val type: String
)

class SustainaCampaigns {
    // TODO( Remove this; Sample data )
    private val events = listOf(
        SustainaCampaign("event_001", 10.266363, 123.877388, "IL CORSO Clean-up Drive in Rizal Park", "Join us in cleaning up!", 1709056800, "campaign"),
        SustainaCampaign("event_002", 10.282110, 123.883115, "SEASIDE Tree Planting Activity", "Help plant trees in our community!", 1709060000, "environment"),
        SustainaCampaign("event_003", 10.290376, 123.900082, "CSCR TUNNEL Recycling Workshop", "Learn how to recycle properly.", 1709063200, "education"),
        SustainaCampaign("event_004", 10.293207, 123.905030, "FORT SAN PEDRO Beach Clean-up", "Volunteers needed for beach cleanup.", 1709066400, "campaign"),
        SustainaCampaign("event_005", 10.299741, 123.896808, "STO NIÑO PARISH Community Composting", "Join us in sustainable waste management.", 1709069600, "sustainability")
    )

    fun allCampaigns(): List<SustainaCampaign> = events

    fun campaignsByType(type: String): List<SustainaCampaign> =
        events.filter { it.type.equals(type, ignoreCase = true) }

    fun campaignsInArea(latMin: Double, latMax: Double, lonMin: Double, lonMax: Double): List<SustainaCampaign> =
        events.filter { it.latitude in latMin..latMax && it.longitude in lonMin..lonMax }

    fun campaignsEvents(since: Long): List<SustainaCampaign> =
        events.filter { it.timestamp >= since }
}