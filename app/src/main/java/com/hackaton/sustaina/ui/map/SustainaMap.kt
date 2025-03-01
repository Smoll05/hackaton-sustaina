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

class SustainaMap(private val mMap: GoogleMap) {

    private val circleZoneReportMap = mutableMapOf<Circle, UserReport>()
    private val campaignMarkerMap = mutableMapOf<Marker?, SustainaCampaign>()
    var onHotspotClick: ((UserReport) -> Unit)? = null
    var onCampaignClick: ((SustainaCampaign) -> Unit)? = null

    private fun goToLocation(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    fun goToUserReportLocation(report: UserReport) {
        val latLng = LatLng(report.latitude, report.longitude)
        goToLocation(latLng)
    }

    fun goToCampaignLocation(campaign: SustainaCampaign) {
        val latLng = LatLng(campaign.latitude, campaign.longitude)
        goToLocation(latLng)
    }

    fun isUserInHotspot(userLocation: LatLng): UserReport? {
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

    fun addHotspotZones(reports: List<UserReport>) {
        reports.forEach { report ->
            val latLng = LatLng(report.latitude, report.longitude)

            val fillColor = when (report.density) {
                1 -> 0x5500FF00.toInt()     // Semi-transparent green -- low density
                2 -> 0x55FFA500.toInt()     // Semi-transparent orange -- moderate density
                3 -> 0x55FF0000.toInt()     // Semi-transparent red -- severe density
                else -> 0x5500FF00.toInt()  // Default -- green
            }

            val strokeColor = when (report.density) {
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
            circleZoneReportMap[circle] = report
        }
        mMap.setOnCircleClickListener { clickedCircle ->
            circleZoneReportMap[clickedCircle]?.let {
                onHotspotClick?.invoke(it)
            }
        }
    }

    fun addCampaignPins(campaigns: List<SustainaCampaign>) {
        campaigns.forEach { event ->
            val latLng = LatLng(event.latitude, event.longitude)

            val pinConfig = PinConfig.builder()
                .setBackgroundColor(0xFF87CEEB.toInt())
                .setBorderColor(0xFF4682B4.toInt())
                .setGlyph(PinConfig.Glyph("🎉"))
                .build()

            val markerOptions = AdvancedMarkerOptions()
                .position(latLng)
                .title(event.name)
                .snippet(event.description)
                .icon(BitmapDescriptorFactory.fromPinConfig(pinConfig))

            val marker = mMap.addMarker(markerOptions)
            campaignMarkerMap[marker] = event
        }

        mMap.setOnMarkerClickListener { clickedMarker ->
            campaignMarkerMap[clickedMarker]?.let {
                onCampaignClick?.invoke(it)
            }
            true
        }
    }
}
