package com.hackaton.sustaina.ui.map

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.hackaton.sustaina.domain.models.Campaign
import com.hackaton.sustaina.domain.models.Hotspot

data class MapState(

    val googleMap: MutableState<GoogleMap?> = mutableStateOf(null),
    val userLocation: MutableState<LatLng?> = mutableStateOf(null),
    val hotspots: List<Hotspot> = mutableListOf(),
    val campaigns: List<Campaign> = mutableListOf(),
    val hotspotCircles: MutableMap<Circle, Hotspot> = mutableMapOf(),
    val campaignMarkers: MutableMap<Marker?, Campaign> = mutableMapOf(),
    val bottomSheetInspectMapEntryDetails: Boolean = false,
    val bottomSheetCreateMapEntry: Boolean = false,
    val cameraPosition: CameraPosition = CameraPosition
        .fromLatLngZoom(
            LatLng( // Go to CIT-U
                10.294580502983363,
                123.88107388555748
            ),
        20f
        ),
    val loading: Boolean = true,
    val mapId: String = ""
)
