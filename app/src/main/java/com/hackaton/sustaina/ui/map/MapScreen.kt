package com.hackaton.sustaina.ui.map

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.maptest.SustainaMap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapCapabilities
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.hackaton.sustaina.R

@OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(navController: NavController) {
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    val reports = remember { SustainaUserReports().allReports() }
    val events = remember { SustainaCampaigns().allCampaigns() }
    val cameraPosition = rememberCameraPositionState {
        reports.firstOrNull()?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude),
                15f
            )
        }
    }
    var googleMap = remember { mutableStateOf<GoogleMap?>(null) }
    // we use the map id to have advanced markers (this one costs money)
    // see: https://developers.google.com/maps/documentation/android-sdk/advanced-markers/start
    val mapId = LocalContext.current.getString(R.string.map_id)

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPosition,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true,
            tiltGesturesEnabled = true,
            rotationGesturesEnabled = true
        ),
        properties = MapProperties(
            isMyLocationEnabled = locationPermissionState.status.isGranted
        ),
        googleMapOptionsFactory = { GoogleMapOptions().mapId(mapId) }
    ) {
        MapEffect(Unit) { map ->
            googleMap.value = map

        }
        googleMap.value?.let { mapInstance ->
            val capabilities: MapCapabilities = mapInstance.mapCapabilities
            Log.d("SUSTAINA MAP CAPABILITIES", "is advanced marker enabled? "
                    + capabilities.isAdvancedMarkersAvailable)

            mapInstance.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    LocalContext.current,
                    R.raw.map_style
                )
            )

            val sustainaMap = remember { SustainaMap(mapInstance) }

            sustainaMap.addHotspotZones(reports)
            sustainaMap.addCampaignPins(events)
        }

    }
}