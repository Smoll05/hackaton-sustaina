package com.hackaton.sustaina.ui.map

import com.hackaton.sustaina.ui.utils.notification.NotificationHelper
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.hackaton.sustaina.domain.models.HotspotStatus
import com.hackaton.sustaina.ui.loadingscreen.LoadingScreen
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.random.Random

@OptIn(
    ExperimentalPermissionsApi::class, MapsComposeExperimentalApi::class,
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

    if (uiState.loading) {
        LoadingScreen()
        return
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                zoomGesturesEnabled = true,
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
                viewModel.showBottomSheetInspectMapEntryDetails()
            },
            onMapLongClick = { latLng ->
                mapClickLocation = latLng
                viewModel.showBottomSheetCreateMapEntry()
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
                    viewModel.showBottomSheetInspectMapEntryDetails()
                }
                viewModel.onCampaignClick = { campaign ->
                    selectedCampaign = campaign
                    selectedHotspot = null
                    viewModel.showBottomSheetInspectMapEntryDetails()
                }
            }
        }

        if (uiState.bottomSheetCreateMapEntry) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                onDismissRequest = {
                    viewModel.hideBottomSheetCreateMapEntry()
                    mapClickLocation = null
                },
                sheetState = sheetState
            ) {
                CreateEntryBottomSheet(navController, mapClickLocation, viewModel, context) {
                    bottomSheetScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            viewModel.hideBottomSheetCreateMapEntry()
                        }
                    }
                }
            }
        }

        // Bottom Sheet
        if (uiState.bottomSheetInspectMapEntryDetails) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.hideBottomSheetMapEntryDetails()
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
                                viewModel.hideBottomSheetMapEntryDetails()
                                selectedHotspot = null
                            }
                        }
                    }
                } else if (selectedCampaign != null) {
                    CampaignDetailsBottomSheet(selectedCampaign!!, navController) {
                        bottomSheetScope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                viewModel.hideBottomSheetMapEntryDetails()
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
        Log.d(
            "MapScreen",
            "LaunchedEffect triggered. Permission: ${locationPermissionState.status.isGranted}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        }

        if (!locationPermissionState.status.isGranted) {
            Log.d("MapScreen", "Requesting permissions.")
            locationPermissionState.launchPermissionRequest()
        } else {
            Log.d("MapScreen", "Fetching user location.")
            startLocationUpdates(fusedLocationProviderClient, context) { loc ->
                userLocation = loc
                Log.d("MapScreen", "User location: $loc")

                uiState.googleMap.value.let { map ->
                    val insideHotspot = viewModel.isUserInHotspot(loc)

                    if (insideHotspot != null && currentHotspot != insideHotspot) {
                        // User ENTERED a new hotspot
                        // TODO( HANDLE LOGIC FOR THIS )
                        Log.d("MapScreen", "User is inside a hotspot: ${insideHotspot.description}")
                        viewModel.goToUserReportLocation(insideHotspot)

                        if (notificationPermission.status.isGranted) {
                            notificationHelper.sendHotspotNotification(insideHotspot.description)
                        } else {
                            Log.d(
                                "MapScreen",
                                "Notification permission is not granted. Skipping notification."
                            )
                        }
//                        showPopup = true
                        currentHotspot = insideHotspot
                    } else if (insideHotspot == null && currentHotspot != null) {
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
fun CreateEntryBottomSheet(
    navController: NavController,
    mapClickLocation: LatLng?,
    viewModel: MapViewModel,
    context: Context,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Create Hotspot", "Create Campaign")

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
                text = "New Entry",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> CreateHotspotForm(navController, mapClickLocation, viewModel, context, onClose)
            1 -> CreateCampaignForm(navController, mapClickLocation, viewModel, context, onClose)
        }
    }
}

@Composable
fun CreateHotspotForm(
    navController: NavController,
    mapClickLocation: LatLng?,
    viewModel: MapViewModel,
    context: Context,
    onClose: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(HotspotStatus.UNCLEANED) }
    var selectedDensity by remember { mutableStateOf(HotspotDensity.LOW) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Create a new Hotspot", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Hotspot Description", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Status Level", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        DropdownMenuBox(
            options = HotspotStatus.entries,
            selectedOption = selectedStatus,
            onOptionSelected = { selectedStatus = it },
            label = "Select Status"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Density Level", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        DropdownMenuBox(
            options = HotspotDensity.entries,
            selectedOption = selectedDensity,
            onOptionSelected = { selectedDensity = it },
            label = "Select Density"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val newHotspot = Hotspot(
                    description = description,
                    statusLevel = selectedStatus.statusLevel,
                    densityLevel = selectedDensity.level,

                    hotspotId = Random.nextInt(0, 1_000_000).toString(),
                    latitude = mapClickLocation!!.latitude,
                    longitude = mapClickLocation.longitude,
                    radius = 20.0,
                    timestamp = System.currentTimeMillis(),
                    imageUrl = "",
                    reportedByUserId = "user@" + Random.nextInt(0, 1_000_000).toString()
                )
                viewModel.addHotspot(newHotspot)
                Toast.makeText(context, "Hotspot added", Toast.LENGTH_SHORT).show()
                Log.d("MapScreen", "New hotspot added: ${newHotspot.description}")
                onClose()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit Hotspot")
        }
    }
}

@Composable
fun <T> DropdownMenuBox(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = selectedOption.toString())
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CreateCampaignForm(
    navController: NavController,
    mapClickLocation: LatLng?,
    viewModel: MapViewModel,
    context: Context,
    onClose: () -> Unit
) {
    var campaignName by remember { mutableStateOf("") }
    var campaignOrganizer by remember { mutableStateOf("") }
    var campaignStartDate by remember { mutableStateOf(Instant.now().toEpochMilli()) }
    var campaignAbout by remember { mutableStateOf("") }
    var campaignVenue by remember { mutableStateOf("") }
    var campaignAddress by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Create a new Campaign", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = campaignName,
            onValueChange = { campaignName = it },
            label = { Text("Campaign Name", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = campaignOrganizer,
            onValueChange = { campaignOrganizer = it },
            label = { Text("Organizer", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = campaignAbout,
            onValueChange = { campaignAbout = it },
            label = { Text("Campaign Description", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = campaignVenue,
            onValueChange = { campaignVenue = it },
            label = { Text("Venue", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = campaignAddress,
            onValueChange = { campaignAddress = it },
            label = { Text("Address", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val newCampaign = Campaign(
                    campaignName = campaignName,
                    campaignOrganizer = campaignOrganizer,
                    campaignStartDate = campaignStartDate,
                    campaignAbout = campaignAbout,
                    campaignVenue = campaignVenue,
                    campaignAddress = campaignAddress,
                    campaignId = Random.nextInt(0, 1_000_000).toString(),

                    latitude = mapClickLocation!!.latitude,
                    longitude = mapClickLocation.longitude,
                    campaignOrganizerId = "user@" + Random.nextInt(0, 1_000_000).toString(),
                    campaignAttendingUser = List(0) { campaignOrganizer }
                )
                viewModel.addCampaign(newCampaign)
                Toast.makeText(context, "Campaign added", Toast.LENGTH_SHORT).show()
                Log.d("MapScreen", "New campaign added: ${newCampaign.campaignName}")
                onClose()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit Campaign")
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CampaignDetailsBottomSheet(
    campaign: Campaign,
    navController: NavController,
    onClose: () -> Unit
) {

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
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("AboutIssue/${campaign.campaignId}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("MORE DETAILS", fontWeight = FontWeight.Bold)
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
