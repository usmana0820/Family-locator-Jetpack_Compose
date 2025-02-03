package com.auth0.trackinapp.settings

import GroupDetailsResponse
import GroupMember
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun GroupSetting(navController: NavController, token: String, groupId: Int, viewModel: GroupViewModel = viewModel()) {
//    var groupName by remember { mutableStateOf("My Group") }
//    var maxMembers by remember { mutableStateOf(10) }
//
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = Color.White
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "Group Management",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF0098c3)
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Group Details
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(12.dp),
//                    //elevation = 4.dp
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp),
//                        horizontalAlignment = Alignment.Start
//                    ) {
//                        Text(text = "Group Name: $groupName", fontSize = 18.sp)
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(text = "Max Members: $maxMembers", fontSize = 18.sp)
//                    }
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//
//                // Buttons for different actions
//                Button(
//                    onClick = { /* Navigate to Create Group Screen */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
//                ) {
//                    Text("Create Another Group", color = Color.White, fontSize = 18.sp)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Button(
//                    onClick = { /* Open Dialog to Enter PIN and Join Group */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
//                ) {
//                    Text("Join Another Group", color = Color.White, fontSize = 18.sp)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Button(
//                    onClick = { /* Edit Group Details */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
//                ) {
//                    Text("Edit Group", color = Color.White, fontSize = 18.sp)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Button(
//                    onClick = { /* Remove Group */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor= Color.Red)
//                ) {
//                    Text("Delete Group", color = Color.White, fontSize = 18.sp)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Button(
//                    onClick = { /* Manage Members */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
//                ) {
//                    Text("Manage Members", color = Color.White, fontSize = 18.sp)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Button(
//                    onClick = { /* Switch Groups */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
//                ) {
//                    Text("Switch Groups", color = Color.White, fontSize = 18.sp)
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Button(
//                    onClick = { /* Leave Group */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
//                ) {
//                    Text("Leave Group", color = Color.White, fontSize = 18.sp)
//                }
//            }
//        }
//    }
//}

@Composable
fun GroupSetting(
    navController: NavController,
    token: String,
    groupId: Int
) {
    val groupViewModel: GroupViewModel = viewModel()
    var groupDetailsResponse by remember { mutableStateOf<GroupDetailsResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch group details on screen load
    LaunchedEffect(key1 = groupId) {
        isLoading = true
        groupViewModel.getGroupDetails(
            groupId = groupId,
            token = token,
            onSuccess = { response ->
                groupDetailsResponse = response
                isLoading = false
            },
            onError = { error ->
                errorMessage = error
                isLoading = false
            }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            groupDetailsResponse?.let { groupDetails ->
                Text(text = "Group Name: ${groupDetails.group_name}", style = MaterialTheme.typography.headlineLarge)
                Text(text = "Max Members: ${groupDetails.max_members}")
                Text(text = "Current Members: ${groupDetails.current_member_count}")

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "Members:", style = MaterialTheme.typography.headlineLarge)
                LazyColumn {
                    items(groupDetails.members) { member ->
                        MemberItem(member = member)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { /* Navigate to Update Role Screen */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Update Member Roles")
                }

                Button(
                    onClick = {
                        // Handle leaving group action
                        groupViewModel.leaveGroup(groupId, token, onSuccess = {
                            // Handle success
                        }, onError = {
                            // Handle failure
                        })
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text(text = "Leave Group")
                }

            }

            errorMessage?.let {
                Text(text = "Error: $it", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
fun MemberItem(member: GroupMember) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Username: ${member.user_name}")
        Text(text = "Email: ${member.user_email}")
        Text(text = "Role: ${member.role}")
    }
}