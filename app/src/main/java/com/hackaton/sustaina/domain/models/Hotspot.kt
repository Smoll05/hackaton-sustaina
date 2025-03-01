package com.hackaton.sustaina.domain.models

data class Hotspot(
    val hotspotId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val description: String = "",
    val timestamp: Long = 0,
    val radius: Double = 20.0, // default to 20
    val statusLevel: Int = 0,
    val densityLevel: Int = 0,
    val imageUrl: String = "",
    val reportedByUserId: String = ""
)