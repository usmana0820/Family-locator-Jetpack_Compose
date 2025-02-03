package com.auth0.trackinapp.settings

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.google.firebase.auth.FirebaseAuth
import java.time.format.TextStyle


import androidx.compose.foundation.Canvas
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.nativeCanvas


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    navController: NavController,
    token: String
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, size.height * 0.3f)
                    cubicTo(size.width / 1, size.height * 0.1f, size.width , size.height * 0.5f, size.width, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(
                    path = path,
                    color = Color(0xFF0098c3)
                )

                drawContext.canvas.nativeCanvas.apply {
                    val textPaint = Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 60f
                        textAlign = Paint.Align.CENTER
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                    drawText(
                        "Settings",
                        size.width / 2,
                        size.height * 0.1f,
                        textPaint
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(160.dp))

                // Title with shadow effect
//                Text(
//                    text = "Settings",
//                    style = MaterialTheme.typography.headlineLarge.copy(
//                        color = Color(0xFF0098c3),
//                        fontWeight = FontWeight.Bold,
//                        shadow = Shadow(
//                            color = Color.White,
//                            offset = Offset(4f, 4f),
//                            blurRadius = 4f
//                        )
//                    ),
//                    modifier = Modifier.padding(bottom = 32.dp)
//                )

                // Settings options
                SettingOption(
                    text = "Account Settings",
                    onClick = { /* Handle Account Settings click */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingOption(
                    text = "Smart Notifications",
                    onClick = { /* Handle Smart Notifications click */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingOption(
                    text = "Group Management",
                    onClick = {
                        navController.navigate("GroupManagementScreen/$token")
                    }
                )

//                Spacer(modifier = Modifier.height(8.dp))
//
//                SettingOption(
//                    text = "Location Sharing",
//                    onClick = { /* Handle Location Sharing click */ }
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                SettingOption(
//                    text = "Flight Detection",
//                    onClick = { /* Handle Flight Detection click */ }
//                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingOption(
                    text = "Privacy & Security",
                    onClick = { /* Handle Privacy & Security click */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingOption(
                    text = "About",
                    onClick = { /* Handle About click */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingOption(
                    text = "Logout",
                    onClick = { /* Handle Logout click */ }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingOption(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
            .background(Color(0xFF0098c3), shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    // A sample navController and token for the preview
    val navController = rememberNavController()
    SettingsScreen(
        navController = navController,
        token = "sample_token"
    )
}


//
//class SettingsActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            TrackinAppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.White
//                ) {
//                    val navController = rememberNavController()
//                    SettingsActivity(navController)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SettingsActivity(navController: NavController) {
//    val user = FirebaseAuth.getInstance().currentUser
//    var userName by remember { mutableStateOf("User Name") }
//    var userEmail by remember { mutableStateOf("user@example.com") }
//    var userPhotoUrl by remember { mutableStateOf<String?>(null) }
//
//    LaunchedEffect(user) {
//        user?.let {
//            userName = it.displayName ?: "User Name"
//            userEmail = it.email ?: "user@example.com"
//            userPhotoUrl = it.photoUrl?.toString()
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // User Profile Section
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 16.dp)
//        ) {
//            Image(
//                painter = if (userPhotoUrl != null) {
//                    rememberImagePainter(userPhotoUrl)
//                } else {
//                    painterResource(id = R.drawable.google_logo)
//                },
//                contentDescription = "User Profile",
//                modifier = Modifier
//                    .size(60.dp)
//                    .clip(CircleShape)
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column {
//                Text(text = userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//                Text(text = userEmail, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
//            }
//        }
//
//        Divider(color = Color.LightGray, thickness = 1.dp)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Settings Options with Icons
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_account
//            text = "Account Settings",
//            onClick = { navController.navigate("AccountSettings") }
//        )
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_notifications
//            text = "Smart Notifications",
//            onClick = { navController.navigate("SmartNotifications") }
//        )
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_group
//            text = "Group Management",
//            onClick = { navController.navigate("GroupManagement") }
//        )
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_location)
//            text = "Location Sharing",
//            onClick = { navController.navigate("LocationSharing") }
//        )
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_flight
//            text = "Flight Detection",
//            onClick = { navController.navigate("FlightDetection") }
//        )
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_privacy
//            text = "Privacy & Security",
//            onClick = { navController.navigate("PrivacySecurity") }
//        )
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_about
//            text = "About",
//            onClick = { navController.navigate("About") }
//        )
//        SettingsOption(
//            icon = painterResource(id = R.drawable.applogo),//ic_logout
//            text = "Logout",
//            onClick = {
//                FirebaseAuth.getInstance().signOut()
//                navController.navigate("LoginScreen") {
//                    popUpTo("SettingsActivity") { inclusive = true }
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun SettingsOption(icon: Painter, text: String, onClick: () -> Unit) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(vertical = 12.dp)
//    ) {
//        Icon(
//            painter = icon,
//            contentDescription = text,
//            tint = Color.Gray,
//            modifier = Modifier.size(24.dp)
//        )
//        Spacer(modifier = Modifier.width(16.dp))
//        Text(text = text, style = MaterialTheme.typography.bodyLarge)
//    }
//    Divider(color = Color.LightGray, thickness = 0.5.dp)
// }