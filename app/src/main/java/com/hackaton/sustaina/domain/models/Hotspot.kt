package com.hackaton.sustaina.domain.models

data class Hotspot(
    val hotspotId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val radius: Int = 0
)