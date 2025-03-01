package com.hackaton.sustaina.ui.map

import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.AdvancedMarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PinConfig

import com.hackaton.sustaina.domain.models.Campaign
import com.hackaton.sustaina.domain.models.Hotspot

class SustainaMap(private val mMap: GoogleMap) {

    private val circleZoneReportMap = mutableMapOf<Circle, Hotspot>()
    private val campaignMarkerMap = mutableMapOf<Marker?, Campaign>()
    var onHotspotClick: ((Hotspot) -> Unit)? = null
    var onCampaignClick: ((Campaign) -> Unit)? = null

    private fun goToLocation(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    fun goToUserReportLocation(hotspot: Hotspot) {
        val latLng = LatLng(hotspot.latitude, hotspot.longitude)
        goToLocation(latLng)
    }

    fun goToCampaignLocation(campaign: Campaign) {
        val latLng = LatLng(campaign.latitude, campaign.longitude)
        goToLocation(latLng)
    }

    fun isUserInHotspot(userLocation: LatLng): Hotspot? {
        for ((circle, report) in circleZoneReportMap) {
            val hotspotCenter = circle.center
            val distance = FloatArray(1)

            Location.distanceBetween(
                userLocation.latitude, userLocation.longitude,
                hotspotCenter.latitude, hotspotCenter.longitude,
                distance
            )

            if (distance[0] <= circle.radius) {
                return report
            }
        }
        return null // User is not inside any hotspot
    }

    fun addHotspotZones(reports: List<Hotspot>) {
        reports.forEach { hotspot ->
            val latLng = LatLng(hotspot.latitude, hotspot.longitude)

            val fillColor = when (hotspot.densityLevel) {
                1 -> 0x5500FF00.toInt()     // Semi-transparent green -- low density
                2 -> 0x55FFA500.toInt()     // Semi-transparent orange -- moderate density
                3 -> 0x55FF0000.toInt()     // Semi-transparent red -- severe density
                else -> 0x5500FF00.toInt()  // Default -- green
            }

            val strokeColor = when (hotspot.densityLevel) {
                1 -> 0xFF008000.toInt()     // Dark green -- low density
                2 -> 0xFFFFA500.toInt()     // Orange -- moderate density
                3 -> 0xFFFF0000.toInt()     // Red -- severe density
                else -> 0xFF008000.toInt()  // Default -- dark green
            }

            val circleOption = CircleOptions()
                .center(latLng)
                .radius(10.0)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
                .strokeWidth(2f)
                .clickable(true)

            val circle = mMap.addCircle(circleOption)
            circleZoneReportMap[circle] = hotspot
        }
        mMap.setOnCircleClickListener { clickedCircle ->
            circleZoneReportMap[clickedCircle]?.let {
                onHotspotClick?.invoke(it)
            }
        }
    }

    fun addCampaignPins(campaigns: List<Campaign>) {
        campaigns.forEach { campaign ->
            val latLng = LatLng(campaign.latitude, campaign.longitude)

            val pinConfig = PinConfig.builder()
                .setBackgroundColor(0xFF87CEEB.toInt())
                .setBorderColor(0xFF4682B4.toInt())
                .setGlyph(PinConfig.Glyph("🎉"))
                .build()

            val markerOptions = AdvancedMarkerOptions()
                .position(latLng)
                .title(campaign.campaignName)
                .snippet(campaign.campaignDescription)
                .icon(BitmapDescriptorFactory.fromPinConfig(pinConfig))

            val marker = mMap.addMarker(markerOptions)
            campaignMarkerMap[marker] = campaign
        }

        mMap.setOnMarkerClickListener { clickedMarker ->
            campaignMarkerMap[clickedMarker]?.let {
                onCampaignClick?.invoke(it)
            }
            true
        }
    }
}
