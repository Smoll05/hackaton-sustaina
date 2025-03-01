package com.hackaton.sustaina.domain.models

/*
    Hotspot density will be mapped to 3 levels.
    Refer to the enum implementation.
 */
enum class HotspotDensity(val level: Int) {
    LOW(1),
    MODERATE(2),
    SEVERE(3);

    // Use this to convert a level (Integer)
    // to a HotspotDensity string (HotspotDensity.toString())
    companion object {
        fun fromLevel(level: Int): HotspotDensity? {
            return entries.find { it.level == level }
        }
    }
}