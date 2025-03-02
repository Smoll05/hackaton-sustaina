package com.hackaton.sustaina.domain.usecases

import com.hackaton.sustaina.domain.models.Hotspot

class GetHotspotUseCase {
    // TODO( Remove this; Sample data )
    private val reports = listOf(
        Hotspot(
            hotspotId = "report_123",
            latitude = 10.294359,
            longitude = 123.881416,
            imageUrl = "https://example.com/garbage1.jpg",
            description = "CIT-U Overflowing trash bins near park",
            statusLevel = 0, // Uncleaned = 0
            timestamp = 1709056800,
            reportedByUserId = "user_456",
            densityLevel = 1,
            radius = 20.0
        ),
        Hotspot(
            hotspotId = "report_124",
            latitude = 10.292175,
            longitude = 123.882855,
            imageUrl = "https://example.com/garbage2.jpg",
            description = "C.Padilla Piles of plastic waste near riverbank",
            statusLevel = 0, // Uncleaned = 0
            timestamp = 1709057000,
            reportedByUserId = "user_789",
            densityLevel = 2,
            radius = 20.0
        ),
        Hotspot(
            hotspotId = "report_125",
            latitude = 10.297658,
            longitude = 123.882134,
            imageUrl = "https://example.com/garbage3.jpg",
            description = "TRES DE ABRIL Scattered debris after market day",
            statusLevel = 1, // In Progress = 1
            timestamp = 1709057200,
            reportedByUserId = "user_321",
            densityLevel = 3,
            radius = 20.0
        ),
        Hotspot(
            hotspotId = "report_126",
            latitude = 10.297407,
            longitude = 123.896274,
            imageUrl = "https://example.com/garbage4.jpg",
            description = "UC MAIN Abandoned garbage bags on sidewalk",
            statusLevel = 2, // Cleaned = 2
            timestamp = 1709057400,
            reportedByUserId = "user_654",
            densityLevel = 3,
            radius = 20.0
        ),
        Hotspot(
            hotspotId = "report_127",
            latitude = 10.258640,
            longitude = 123.823828,
            imageUrl = "https://example.com/garbage5.jpg",
            description = "STAR MALL Improperly disposed food waste",
            statusLevel = 0, // Uncleaned = 0
            timestamp = 1709057600,
            reportedByUserId = "user_987",
            densityLevel = 2,
            radius = 20.0
        ),
        Hotspot(
            hotspotId = "report_128",
            latitude = 10.294067,
            longitude = 123.882511,
            imageUrl = "https://example.com/garbage5.jpg",
            description = "FATIMA Hotspot",
            statusLevel = 0, // Uncleaned = 0
            timestamp = 1709057600,
            reportedByUserId = "user_987",
            densityLevel = 2,
            radius = 20.0
        ),
        Hotspot(
            hotspotId = "report_128", // Note: Duplicate ID, might want to change this
            latitude = 37.4220,
            longitude = -122.0841,
            imageUrl = "https://example.com/garbage5.jpg",
            description = "Google HQ Hotspot",
            statusLevel = 0, // Uncleaned = 0
            timestamp = 1709057600,
            reportedByUserId = "user_987",
            densityLevel = 2,
            radius = 20.0
        )
    )

    fun allHotspots(): List<Hotspot> = reports

    fun reportsByStatus(status: Int): List<Hotspot> =
        reports.filter { it.statusLevel == status }

    fun reportsByUser(userId: String): List<Hotspot> =
        reports.filter { it.reportedByUserId == userId }

    fun reportsInArea(latMin: Double, latMax: Double, lonMin: Double, lonMax: Double): List<Hotspot> =
        reports.filter { it.latitude in latMin..latMax && it.longitude in lonMin..lonMax }

    fun latestReports(since: Long): List<Hotspot> =
        reports.filter { it.timestamp >= since }
}