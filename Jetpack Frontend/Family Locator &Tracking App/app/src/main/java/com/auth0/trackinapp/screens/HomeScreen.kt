@file:OptIn(ExperimentalMaterial3Api::class)

package com.auth0.trackinapp.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.material3.IconButton
import com.auth0.trackinapp.SharedPrefManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.auth0.trackinapp.R
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
//
//@SuppressLint("MissingPermission")
//@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun HomeContent(fusedLocationClient: FusedLocationProviderClient, initialGroupName: String, navController: NavController, token: String) {
//    val context = LocalContext.current
//    val groupList = listOf("No Group", "Group A", "Group B", "Group C")
//    var expanded by remember { mutableStateOf(false) }
//    var selectedGroup by remember { mutableStateOf(initialGroupName) }
//
//    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
//
//    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
//    }
//
//    // Location fetch
//    LaunchedEffect(Unit) {
//        if (locationPermissionState.status.isGranted) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                location?.let {
//                    currentLatLng = LatLng(it.latitude, it.longitude)
//                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
//                }
//            }
//        } else {
//            locationPermissionState.launchPermissionRequest()
//        }
//    }
//
//    val sheetState = rememberBottomSheetScaffoldState()
//    val scope = rememberCoroutineScope()
//
//    BottomSheetScaffold(
//        scaffoldState = sheetState,
//        sheetPeekHeight = 190.dp,
//        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
//        sheetContainerColor = colorResource(id = R.color.blueish),
//        sheetContent = {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("Group Members", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                Spacer(modifier = Modifier.height(12.dp))
//
//                val dummyUsers = listOf(
//                    "Usmana Zulfiqar" to "Software Developer",
//                    "Ahmed Raza" to "UI/UX Designer",
//                    "Fatima Noor" to "Mobile Developer",
//                    "Ali Khan" to "Backend Engineer"
//                )
//
//                dummyUsers.forEach { (name, role) ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .padding(vertical = 8.dp)
//                            .fillMaxWidth()
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_person),
//                            contentDescription = "User",
//                            modifier = Modifier
//                                .size(40.dp)
//                                .clip(CircleShape)
//                        )
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Column {
//                            Text(name, color = Color.White, fontWeight = FontWeight.SemiBold)
//                            Text(role, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    Button(
//                        onClick = { navController.navigate("subscription_screen") },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
//                    ) {
//                        Text("Upgrade to Premium", color = colorResource(id = R.color.blueish), fontWeight = FontWeight.Bold)
//                    }
//                }
//            }
//        }
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            // Map
//            GoogleMap(
//                modifier = Modifier.fillMaxSize(),
//                cameraPositionState = cameraPositionState,
//                properties = MapProperties(isMyLocationEnabled = true)
//            )
//
//            // Top Bar
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(colorResource(id = R.color.blueish))
//                    .padding(horizontal = 16.dp, vertical = 12.dp)
//                    .align(Alignment.TopCenter)
//            ) {
//
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(colorResource(id = R.color.blueish))
//                        .background()
//                        .padding(horizontal = 16.dp, vertical = 12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Group Name Dropdown
//                    Box(
//                        modifier = Modifier
//                            .background(Color.White, shape = RoundedCornerShape(20.dp))
//                            .clickable { expanded = true }
//                            .padding(horizontal = 12.dp, vertical = 6.dp)
//                    ) {
//                        Text(
//                            text = selectedGroup,
//                            color = colorResource(id = R.color.blueish),
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
//
//                    // Spacer to push the app name to the center
//                    Spacer(modifier = Modifier.weight(1f))
//
//                    // App Name
//                    Text(
//                        text = "Family Locator",
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    // Spacer to push the settings icon to the right
//                    Spacer(modifier = Modifier.weight(1f))
//
//                    // Settings Icon
//                    IconButton(
//                        onClick = {
//                            val storedToken = SharedPrefManager.getToken() ?: ""
//                            navController.navigate("SettingsScreen/$storedToken")
//                        }
//                    ) {
//                        Icon(
//                            Icons.Default.Settings,
//                            contentDescription = "Settings",
//                            tint = Color.White
//                        )
//                    }
//
//            }
//            }
//
//
//        }
//    }
//}










@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(fusedLocationClient: FusedLocationProviderClient, initialGroupName: String, navController: NavController, token: String) {
    val context = LocalContext.current
    val groupList = listOf("Group A", "Group B", "Group C", "Group D")
    var expanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf(initialGroupName) }
    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
    }

    LaunchedEffect(Unit) {
        if (locationPermissionState.status.isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    currentLatLng = LatLng(it.latitude, it.longitude)
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, 15f)
                }
            }
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    val sheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val actions = listOf(
        stringResource(id = R.string.sos),
        stringResource(id = R.string.check_in),
        stringResource(id = R.string.share),
        stringResource(id = R.string.navigate),
        stringResource(id = R.string.history),
        stringResource(id = R.string.help)
    )

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 230.dp,
        sheetShape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
        sheetContainerColor = colorResource(id = R.color.light_blueish),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(13.dp)
                    .padding(bottom = 24.dp)
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(actions) { action ->
                        Card(
                            modifier = Modifier.size(50.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.blueish)),
                            shape = CircleShape
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    action,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.group_members),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color =colorResource(id = R.color.blueish)
                )

                val dummyUsers = listOf(
                    "Usmana Zulfiqar" to "Software Developer",
                    "Ahmed Raza" to "UI/UX Designer",
                    "Fatima Noor" to "Mobile Developer",
                    "Ali Khan" to "Backend Engineer",
                    "Hira Shah" to "QA Engineer"
                )

                dummyUsers.take(3).forEach { (name, role) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = "User", tint = colorResource(id = R.color.blueish))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(name, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(role, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { navController.navigate("subscription_screen") },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueish))
                    ) {
                        Text(stringResource(id = R.string.upgrade_premium), color = Color.White)
                    }
                }
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.blueish))
                    .padding(horizontal = 28.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                                .clickable { expanded = true }
                                .padding(horizontal = 26.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.group_prefix) + selectedGroup,
                                color = colorResource(id = R.color.blueish),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            offset = DpOffset(x = 0.dp, y = 10.dp),
                            modifier = Modifier.width(IntrinsicSize.Min)
                        ) {
                            groupList.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group) },
                                    onClick = {
                                        selectedGroup = group
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            val storedToken = SharedPrefManager.getToken() ?: ""
                            navController.navigate("SettingsScreen/$storedToken")
                        },
                        modifier = Modifier.weight(0.5f),
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings),
                            tint = Color.White
                        )
                    }
                }
            }

            // Google Map
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = locationPermissionState.status.isGranted),
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                Marker(
                    state = MarkerState(position = currentLatLng),
                    title = "You are here"
                )
            }
        }
    }
}
