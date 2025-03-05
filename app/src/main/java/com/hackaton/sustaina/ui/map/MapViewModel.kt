package com.hackaton.sustaina.ui.map

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.AdvancedMarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.hackaton.sustaina.R
import com.hackaton.sustaina.data.auth.AuthRepository
import com.hackaton.sustaina.data.campaign.CampaignRepository
import com.hackaton.sustaina.data.hotspot.HotspotRepository
import com.hackaton.sustaina.domain.models.Campaign
import com.hackaton.sustaina.domain.models.Hotspot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val campaignRepo: CampaignRepository,
    private val hotspotRepo: HotspotRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<MapState> = MutableStateFlow(MapState())
    val uiState = _uiState.asStateFlow()

    var onHotspotClick: ((Hotspot) -> Unit)? = null
    var onCampaignClick: ((Campaign) -> Unit)? = null

    init {
        viewModelScope.launch {
            setMapId(R.string.map_id.toString())
            hotspotRepo.observeHotspots { hotspots ->
                _uiState.value = _uiState.value.copy(hotspots = hotspots)
            }
            addHotspotZones(_uiState.value.hotspots)

            campaignRepo.observeCampaigns { campaigns ->
                _uiState.value = _uiState.value.copy(campaigns = campaigns)
            }
            addCampaignPins(_uiState.value.campaigns)

            _uiState.value = _uiState.value.copy(loading = false)
        }
    }

    fun getUserId(): String {
        return authRepository.currentUserId
    }

    fun addCampaign(campaign: Campaign) {
        viewModelScope.launch {
            campaignRepo.createCampaign(campaign) { success, message ->
                if(success) {
                    Log.d("MapViewModel", "addCampaign success: $success, message: $message")
                }
                else Log.e("MapViewModel", "addCampaign failed: $success, message: $message")
            }
        }
        val markerOptions = createPin(campaign)
        val marker = _uiState.value.googleMap.value?.addMarker(markerOptions)
        _uiState.value.campaignMarkers[marker] = campaign
    }

    fun addHotspot(hotspot: Hotspot) {
        viewModelScope.launch {
            hotspotRepo.addNewHotspot(hotspot) { success, message ->
                if(success) {
                    Log.d("MapViewModel", "addHotspot success: $success, message: $message")
                }
                else Log.e("MapViewModel", "addHotspot failed: $success, message: $message")
            }
        }
        val circle = createCircle(hotspot)
        val circleOption = _uiState.value.googleMap.value!!.addCircle(circle)
        _uiState.value.hotspotCircles[circleOption] = hotspot
    }

    fun setGoogleMap(googleMap: GoogleMap) {
        _uiState.update { it.copy(googleMap = mutableStateOf(googleMap)) }
    }

    fun goToUserReportLocation(hotspot: Hotspot) {
        val latLng = LatLng(hotspot.latitude, hotspot.longitude)
        goToLocation(latLng)
    }

    fun goToCampaignLocation(campaign: Campaign) {
        val latLng = LatLng(campaign.latitude, campaign.longitude)
        goToLocation(latLng)
    }

    private fun goToLocation(latLng: LatLng) {
        _uiState.update { it.copy(
            cameraPosition = CameraPosition.fromLatLngZoom(latLng, 15f))
        }
        _uiState.value.googleMap.value?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                _uiState.value.cameraPosition.target,
                _uiState.value.cameraPosition.zoom
            )
        )
    }

    fun isUserInHotspot(userLocation: LatLng): Hotspot? {
        for ((circle, report) in _uiState.value.hotspotCircles) {
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

    fun addHotspotZones(hotspots: List<Hotspot>) {
        hotspots.forEach { hotspot ->
            val circleOption = createCircle(hotspot)
            val circle = _uiState.value.googleMap.value!!.addCircle(circleOption)
            _uiState.value.hotspotCircles[circle] = hotspot
        }
        uiState.value.googleMap.value?.setOnCircleClickListener { clickedCircle ->
            _uiState.value.hotspotCircles[clickedCircle]?.let {
                onHotspotClick?.invoke(it)
            }
        }
    }

    fun addCampaignPins(campaigns: List<Campaign>) {
        campaigns.forEach { campaign ->
            val markerOptions = createPin(campaign)
            val marker = _uiState.value.googleMap.value?.addMarker(markerOptions)
            _uiState.value.campaignMarkers[marker] = campaign
        }

        uiState.value.googleMap.value?.setOnMarkerClickListener { clickedMarker ->
            _uiState.value.campaignMarkers[clickedMarker]?.let {
                onCampaignClick?.invoke(it)
            }
            true
        }
    }

    // we use the map id to have advanced markers/custom markers (this one costs money)
    // see: https://developers.google.com/maps/documentation/android-sdk/advanced-markers/start
    private fun setMapId(mapId: String) {
        _uiState.update { it.copy(mapId = mapId) }
    }

    private fun createCircle(hotspot: Hotspot): CircleOptions {
        val latLng = LatLng(hotspot.latitude, hotspot.longitude)
        val fillColor = when (hotspot.densityLevel) {
            1 -> 0x1100FF00.toInt()     // Semi-transparent green -- low density
            2 -> 0x11FFA500.toInt()     // Semi-transparent orange -- moderate density
            3 -> 0x11FF0000.toInt()     // Semi-transparent red -- severe density
            else -> 0x1100FF00.toInt()  // Default -- green
        }
        val strokeColor = when (hotspot.densityLevel) {
            1 -> 0xFF008000.toInt()     // Dark green -- low density
            2 -> 0xFFFFA500.toInt()     // Orange -- moderate density
            3 -> 0xFFFF0000.toInt()     // Red -- severe density
            else -> 0xFF008000.toInt()  // Default -- dark green
        }
        val circleOption = CircleOptions()
            .center(latLng)
            .radius(hotspot.radius)
            .strokeColor(strokeColor)
            .fillColor(fillColor)
            .strokeWidth(2f)
            .clickable(true)
        return circleOption
    }

    private fun createPin(campaign: Campaign): AdvancedMarkerOptions {
        val latLng = LatLng(campaign.latitude, campaign.longitude)
        val pinConfig = PinConfig.builder()
            .setBackgroundColor(0xFF87CEEB.toInt())
            .setBorderColor(0xFF4682B4.toInt())
            .setGlyph(PinConfig.Glyph("🎉"))
            .build()
        val markerOptions = AdvancedMarkerOptions()
            .position(latLng)
            .title(campaign.campaignName)
            .snippet(campaign.campaignAbout)
            .icon(BitmapDescriptorFactory.fromPinConfig(pinConfig))
        return markerOptions
    }

    fun showBottomSheetCreateMapEntry() {
        _uiState.update { it.copy(bottomSheetCreateMapEntry = true) }
    }

    fun hideBottomSheetCreateMapEntry() {
        _uiState.update { it.copy(bottomSheetCreateMapEntry = false) }
    }

    fun showBottomSheetInspectMapEntryDetails() {
        _uiState.update { it.copy(bottomSheetInspectMapEntryDetails = true) }
    }
    fun hideBottomSheetMapEntryDetails() {
        _uiState.update { it.copy(bottomSheetInspectMapEntryDetails = false) }
    }
}