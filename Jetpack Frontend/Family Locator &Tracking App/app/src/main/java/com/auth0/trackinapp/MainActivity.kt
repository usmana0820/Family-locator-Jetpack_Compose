package com.auth0.trackinapp

import EmailVerificationScreen
import ForgotPasswordScreen
import ResetPasswordScreen
import SettingsScreen
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
import com.auth0.trackinapp.settings.All_Group_Details
import com.auth0.trackinapp.settings.DeleteGroupScreen
import com.auth0.trackinapp.settings.EditGroupScreen
import com.auth0.trackinapp.settings.GroupSetting
import com.auth0.trackinapp.settings.LeaveGroupScreen
import com.auth0.trackinapp.settings.ManageMembersScreen
import com.auth0.trackinapp.settings.RemoveGroupScreen
import com.auth0.trackinapp.settings.SwitchGroupsScreen
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
                Surface {
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
        composable("splash_screen") {  SplashScreen(navController) }
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




        composable("GroupManagementSetting/{token}", arguments = listOf(navArgument("token") { type = NavType.StringType })) { backStackEntry ->
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
        composable("All_Group_Details") { All_Group_Details(navController) }

        composable("edit_group_screen") { EditGroupScreen(navController) }
        composable("manage_members_screen") { ManageMembersScreen(navController) }
        composable("switch_groups_screen") { SwitchGroupsScreen(navController) }
//        composable("leave_group_screen") { LeaveGroupScreen(navController) }
//        composable("delete_group_screen") { DeleteGroupScreen(navController) }

        composable("remove_group_screen") { RemoveGroupScreen(navController) }




//        // Edit Group Screen
//        composable("edit_group_screen/{token}/{groupId}",
//            arguments = listOf(
//                navArgument("token") { type = NavType.StringType },
//                navArgument("groupId") { type = NavType.IntType }
//            )
//        ) { backStackEntry ->
//            val token = backStackEntry.arguments?.getString("token") ?: ""
//            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
//            EditGroupScreen(navController = navController, token = token, groupId = groupId)
//        }
//
//// All Group Details (Switch Groups)
//        composable("All_Group_Details/{token}",
//            arguments = listOf(navArgument("token") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val token = backStackEntry.arguments?.getString("token") ?: ""
//            AllGroupDetailsScreen(navController = navController, token = token)
//        }
//
//// Manage Members
//        composable("manage_members_screen/{token}/{groupId}",
//            arguments = listOf(
//                navArgument("token") { type = NavType.StringType },
//                navArgument("groupId") { type = NavType.IntType }
//            )
//        ) { backStackEntry ->
//            val token = backStackEntry.arguments?.getString("token") ?: ""
//            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
//            ManageMembersScreen(navController = navController, token = token, groupId = groupId)
//        }

// Leave Group
        composable("leave_group_screen/{token}/{groupId}",
            arguments = listOf(
                navArgument("token") { type = NavType.StringType },
                navArgument("groupId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 7
            LeaveGroupScreen(navController = navController, token = token, groupId = groupId)
        }

// Delete Group
        composable("delete_group_screen/{token}/{groupId}",
            arguments = listOf(
                navArgument("token") { type = NavType.StringType },
                navArgument("groupId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 7
            DeleteGroupScreen(navController = navController, token = token, groupId = groupId)
        }

//        composable("group_edit_screen/{group_id}") { backStackEntry ->
//            val groupId = backStackEntry.arguments?.getString("group_id")?.toInt() ?: 0
//            GroupEditScreen(navController = navController, groupId = groupId, token = token)
//        }
//        composable("group_leave_screen/{group_id}") { backStackEntry ->
//            val groupId = backStackEntry.arguments?.getString("group_id")?.toInt() ?: 0
//            GroupLeaveScreen(navController = navController, groupId = groupId, token = "your_token_here")
//        }

        composable("home_screen/{groupName}/{token}") { backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName") ?: "No Group"
            val token = backStackEntry.arguments?.getString("token") ?: ""
            // Pass the groupName and token to the HomeContent composable
            HomeContent(fusedLocationClient = fusedLocationClient, groupName = groupName, navController = navController, token = token)
        }


        composable("SettingsScreen/{token}", arguments = listOf(navArgument("token") { type = NavType.StringType })) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            SettingsScreen(navController = navController, token = token)
        }

        composable("subscription_screen") { SubscriptionScreen(navController) }


//        composable("home_screen/{groupName}/{username}") { backStackEntry ->
//            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
//            val username = backStackEntry.arguments?.getString("username") ?: ""
//            HomeScreen(navController, groupName, username)
//        }


    }
}




//

//
//
//






      // composable("home") { HomeScreen(navController) }


