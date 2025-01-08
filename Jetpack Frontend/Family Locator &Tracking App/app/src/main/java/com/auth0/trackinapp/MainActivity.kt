//@file:OptIn(ExperimentalMaterial3Api::class)
//
//package com.auth0.trackinapp.Screens
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ExitToApp
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.rememberImagePainter
//import com.auth0.trackinapp.ui.theme.TrackinAppTheme
//import com.google.firebase.auth.FirebaseAuth
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.tasks.OnSuccessListener
//import androidx.core.content.ContextCompat
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalContext
//
//class HomeScreen : ComponentActivity() {
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        setContent {
//            TrackinAppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.White
//                ) {
//                    HomeContent(fusedLocationClient)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun HomeContent(fusedLocationClient: FusedLocationProviderClient) {
//    val user = FirebaseAuth.getInstance().currentUser
//    val context = LocalContext.current
//    var isLoggedOut by remember { mutableStateOf(false) }
//    var location by remember { mutableStateOf<android.location.Location?>(null) }
//
//    // Navigate back to LoginScreen when logged out
//    if (isLoggedOut) {
//        LaunchedEffect(Unit) {
//            val intent = Intent(context, LoginScreen::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            context.startActivity(intent)
//        }
//    }
//
//    // Request location permission and initialize launcher
//    val locationPermissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        if (granted) {
//            // Permission granted, fetch location
//            fusedLocationClient.lastLocation.addOnSuccessListener(OnSuccessListener { loc ->
//                location = loc
//            })
//        } else {
//            // Permission denied
//            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Check if location permission is granted
//    val permissionGranted = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//
//    // Launch permission request if needed
//    LaunchedEffect(Unit) {
//        if (!permissionGranted) {
//            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        } else {
//            // Permission already granted, fetch location
//            fusedLocationClient.lastLocation.addOnSuccessListener(OnSuccessListener { loc ->
//                location = loc
//            })
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Navbar with user info
//        TopAppBar(
//            title = {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    user?.let {
//                        val photoUrl = it.photoUrl
//                        if (photoUrl != null) {
//                            Image(
//                                painter = rememberImagePainter(photoUrl),
//                                contentDescription = "Profile Picture",
//                                modifier = Modifier
//                                    .size(40.dp)
//                                    .clip(CircleShape),
//                                contentScale = ContentScale.Crop
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "Hi, ${it.displayName}",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 18.sp,
//                            color = Color.Black
//                        )
//                    }
//                }
//            },
//            actions = {
//                IconButton(onClick = {
//                    FirebaseAuth.getInstance().signOut()
//                    isLoggedOut = true // Trigger navigation to LoginScreen
//                }) {
//                    Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Displaying location information
//        if (location != null) {
//            Text(
//                text = "Your Location: Latitude ${location?.latitude}, Longitude ${location?.longitude}",
//                fontSize = 16.sp,
//                color = Color.Gray
//            )
//        } else {
//            Text(
//                text = "Fetching your location...",
//                fontSize = 16.sp,
//                color = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Show map navigation or other content here
//        Text(
//            text = "Map Navigation View Coming Soon...",
//            fontSize = 14.sp,
//            color = Color.Gray
//        )
//    }
//}
