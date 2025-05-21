@file:OptIn(ExperimentalMaterial3Api::class)

package com.auth0.trackinapp.groupmanagement

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.auth0.trackinapp.R
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.ContextCompat.getSystemService
import com.auth0.trackinapp.SharedPrefManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaveHeader1(modifier: Modifier = Modifier, color: Color) {
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
fun GroupCreationScreen(navController: NavController, token: String, groupViewModel: GroupViewModel = viewModel()) {
    var groupName by remember { mutableStateOf(TextFieldValue("")) }
    var maxMembers by remember { mutableStateOf(TextFieldValue("")) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var generatedPin by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isMaxMembersValid by remember { mutableStateOf(true) }

    val context = LocalContext.current
    SharedPrefManager.init(context)

    val blueishColor = colorResource(id = R.color.blueish)

    fun isValidNumber(value: String): Boolean = value.all { it.isDigit() }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Generated Pin", text)
        clipboard?.setPrimaryClip(clip)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 🌊 Wave Header
            WaveHeader1(
                modifier = Modifier.fillMaxSize(),
                color = blueishColor
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Group",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = blueishColor,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Group Name Field
                TextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = { Text("Group Name", color = Color.Black) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = blueishColor,
                        unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Max Members Field with Numeric Keyboard
                TextField(
                    value = maxMembers,
                    onValueChange = {
                        if (isValidNumber(it.text)) {
                            maxMembers = it
                            isMaxMembersValid = true
                        } else {
                            isMaxMembersValid = false
                        }
                    },
                    label = { Text("Max Members", color = Color.Black) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = blueishColor,
                        unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )

                if (!isMaxMembersValid) {
                    Text(
                        text = "Please enter a valid number",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Create Group Button
                Button(
                    onClick = {
                        if (isMaxMembersValid) {
                            groupViewModel.createGroup(
                                token,
                                groupName.text,
                                maxMembers.text.toInt(),
                                onSuccess = { group ->
                                    generatedPin = group.pin.toString()
                                    successMessage = "Group created successfully"
                                    showDialog = true
                                    SharedPrefManager.saveToken(token)
                                    navController.navigate("home_screen/${group.group_name}/$token")
                                },
                                onError = {
                                    errorMessage = it
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                ) {
                    Text("Create Group", color = Color.White)
                }

                if (successMessage.isNotEmpty()) {
                    Text(
                        text = successMessage,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            // PIN Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Group Pin Code", color = Color.Black) },
                    text = {
                        Column {
                            TextField(
                                value = generatedPin,
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Gray,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                textStyle = TextStyle(color = Color.Black),
                                enabled = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    copyToClipboard(context, generatedPin)
                                    Toast.makeText(context, "PIN copied!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                            ) {
                                Text("Copy Pin", color = Color.White)
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                navController.navigate("home_screen/{groupName}/{token}")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                        ) {
                            Text("OK", color = Color.White)
                        }
                    },
                    containerColor = Color.White,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}









//
//        @Composable
//        fun GroupEditScreen(
//            navController: NavController,
//            token: String,
//            groupId: Int,
//            groupViewModel: GroupViewModel = viewModel()
//        ) {
//            var groupName by remember { mutableStateOf(TextFieldValue("")) }
//            var maxMembers by remember { mutableStateOf(TextFieldValue("")) }
//            var successMessage by remember { mutableStateOf("") }
//            var errorMessage by remember { mutableStateOf("") }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("Edit Group", style = MaterialTheme.typography.headlineLarge)
//                Spacer(modifier = Modifier.height(16.dp))
//
//                TextField(
//                    value = groupName,
//                    onValueChange = { groupName = it },
//                    label = { Text("Group Name") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//
//                TextField(
//                    value = maxMembers,
//                    onValueChange = { maxMembers = it },
//                    label = { Text("Max Members") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        groupName.text.takeIf { it.isNotEmpty() }?.let {
//                            maxMembers.text.toIntOrNull()?.let { it1 ->
//                                groupViewModel.editGroup(
//                                    token,
//                                    groupId,
//                                    it,
//                                    it1,
//                                    onSuccess = {
//                                        successMessage = "Group updated: ${it.group_name}"
//                                        navController.popBackStack() // Go back to previous screen
//                                    },
//                                    onError = {
//                                        errorMessage = it
//                                    }
//                                )
//                            }
//                        }
//                    }
//                ) {
//                    Text("Update Group")
//                }
//
//                if (successMessage.isNotEmpty()) {
//                    Text(
//                        text = successMessage,
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.padding(top = 16.dp)
//                    )
//                }
//
//                if (errorMessage.isNotEmpty()) {
//                    Text(
//                        text = errorMessage,
//                        color = MaterialTheme.colorScheme.error,
//                        modifier = Modifier.padding(top = 16.dp)
//                    )
//                }
//            }
//        }

//        @Composable
//        fun GroupLeaveScreen(
//            navController: NavController,
//            token: String,
//            groupId: Int,
//            groupViewModel: GroupViewModel = viewModel()
//        ) {
//            var successMessage by remember { mutableStateOf("") }
//            var errorMessage by remember { mutableStateOf("") }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("Leave Group", style = MaterialTheme.typography.headlineLarge)
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        groupViewModel.leaveGroup(
//                            token,
//                            groupId,
//                            onSuccess = {
//                                successMessage = "You have successfully left the group."
//                            },
//                            onError = {
//                                errorMessage = it
//                            }
//                        )
//                    }
//                ) {
//                    Text("Leave Group")
//                }
//
//                if (successMessage.isNotEmpty()) {
//                    Text(
//                        text = successMessage,
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.padding(top = 16.dp)
//                    )
//                }
//
//                if (errorMessage.isNotEmpty()) {
//                    Text(
//                        text = errorMessage,
//                        color = MaterialTheme.colorScheme.error,
//                        modifier = Modifier.padding(top = 16.dp)
//                    )
//                }
//            }
//        }
//
