package com.hackaton.sustaina.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
        else {
            fetchUserLocation(fusedLocationProviderClient) {
                loc -> userLocation = loc
            }
        }
    }

    val reports = remember { SustainaUserReports().allReports() }
    val events = remember { SustainaCampaigns().allCampaigns() }
    val cameraPosition = rememberCameraPositionState {
        userLocation?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude),
                15f
            )
        } ?: reports.firstOrNull()?.let {
            position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude),
                15f
            )
        }
    }
    var googleMap = remember { mutableStateOf<GoogleMap?>(null) }
    // we use the map id to have advanced markers (this one costs money)
    // see: https://developers.google.com/maps/documentation/android-sdk/advanced-markers/start
    val mapId = context.getString(R.string.map_id)

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
            Log.d("MapScreen", "is advanced marker enabled? "
                    + capabilities.isAdvancedMarkersAvailable)

            mapInstance.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style
                )
            )
            val sustainaMap = remember { SustainaMap(mapInstance) }
            sustainaMap.addHotspotZones(reports)
            sustainaMap.addCampaignPins(events)
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchUserLocation(
    fusedLocationProviderClient: FusedLocationProviderClient,
    onLocationReceived: (LatLng) -> Unit
) {
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            onLocationReceived(latLng)
        } ?: Log.e("MapScreen", "User location is null")
    }.addOnFailureListener { e: Exception ->
        Log.e("MapScreen", "Failed to get user location", e)
    }
}

