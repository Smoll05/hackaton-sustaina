package com.hackaton.sustaina.data.hotspot

import com.hackaton.sustaina.domain.models.Hotspot
import javax.inject.Inject

class HotspotRepository @Inject constructor(
    private val dataSource: HotspotDataSource
){
    fun addNewHotspot(hotspot: Hotspot, onComplete: (Boolean, String?) -> Unit) {
        dataSource.addHotspot(hotspot, onComplete)
    }

    fun fetchHotspot(hotspotId: String, onComplete: (Hotspot?) -> Unit) {
        dataSource.getHotspot(hotspotId, onComplete)
    }

    fun fetchHotspots(onComplete: (List<Hotspot>) -> Unit) {
        dataSource.getHotspots(onComplete)
    }

    fun observeHotspots(onComplete: (List<Hotspot>) -> Unit) {
        dataSource.observeHotspots(onComplete)
    }
}