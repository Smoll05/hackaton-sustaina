package com.hackaton.sustaina.ui.map

data class UserReport(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val description: String,
    val status: String,
    val timestamp: Long,
    val userId: String,
    val density: Int
)

class SustainaUserReports {
    // TODO( Remove this; Sample data )
    private val reports = listOf(
        UserReport("report_123", 10.294359, 123.881416, "https://example.com/garbage1.jpg", "CIT-U Overflowing trash bins near park", "Uncleaned", 1709056800, "user_456",1),
        UserReport("report_124", 10.292175, 123.882855, "https://example.com/garbage2.jpg", "C.Padilla Piles of plastic waste near riverbank", "Uncleaned", 1709057000, "user_789",2),
        UserReport("report_125", 10.297658, 123.882134, "https://example.com/garbage3.jpg", "TRES DE ABRIL Scattered debris after market day", "In Progress", 1709057200, "user_321",3),
        UserReport("report_126", 10.297407, 123.896274, "https://example.com/garbage4.jpg", "UC MAIN Abandoned garbage bags on sidewalk", "Cleaned", 1709057400, "user_654",3),
        UserReport("report_127", 10.258640,  123.823828, "https://example.com/garbage5.jpg", "STAR MALL Improperly disposed food waste", "Uncleaned", 1709057600, "user_987",2),
        UserReport("report_128", 10.294067, 123.882511, "https://example.com/garbage5.jpg", "FATIMA Hotspot", "Uncleaned", 1709057600, "user_987",2),
        UserReport("report_128", 37.4220, -122.0841, "https://example.com/garbage5.jpg", "FATIMA Hotspot", "Uncleaned", 1709057600, "user_987",2),
    )

    fun allReports(): List<UserReport> = reports

    fun reportsByStatus(status: String): List<UserReport> =
        reports.filter { it.status.equals(status, ignoreCase = true) }

    fun reportsByUser(userId: String): List<UserReport> =
        reports.filter { it.userId == userId }

    fun reportsInArea(latMin: Double, latMax: Double, lonMin: Double, lonMax: Double): List<UserReport> =
        reports.filter { it.latitude in latMin..latMax && it.longitude in lonMin..lonMax }

    fun latestReports(since: Long): List<UserReport> =
        reports.filter { it.timestamp >= since }
}
