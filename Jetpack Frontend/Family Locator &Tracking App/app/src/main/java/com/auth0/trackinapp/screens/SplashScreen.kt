package com.auth0.trackinapp.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import kotlinx.coroutines.delay

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                SplashScreenLayout()
            }
        }
    }
}
@Preview
@Composable
fun SplashScreenLayout() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Launch a coroutine to navigate after a delay
    LaunchedEffect(true) {
        delay(3000) // Show splash screen for 3 seconds
        // Navigate to WelcomeScreen after splash
        val intent = Intent(context, WelcomeScreen::class.java) // Navigate to WelcomeScreen
        context.startActivity(intent)
        (context as? ComponentActivity)?.finish() // Close splash screen activity
    }

    // Splash screen layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            verticalArrangement = Arrangement.Top, // Adjusted to make the app name appear at the top
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Name in Rainbow Color at the Top
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                         //   Color(0xFFEE1D52), // Red
                         //   Color(0xFFFFA300), // Orange
                            Color(0xFFFFEF00), // Yellow
                            Color(0xFF2A9D8F), // Green
                            Color(0xFF2D6DFF), // Blue
                            Color(0xFF9B2C91)  // Purple
                        )
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp) // Add padding inside the text
                    .padding(horizontal = 15.dp) // Add horizontal margin around the text
                    .padding(vertical = 50.dp), // Add vertical margin around the text
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(190.dp))

            // App Logo
            Image(
                painter = painterResource(id = R.drawable.applogo), // Replace with your logo
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.app_slogan),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )
        }
    }
}
