package com.hackaton.sustaina.ui.map

import com.hackaton.sustaina.ui.utils.notification.NotificationHelper
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.hackaton.sustaina.R

@OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(navController: NavController, key: Long) {
    val context = LocalContext.current

    // PERMISSIONS

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notificationPermission = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
    )

    // LOCATION STUFF

    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // MAP ELEMENTS

    val reports = remember { SustainaUserReports().allReports() }
    val events = remember { SustainaCampaigns().allCampaigns() }
    var googleMap = remember { mutableStateOf<GoogleMap?>(null) }
    val sustainaMap = remember { mutableStateOf<SustainaMap?>(null) }

    // we use the map id to have advanced markers/custom markers (this one costs money)
    // see: https://developers.google.com/maps/documentation/android-sdk/advanced-markers/start
    val mapId = context.getString(R.string.map_id)

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

    // Handle notification and check if user is inside a hotspot

    var showPopup by remember { mutableStateOf(false) }
    var currentHotspot by remember { mutableStateOf<UserReport?>(null) }
    val notificationHelper = remember { NotificationHelper(context) }


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
            isMyLocationEnabled = userLocation != null
        ),
        googleMapOptionsFactory = { GoogleMapOptions().mapId(mapId) }
    ) {
        MapEffect(key) { map ->
            googleMap.value = map
            Log.d("MapScreen", "MapEffect called $map with ID: $key")

            if(sustainaMap.value == null) {
                sustainaMap.value = SustainaMap(map)
                sustainaMap.value?.addHotspotZones(reports)
                sustainaMap.value?.addCampaignPins(events)
            }
        }
    }

    if (showPopup && currentHotspot != null) {
        HotspotPopup(currentHotspot!!) {
            showPopup = false
            currentHotspot = null
        }
    }

    LaunchedEffect(userLocation) {
        userLocation?.let { loc ->
            val currentZoom = cameraPosition.position.zoom
            val newLatLng = LatLng(loc.latitude, loc.longitude)

            cameraPosition.animate(
                CameraUpdateFactory.newLatLngZoom(newLatLng, currentZoom),
                1000
            )
        }
    }

    LaunchedEffect(key) {
        Log.d("MapScreen", "LaunchedEffect called with key: $key")
        Log.d("MapScreen", "LaunchedEffect triggered. Permission: ${locationPermissionState.status.isGranted}")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        }

        if (!locationPermissionState.status.isGranted) {
            Log.d("MapScreen", "Requesting permissions.")
            locationPermissionState.launchPermissionRequest()
        }
        else {
            Log.d("MapScreen", "Fetching user location.")
            startLocationUpdates(fusedLocationProviderClient, context) { loc ->
                userLocation = loc
                Log.d("MapScreen", "User location: $loc")

                sustainaMap.value?.let { map ->
                    val insideHotspot = map.isUserInHotspot(loc)

                    if (insideHotspot != null && currentHotspot != insideHotspot) {
                        // User ENTERED a new hotspot
                        // TODO( HANDLE UI AND FUNCTIONALITIES FOR THIS )
                        Log.d("MapScreen", "User is inside a hotspot: ${insideHotspot.description}")
                        map.goToUserReportLocation(insideHotspot)

                        if (notificationPermission.status.isGranted) {
                            notificationHelper.sendHotspotNotification(insideHotspot.description)
                        } else {
                            Log.d("MapScreen", "Notification permission is not granted. Skipping notification.")
                        }
//                        showPopup = true
                        currentHotspot = insideHotspot
                    }
                    else if (insideHotspot == null && currentHotspot != null) {
                        // User EXITED the hotspot
                        // TODO( HANDLE UI FOR THIS )
                        Log.d("MapScreen", "User has left the hotspot area")
                        showPopup = false
                        currentHotspot = null
                    }
                }
            }
        }
    }
}

@Composable
fun HotspotPopup(hotspot: UserReport, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Garbage Hotspot Alert!") },
        text = { Text("You're in a hotspot zone:\n\n${hotspot.description}") },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("OK")
            }
        }
    )
}

@SuppressLint("MissingPermission")
private fun startLocationUpdates(
    fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context,
    onLocationReceived: (LatLng) -> Unit
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000
    ).build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                onLocationReceived(latLng)
                Log.d("MapScreen", "User location: $latLng")
            }
        }
    }

    fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        context.mainLooper
    )
}
