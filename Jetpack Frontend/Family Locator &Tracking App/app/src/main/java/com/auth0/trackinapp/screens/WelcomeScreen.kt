package com.auth0.trackinapp.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.TrackinAppTheme

class WelcomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                WelcomeScreenLayout()
            }
        }
    }
}

@Composable
fun WelcomeScreenLayout() {
    val context = LocalContext.current
    var isUserSignedIn by remember { mutableStateOf(false) } // This can be replaced with actual sign-in check

    // Check if user is signed in, if so navigate to Login screen directly or show sign-in button
    if (isUserSignedIn) {
        // Navigate to login screen
        LaunchedEffect(true) {
            val intent = Intent(context, LoginScreen::class.java)
            context.startActivity(intent)
        }
    }

    // Welcome screen layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.applogo), // Replace with your logo
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Welcome Text
            Text(
                text = stringResource(id = R.string.welcome_app),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Start button to navigate to Login screen
            Button(
                onClick = {
                    val intent = Intent(context, LanguagesScreen::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.app_blue),contentColor = Color.White
                )
            ) {
                // Button text from string resource
                Text(text = stringResource(id = R.string.lets_started),fontWeight = FontWeight.Bold )
            }
        }
    }
}
