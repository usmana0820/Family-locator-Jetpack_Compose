package com.auth0.trackinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier // Import the Modifier here
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.auth0.trackinapp.groupmanagement.CreateGroupActivity
import com.auth0.trackinapp.groupmanagement.JoinGroupActivity
import com.auth0.trackinapp.screens.*
import com.auth0.trackinapp.settings.SettingsActivity
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            TrackinAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), // Use Modifier here
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationHost(fusedLocationClient = fusedLocationClient)
                }
            }
        }
    }
}
@Composable
fun NavigationHost(fusedLocationClient: FusedLocationProviderClient) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") { SplashActivity(navController) }
        composable("welcome_screen") { WelcomeActivity(navController) }
        composable("languages_screen") { LanguagesActivity(navController) }
        composable("login_screen") { LoginActivity(navController) }
        composable("group_screen") { GroupManagementActivity(navController) }

        composable("create_group") { CreateGroupActivity(navController) }
        composable("join_group") { JoinGroupActivity(navController) }

        composable("settings_screen") { SettingsActivity(navController) }
        composable("home_screen/{groupName}") { backStackEntry ->
            // Retrieve the group name from the arguments
            val groupName = backStackEntry.arguments?.getString("groupName") ?: "No Group"

            // Pass the groupName and fusedLocationClient to HomeContent
            HomeContent(fusedLocationClient = fusedLocationClient, groupName = groupName, navController = navController)
        }

    }
}
