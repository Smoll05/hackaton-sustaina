package com.hackaton.sustaina.domain.models

/*
    Hotspot density will be mapped to 3 levels.
    Refer to the enum implementation.
 */
enum class HotspotStatus(val statusLevel: Int) {
    UNCLEANED(1),
    IN_PROGRESS(2),
    CLEANED(3);

    // Use this to convert a level (Integer)
    // to a HotspotStatus string (HotspotStatus.toString())
    companion object {
        fun fromLevel(level: Int): HotspotStatus? {
            return entries.find { it.statusLevel == level }
        }
    }
}