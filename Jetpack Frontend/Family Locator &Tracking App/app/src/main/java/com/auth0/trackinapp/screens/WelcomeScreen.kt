package com.auth0.trackinapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
class WelcomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                WelcomeActivity(navController = rememberNavController())
            }
        }
    }
}
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WelcomeActivity(navController: NavController) {
    var isUserSignedIn by remember { mutableStateOf(false) }

    // Check if user is signed in, if so navigate to Login screen directly or show sign-in button
    if (isUserSignedIn) {
        LaunchedEffect(true) {
            navController.navigate("login_screen")
        }
    }

    // Requesting location permission
    val locationPermissionState = rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)

    // Handle permission request
    LaunchedEffect(locationPermissionState) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    // Welcome screen layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2A9D8F), // Green
                            Color(0xFF2D6DFF), // Blue
                            Color(0xFF9B2C91)  // Purple
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween // To space out the content, pushing the button to the bottom
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp) // Adjust height as needed
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    colorResource(id = R.color.white),
                                    colorResource(id = R.color.white)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = colorResource(id = R.color.app_blue),
                            )
                        )
                    }
                }

                // Circle containing the GIF, tutorial, and logo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // This makes sure the circle takes up available space
                        .padding(16.dp), // Added padding for spacing inside the box
                    contentAlignment = Alignment.Center
                ) {
                    // Initialize pagerState
                    val pagerState = rememberPagerState(pageCount = { 3 })

                    // Auto slide the pager
                    LaunchedEffect(pagerState.currentPage) {
                        delay(3000) // 3 seconds delay for auto-sliding
                        val nextPage = (pagerState.currentPage + 1) % 3
                        pagerState.animateScrollToPage(nextPage)
                    }

                    // HorizontalPager
                    HorizontalPager(state = pagerState) { page ->
                        when (page) {
                            0 -> {
                                Image(
                                    painter = rememberImagePainter("https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExeXg5Zm83NHZpanUzbmI2dWpqZjd0dmdpbW05cnpucXEweHN0dnF3YiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/j44gYMJRCUnIJrg9nR/giphy-downsized-large.gif"),
                                    contentDescription = "App Tutorial Slide 1",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            1 -> {
                                Image(
                                    painter = rememberImagePainter("https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExYWw3ejJ0aDVmM3IxanEzaWVrbGZkMXN6b2ZreXpjNnZwdDhpYXExbyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/NVYJDvaFbx16ClIfIV/giphy.gif"),
                                    contentDescription = "App Tutorial Slide 2",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            2 -> {
                                Image(
                                    painter = rememberImagePainter("https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExZjZ2bTd1ZG0zMWRjYnVpbzMxdjlwcjM4dGtrN2hpeGE4OWxyM2FjdCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/HzMfJIkTZgx8s/giphy.gif"),
                                    contentDescription = "App Tutorial Slide 3",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    // Dots for slide indication
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 30.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .padding(4.dp)
                                    .background(
                                        if (pagerState.currentPage == index) Color.White else Color.Gray,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }

                // Tutorial Text that moves with the slides
                val tutorialTexts = listOf(
                    "Step 1: Enable Location Access",
                    "Step 2: Follow the tutorial",
                    "Step 3: Start tracking family members"
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tutorialTexts[1],
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                // Start button inside the circle at the bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("languages_screen")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.app_blue),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Let's Get Started",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
