package com.auth0.trackinapp.settings

import Groupdetails
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.benchmark.perfetto.Row
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.auth0.trackinapp.groupmanagement.GroupViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.auth0.trackinapp.SharedPrefManager




@Composable
fun GroupSetting(navController: NavController, token: String, groupId: Int, viewModel: GroupViewModel = viewModel()) {
    //  val groupName = remember { mutableStateOf(SharedPrefManager.getSelectedGroupName() ?: "No Group Selected") }
    //  val groupId = remember { mutableStateOf(SharedPrefManager.getSelectedGroupId()) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Group Management",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0098c3)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Group Details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Text(text = "Current Group: ${groupName.value}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        // Text(text = "Group ID: ${groupId.value}", fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Buttons for different actions

                Button(
                    onClick = { navController.navigate("group_creation_screen/$token") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
                ) {
                    Text("Create Group", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("group_join_screen/$token") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
                ) {
                    Text("Join Group", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("edit_group_screen") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
                ) {
                    Text("Edit Group", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))


                Button(
                    onClick = { navController.navigate("All_Group_Details") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
                ) {
                    Text("Switch Groups", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("manage_members_screen") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
                ) {
                    Text("Manage Members", color = Color.White, fontSize = 18.sp)
                }


                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("leave_group_screen/$token/$groupId") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Leave Group", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("delete_group_screen/$token/$groupId") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete Group", color = Color.White, fontSize = 18.sp)
                }



            }
        }
    }
}
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun All_Group_Details(navController: NavController) {
    // Retrieve the token from SharedPreferences
    val token = SharedPrefManager.getToken()
    val groupViewModel = GroupViewModel()

    // Check if the token exists
    if (token != null) {
        // Fetch the groups' details using the token from SharedPreferences
        groupViewModel.fetchGroupDetails(token)
    } else {
        // If the token does not exist, show an error message or navigate to login
        Text(text = "User not logged in. Please login first.")
    }

    // Observe the group details in the view model
    val groupDetails by groupViewModel.groupDetails.observeAsState(emptyList())

    // UI displaying the fetched group details
    if (groupDetails.isEmpty()) {
        CircularProgressIndicator() // Show loading indicator if no data yet
    } else {
        LazyColumn {
            items(groupDetails) { group ->
                GroupItem(group = group, navController = navController)
            }
        }
    }
}

@Composable
fun GroupItem(group: Groupdetails, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                //SharedPrefManager.saveSelectedGroup(group.id, group.group_name)
                navController.navigate("GroupManagementSetting/{token}") // Navigate to GroupSetting
            },     //   elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Group id: ${group.id}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Group Name: ${group.group_name}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Max Members: ${group.max_members}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "PIN: ${group.pin}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Created By: ${group.created_by}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


//1️⃣ Edit Group Screen
@Composable
fun EditGroupScreen(navController: NavController) {
    var groupName by remember { mutableStateOf("") }
    var maxMembers by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Edit Group", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0098c3))
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = maxMembers,
                onValueChange = { maxMembers = it },
                label = { Text("Max Members") },
                modifier = Modifier.fillMaxWidth(),
             //   keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Save Changes */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
            ) {
                Text("Save Changes", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    }
}

//2️⃣ Remove Group Confirmation
@Composable
fun RemoveGroupScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Delete Group", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Are you sure you want to delete this group?", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Delete Group */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Confirm Delete", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    }
}
// 3️⃣ Manage Members Screen
@Composable
fun ManageMembersScreen(navController: NavController) {
    val members = listOf("User1", "User2", "User3", "Admin1")  // Dummy list

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Manage Members", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0098c3))
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(members) { member ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = member, fontSize = 18.sp)
                            Row {
                                IconButton(onClick = { /* Make Admin */ }) {
                                    Icon(Icons.Default.Person, contentDescription = "Make Admin", tint = Color.Blue)
                                }
                                IconButton(onClick = { /* Remove User */ }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove User", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Back", color = Color.White)
            }
        }
    }
}


//4️⃣ Switch Groups Screen
@Composable
fun SwitchGroupsScreen(navController: NavController) {
    val groups = listOf("Work Group", "Family Group", "Friends Group")  // Dummy list

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Switch Groups", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0098c3))
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(groups) { group ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = group, fontSize = 18.sp)
                            Button(onClick = { /* Switch Group */ }) {
                                Text("Switch", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Back", color = Color.White)
            }
        }
    }
}

// 5️⃣ Leave Group Confirmation
@Composable
fun LeaveGroupScreen(navController: NavController, token: String, groupId: Int, viewModel: GroupViewModel = viewModel()) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Are you sure you want to leave the group?", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.leaveGroup(
                        token = token,
                        groupId = groupId,
                        onSuccess = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Leave Group", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text("Cancel", color = Color.Black)
            }
        }
    }
}

// Delete Group Screen
@Composable
fun DeleteGroupScreen(navController: NavController, token: String, groupId: Int, viewModel: GroupViewModel = viewModel()) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Are you sure you want to delete this group?", fontSize = 20.sp, color = Color.Red)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.deleteGroup(
                        token = token,
                        groupId = groupId,
                        onSuccess = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            navController.navigate("group_creation_screen/$token") {
                                popUpTo("group_setting_screen") { inclusive = true }
                            }
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete Group", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text("Cancel", color = Color.Black)
            }
        }
    }
}


