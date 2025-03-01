package com.hackaton.sustaina.ui.map

import com.hackaton.sustaina.ui.utils.notification.NotificationHelper
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.hackaton.sustaina.domain.models.Campaign
import com.hackaton.sustaina.domain.models.Hotspot
import com.hackaton.sustaina.domain.models.HotspotDensity
import com.hackaton.sustaina.ui.loadingscreen.LoadingScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun MapScreen(navController: NavController, key: Long, viewModel: MapViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // region init stuff

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

    val hotspots = uiState.hotspots
    val campaigns = uiState.campaigns
    val cameraPosition = rememberCameraPositionState { position = uiState.cameraPosition }

    // Handle notification and check if user is inside a hotspot

    var showPopup by remember { mutableStateOf(false) }
    var currentHotspot by remember { mutableStateOf<Hotspot?>(null) }
    val notificationHelper = remember { NotificationHelper(context) }

    // Handle bottom sheet (the pop up sheet when user clicks on a circle(hotspot) or pin(campaign))

    val bottomSheetScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedHotspot by remember { mutableStateOf<Hotspot?>(null) }
    var selectedCampaign by remember { mutableStateOf<Campaign?>(null) }
    var mapClickLocation by remember { mutableStateOf<LatLng?>(null) }

    // endregion

    if(uiState.loading) {
        LoadingScreen()
        return
    }

    Box(Modifier.fillMaxSize()) {
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
                isMyLocationEnabled = userLocation != null,
            ),
            googleMapOptionsFactory = { GoogleMapOptions().mapId(uiState.mapId) },
            onMapClick = { latLng ->
                mapClickLocation = latLng
                viewModel.showBottomSheet()
            }
        ) {
            MapEffect(key) {
                viewModel.setGoogleMap(it)

                Log.d("MapScreen", "MapEffect called $it with ID: $key")

                viewModel.addHotspotZones(hotspots)
                viewModel.addCampaignPins(campaigns)

                viewModel.onHotspotClick = { report ->
                    selectedHotspot = report
                    selectedCampaign = null
                    viewModel.showBottomSheet()
                }
                viewModel.onCampaignClick = { campaign ->
                    selectedCampaign = campaign
                    selectedHotspot = null
                    viewModel.showBottomSheet()
                }
            }
        }

        // Bottom Sheet
        if (uiState.bottomSheetState) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.hideBottomSheet()
                    selectedHotspot = null
                    selectedCampaign = null
                    mapClickLocation = null
                },
                sheetState = sheetState
            ) {
                if (selectedHotspot != null) {
                    HotspotDetailsBottomSheet(selectedHotspot!!) {
                        bottomSheetScope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                viewModel.hideBottomSheet()
                                selectedHotspot = null
                            }
                        }
                    }
                } else if (selectedCampaign != null) {
                    CampaignDetailsBottomSheet(selectedCampaign!!) {
                        bottomSheetScope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                viewModel.hideBottomSheet()
                                selectedCampaign = null
                            }
                        }
                    }
                }
            }
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

                uiState.googleMap.value!!.let { map ->
                    val insideHotspot = viewModel.isUserInHotspot(loc)

                    if (insideHotspot != null && currentHotspot != insideHotspot) {
                        // User ENTERED a new hotspot
                        // TODO( HANDLE LOGIC FOR THIS )
                        Log.d("MapScreen", "User is inside a hotspot: ${insideHotspot.description}")
                        viewModel.goToUserReportLocation(insideHotspot)

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
                        // TODO( HANDLE LOGIC FOR THIS )
                        Log.d("MapScreen", "User has left the hotspot area")
                        showPopup = false
                        currentHotspot = null
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun HotspotDetailsBottomSheet(hotspot: Hotspot, onClose: () -> Unit) {
    val hotspotDensity = HotspotDensity.fromLevel(hotspot.densityLevel)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hotspot Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Description:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = hotspot.description)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Density:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = hotspotDensity.toString())
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Location:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(
                                    LatLng(
                                        hotspot.latitude,
                                        hotspot.longitude
                                    ), 15f
                                )
                            },
                            googleMapOptionsFactory = { GoogleMapOptions().liteMode(true) }
                        ) {
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        hotspot.latitude,
                                        hotspot.longitude
                                    )
                                ),
                                title = "Hotspot Location",
                                snippet = hotspot.description
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CampaignDetailsBottomSheet(campaign: Campaign, onClose: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Campaign Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Name:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = campaign.campaignName)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Description:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = campaign.campaignAbout)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Location:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(
                                    LatLng(
                                        campaign.latitude,
                                        campaign.longitude
                                    ), 15f
                                )
                            },
                            googleMapOptionsFactory = { GoogleMapOptions().liteMode(true) }
                        ) {
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        campaign.latitude,
                                        campaign.longitude
                                    )
                                ),
                                title = "Hotspot Location",
                                snippet = campaign.campaignAbout
                            )
                        }
                    }
                }
            }
        }
    }
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
