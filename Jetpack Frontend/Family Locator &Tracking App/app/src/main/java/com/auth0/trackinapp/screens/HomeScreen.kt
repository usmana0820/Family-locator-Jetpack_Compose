@file:OptIn(ExperimentalMaterial3Api::class)

package com.auth0.trackinapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.auth0.trackinapp.settings.SettingsActivity
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState


class HomeScreen : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            TrackinAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    HomeContent(fusedLocationClient)
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeContent(fusedLocationClient: FusedLocationProviderClient) {
    val context = LocalContext.current
    var location by remember { mutableStateOf("Fetching your location...") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    // Camera position state for map zoom
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f) // Default zoom level
    }

    // Check and request location permission
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                    currentLatLng = LatLng(latitude, longitude)
                    location = "Latitude: $latitude, Longitude: $longitude"

                    // Update camera position after location is fetched
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
                } ?: run {
                    location = "Unable to fetch location. Please try again."
                }
            }
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        // Top Bar with Settings Icon
        TopAppBar(
            title = { Text("Home Screen") },
            actions = {
                IconButton(onClick = {
                    // Navigate to settings screen
                    context.startActivity(Intent(context, SettingsActivity::class.java))
                }) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        )

        // Map section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
        ) {
            if (locationPermissionState.status.isGranted) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    // Add marker at the current location
                    Marker(
                        state = com.google.maps.android.compose.MarkerState(position = currentLatLng),
                        title = "Your Location",
                        snippet = "Latitude: $latitude, Longitude: $longitude"
                    )
                }
            } else {
                Text(
                    text = "Location permission is required to show the map.",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location info section
        Text(
            text = "Current Location: $location",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Other UI components
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                Toast.makeText(context, "Check-In feature clicked!", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Check In")
            }

            Button(onClick = {
                Toast.makeText(context, "Set Up SOS feature clicked!", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Set Up SOS")
            }
        }
    }
}
