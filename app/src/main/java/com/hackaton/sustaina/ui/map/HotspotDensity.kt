package com.hackaton.sustaina.ui.map

enum class HotspotDensity(val level: Int) {
    LOW(1),
    MODERATE(2),
    SEVERE(3);

    companion object {
        fun fromLevel(level: Int): HotspotDensity? {
            return entries.find { it.level == level }
        }
    }
}