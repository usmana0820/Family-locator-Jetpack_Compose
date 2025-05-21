package com.auth0.trackinapp.screens

import android.content.Context
import android.graphics.fonts.FontStyle
import android.os.Bundle
import android.util.LayoutDirection
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auth0.trackinapp.R
import kotlinx.coroutines.delay

import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.android.tools.build.jetifier.core.utils.Log
import com.auth0.trackinapp.SharedPrefManager



@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    SharedPrefManager.init(context)
    // Check if token is available and log it
    val token = SharedPrefManager.getToken()
    Log.e("SplashScreen", "Valid Token: $token")  // Print the token in log


    LaunchedEffect(Unit) {
        delay(1000)  // Simulate loading time

        if (SharedPrefManager.isTokenAvailable()) {
            // If token exists, navigate to home screen
            // Pass the token to the home screen
            val groupName = "Group " // Get the group name or any other relevant data
            navController.navigate("home_screen/$groupName/$token")
        } else {
            // Otherwise, navigate to login screen
            navController.navigate("LoginScreen")
        }






    }
    // Animation for fade-in effect on logo
    val logoAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
    )


    // Animation for button background scale effect
    val buttonScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
    )

    // Animation for sliding content from the bottom
    val slideInOffsetY by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
    )

//    LaunchedEffect(Unit) {
//        delay(3000) // Splash screen duration
//        navController.navigate("LoginScreen") // Navigate to login screen
//    }




    // Full-screen background with gradient
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.light_sea_green), // Light sea green
                            colorResource(id = R.color.blueish)                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo and Slogan
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .offset(y = slideInOffsetY.dp) // Apply slide-in animation
                ) {
                    // Logo with fade-in effect
                    Image(
                        painter = painterResource(id = R.drawable.applogo), // Replace with your logo
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(200.dp)
                            .alpha(logoAlpha) // Apply fade-in effect
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Button-like background behind app name with increased size
                    Box(
                        modifier = Modifier
                            .graphicsLayer(
                                scaleX = buttonScale,
                                scaleY = buttonScale
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.headlineLarge.copy(color = Color(0xFFFFFFFF)), // White color
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Slogan Text
                    Text(
                        text = stringResource(id = R.string.app_slogan),
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                        fontWeight = FontWeight.Normal
                    )

                    // Check if token is available and log it
                    val token = SharedPrefManager.getToken()
                    Log.d("SplashScreen", "Valid Token: $token")  // Print the token in log
                    Log.e("SplashScreen", "Valid Token: $token")  // Print the token in log

                }
            }
        }
    }
}
