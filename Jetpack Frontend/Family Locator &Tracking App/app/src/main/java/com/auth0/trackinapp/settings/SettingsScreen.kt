package com.auth0.trackinapp.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    SettingsActivity(navController)
                }
            }
        }
    }
}

@Composable
fun SettingsActivity(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    var userName by remember { mutableStateOf("User Name") }
    var userEmail by remember { mutableStateOf("user@example.com") }
    var userPhotoUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user) {
        user?.let {
            userName = it.displayName ?: "User Name"
            userEmail = it.email ?: "user@example.com"
            userPhotoUrl = it.photoUrl?.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User Profile Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Image(
                painter = if (userPhotoUrl != null) {
                    rememberImagePainter(userPhotoUrl)
                } else {
                    painterResource(id = R.drawable.google_logo)
                },
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = userEmail, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // Settings Options with Icons
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_account
            text = "Account Settings",
            onClick = { navController.navigate("AccountSettings") }
        )
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_notifications
            text = "Smart Notifications",
            onClick = { navController.navigate("SmartNotifications") }
        )
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_group
            text = "Group Management",
            onClick = { navController.navigate("GroupManagement") }
        )
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_location)
            text = "Location Sharing",
            onClick = { navController.navigate("LocationSharing") }
        )
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_flight
            text = "Flight Detection",
            onClick = { navController.navigate("FlightDetection") }
        )
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_privacy
            text = "Privacy & Security",
            onClick = { navController.navigate("PrivacySecurity") }
        )
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_about
            text = "About",
            onClick = { navController.navigate("About") }
        )
        SettingsOption(
            icon = painterResource(id = R.drawable.applogo),//ic_logout
            text = "Logout",
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("LoginScreen") {
                    popUpTo("SettingsActivity") { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun SettingsOption(icon: Painter, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
    Divider(color = Color.LightGray, thickness = 0.5.dp)
}