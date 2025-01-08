package com.auth0.trackinapp.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.auth0.trackinapp.R // Ensure R is properly imported for your resources

class LanguagesScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                LanguagesScreenLayout()
            }
        }
    }
}
@Preview
@Composable
fun LanguagesScreenLayout() {
    val context = LocalContext.current
    var selectedLanguage by remember { mutableStateOf("") } // Tracks the selected language

    // Languages list (you can customize this)
    val languages = listOf("English", "Spanish", "French", "German", "Italian", "Portuguese", "Dutch", "Chinese", "Japanese", "Korean", "Nepali", "Hindi", "Urdu")


    // Language selection button behavior
    if (selectedLanguage.isNotEmpty()) {
        LaunchedEffect(true) {
            val intent = Intent(context, LoginScreen::class.java)
            context.startActivity(intent)
            (context as? ComponentActivity)?.finish() // Close language screen activity
        }
    }

    // Language screen layout
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        // Make the screen scrollable
        ) {
            // Rainbow Background for TrackinApp Logo and Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Adjust height as needed
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                               // colorResource(id = R.color.app_yellow),
                                colorResource(id = R.color.white),
                                colorResource(id = R.color.white),
                              //  colorResource(id = R.color.app_parrot),
                              //  colorResource(id = R.color.app_orange)
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
                    Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = colorResource(id = R.color.app_blue),
                    ))
                }
            }

            // Centered "Select Your Language" Text
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.select_your_language),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            // List of Language Buttons
            Column(
                modifier = Modifier
                    .fillMaxSize() // Make the Column fill the entire screen
                    .padding(16.dp), // Optional padding for spacing
                verticalArrangement = Arrangement.Center, // Center content vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
            ) {
                languages.forEach { language ->
                    Button(
                        onClick = {
                            selectedLanguage = language // Set the selected language
                        },
                        modifier = Modifier
                            .padding(top = 10.dp) // Space between buttons
                            .width(350.dp) // Set a fixed width for the button
                            .height(50.dp), // Set a fixed height for the button
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.app_blue), // Background color
                            contentColor = Color.White // Text color
                        )
                    ) {
                        Text(
                            text = language,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp // Font size of the button text
                        )
                    }
                }
            }
        }
    }
}
