package com.auth0.trackinapp.settings

import Groupdetails
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.auth0.trackinapp.SharedPrefManager
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.auth0.trackinapp.R

@Composable
fun WaveHeader() {
    val blueishColor = Color(0xFF0098C3)
    val lightBlue = Color(0xFFE0F7FA)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(blueishColor, lightBlue)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.75f)
                cubicTo(
                    size.width * 0.25f, size.height,
                    size.width * 0.75f, size.height * 0.5f,
                    size.width, size.height * 0.75f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(path = path, color = blueishColor)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Group Management",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}

@Composable
fun GroupSetting(navController: NavController, token: String, groupId: Int, viewModel: GroupViewModel = viewModel() ){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            WaveHeader()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 260.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Text(
//                    text = "Group Management",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = colorResource(id = R.color.blueish)
//                )
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
                        // Add group details here
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Buttons for different actions
                val buttonModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)

                Button(
                    onClick = { navController.navigate("group_creation_screen/$token") },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueish))
                ) {
                    Text("Create Group", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.navigate("group_join_screen/$token") },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueish))
                ) {
                    Text("Join Group", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.navigate("edit_group_screen") },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueish))
                ) {
                    Text("Edit Group", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.navigate("All_Group_Details") },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueish))
                ) {
                    Text("Group details", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.navigate("switch_groups_screen") },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blueish))
                ) {
                    Text("Switch group", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.navigate("leave_group_screen/$token/$groupId") },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Leave Group", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.navigate("delete_group_screen/$token/$groupId") },
                    modifier = buttonModifier,
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
    val token = SharedPrefManager.getToken()
    val groupViewModel = remember { GroupViewModel() }

    // Observe the group details from the ViewModel
    val groupDetails by groupViewModel.groupDetails.observeAsState(emptyList())

    // Fetch group details if token is available
    LaunchedEffect(token) {
        token?.let {
            groupViewModel.fetchGroupDetails(it)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // WaveHeader at the top
            WaveHeader()

            // Content below the header
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    token == null -> {
                        Text(
                            text = "User not logged in. Please login first.",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    groupDetails.isEmpty() -> {
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.blueish)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(groupDetails, key = { it.id }) { group ->
                                GroupItem(group = group, navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupItem(group: Groupdetails, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clickable {
                navController.navigate("GroupManagementSetting/${group.id}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0098C3)) // Using the bluish theme color
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Group ID: ${group.id}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text("Group Name: ${group.group_name}", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Max Members: ${group.max_members}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text("PIN: ${group.pin}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text("Created By: ${group.created_by}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroupScreen(navController: NavController) {
    var groupName by remember { mutableStateOf("") }
    var maxMembers by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Wave header at the top
            WaveHeader()

            // Form fields below the header
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 260.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit Group",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blueish)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = { Text("Group Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .animateContentSize(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = colorResource(id = R.color.blueish),
                        focusedBorderColor = colorResource(id = R.color.blueish),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.blueish),
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusRequester.requestFocus()
                        }
                    )
                )


                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = maxMembers,
                    onValueChange = { maxMembers = it },
                    label = { Text("Max Members") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedLabelColor = colorResource(id = R.color.blueish),
                        focusedBorderColor = colorResource(id = R.color.blueish),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.blueish),
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Save Changes */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.blueish)
                    )
                ) {
                    Text("Save Changes", color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text("Cancel", color = Color.White)
                }
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
                .padding(top = 260.dp, start = 16.dp, end = 16.dp),
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
    WaveHeader()
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
            Text("Manage Members", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.blueish))
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
            Text("Switch Groups", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.blueish))
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


