package com.auth0.trackinapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.TrackinAppTheme

class GroupManagementScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    GroupManagementActivity(navController = androidx.navigation.compose.rememberNavController())
                }
            }
        }
    }
}

@Composable
fun GroupManagementActivity(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(Color(0xFF0098C3), Color(0xFF0070AC))
            )) // Gradient background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Center content vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.applogo), // Add your logo image in res/drawable
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 32.dp)
            )

            // Group Management Title
            Text(
                text = "Group Management", // Title text
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Create Group Button
            ElevatedButton(
                onClick = {
                    navController.navigate("create_group") // Navigate to Create Group Screen
                },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0070AC),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Create Group",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Join Existing Group Button
            ElevatedButton(
                onClick = {
                    navController.navigate("join_group") // Navigate to Join Existing Group Screen
                },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0070AC),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Join Existing Group",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
