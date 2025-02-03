package com.auth0.trackinapp

import EmailVerificationScreen
import ForgotPasswordScreen
import ResetPasswordScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier // Import the Modifier here
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.auth0.trackinapp.auth.LoginScreen
import com.auth0.trackinapp.auth.RegisterScreen
import com.auth0.trackinapp.groupmanagement.GroupCreationScreen
import com.auth0.trackinapp.groupmanagement.GroupJoinScreen
import com.auth0.trackinapp.groupmanagement.GroupManagementActivity
import com.auth0.trackinapp.screens.*
import com.auth0.trackinapp.settings.GroupSetting
import com.auth0.trackinapp.settings.SettingsScreen
//import com.auth0.trackinapp.settings.SettingsActivity
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            TrackinAppTheme {
                Surface(
                   // modifier = Modifier.fillMaxSize(), // Use Modifier here
                  //  color = MaterialTheme.colorScheme.background
                ) {
                    NavigationHost(fusedLocationClient = fusedLocationClient)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(fusedLocationClient: FusedLocationProviderClient) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash_screen") {
        composable("splash_screen") { SplashActivity(navController) }
        //        composable("welcome_screen") { WelcomeActivity(navController) }
        //        composable("languages_screen") { LanguagesActivity(navController) }

        composable("Register") { RegisterScreen(navController) }
        composable("LoginScreen") { LoginScreen(navController) }

     //   composable("verify-email") { EmailVerificationScreen(navController) }
        composable("forgot-password") { ForgotPasswordScreen(navController) }
        composable("reset-password/{token}") { backStackEntry -> val token = backStackEntry.arguments?.getString("token") ?: ""
            ResetPasswordScreen(navController, token) }


        composable("group_management/{token}", arguments = listOf(navArgument("token") { type = NavType.StringType })) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            GroupManagementActivity(navController = navController, token = token)
        }


//        composable("group_management/{token}/{groupId}", arguments = listOf(navArgument("token") { type = NavType.StringType }, navArgument("groupId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val token = backStackEntry.arguments?.getString("token") ?: ""
//            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
//
//            // Call GroupManagementScreen with the necessary arguments
//            GroupManagementScreen(navController = navController, token = token, groupId = groupId)
//        }


        composable("GroupManagementScreen/{token}", arguments = listOf(navArgument("token") { type = NavType.StringType })) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 27
            GroupSetting(navController = navController, token = token, groupId = groupId)
        }
        composable("group_creation_screen/{token}", arguments = listOf(navArgument("token") { type = NavType.StringType })) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            GroupCreationScreen(navController = navController, token = token)
        }
        composable("group_join_screen/{token}", arguments = listOf(navArgument("token") { type = NavType.StringType })) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            GroupJoinScreen(navController = navController, token = token)
        }
//        composable("group_edit_screen/{group_id}") { backStackEntry ->
//            val groupId = backStackEntry.arguments?.getString("group_id")?.toInt() ?: 0
//            GroupEditScreen(navController = navController, groupId = groupId, token = token)
//        }
//        composable("group_leave_screen/{group_id}") { backStackEntry ->
//            val groupId = backStackEntry.arguments?.getString("group_id")?.toInt() ?: 0
//            GroupLeaveScreen(navController = navController, groupId = groupId, token = "your_token_here")
//        }

         composable("home_screen/{groupName}") { backStackEntry ->
        // Retrieve the group name from the arguments
            val groupName = backStackEntry.arguments?.getString("groupName") ?: "No Group"
            // Pass the groupName and fusedLocationClient to HomeContent
          HomeContent(fusedLocationClient = fusedLocationClient, groupName = groupName, navController = navController)
         }

        composable("SettingsScreen/{token}", arguments = listOf(navArgument("token") { type = NavType.StringType })) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            SettingsScreen(navController = navController, token = token)
        }
//        composable("SettingsScreen") {
//            SettingsScreen(navController = navController, token = token)
//        }

//        composable("home_screen/{groupName}/{username}") { backStackEntry ->
//            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
//            val username = backStackEntry.arguments?.getString("username") ?: ""
//            HomeScreen(navController, groupName, username)
//        }

     //   composable("settings_screen") { SettingsActivity(navController) }



    }
}




//

//
//
//






      // composable("home") { HomeScreen(navController) }


