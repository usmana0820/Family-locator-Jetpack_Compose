package com.auth0.trackinapp.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.auth0.trackinapp.ApiService
import com.auth0.trackinapp.CreateGroupRequest
import com.auth0.trackinapp.CreateGroupResponse
import com.auth0.trackinapp.VerifyPinRequest
import com.auth0.trackinapp.VerifyPinResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class GroupScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroupManage()
        }
    }
}

@Composable
fun GroupManage() {
    var groupName by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var generatedPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var groupCreated by remember { mutableStateOf(false) }
    var pinVerified by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!groupCreated) {
            // UI for Group Creation
            Text("Create a Group", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = groupName,
                onValueChange = { groupName = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    // Check if group name already exists in the database
                    val isGroupExists = checkIfGroupExists(groupName)
                    if (isGroupExists) {
                        errorMessage = "Group name already exists. Please choose another name."
                        return@launch
                    }

                    // Generate a dynamic pin
                    generatedPin = (1000..9999).random().toString()

                    try {
                        // Call API to create group
                        val response = createGroup(groupName, generatedPin)
                        if (response.isSuccessful) {
                            groupCreated = true
                            println("Group created successfully with pin: $generatedPin")
                        } else {
                            errorMessage = "Failed to create group. Please try again."
                        }
                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                    }
                }
            }) {
                Text("Create Group")
            }
        } else if (!pinVerified) {
            // UI for Pin Verification
            Text("Enter Pin to Join Group", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = pin,
                onValueChange = { pin = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    try {
                        // Call API to verify pin
                        val response = verifyPin(groupName, pin)
                        if (response.isSuccessful) {
                            pinVerified = true
                            // Navigate to Home Screen using Intent
                         //   val intent = Intent(context, HomeScreen::class.java).apply {
                         //       putExtra("GROUP_NAME", groupName)
                          //  }
                         //   context.startActivity(intent)
                        } else {
                            errorMessage = "Invalid pin. Please try again."
                        }
                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                    }
                }
            }) {
                Text("Verify Pin")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

// API Calls
suspend fun createGroup(groupName: String, pinCode: String): Response<CreateGroupResponse> {
    val request = CreateGroupRequest(groupName, pinCode)
    return ApiService.instance.createGroup(request)
}

suspend fun verifyPin(groupName: String, pinCode: String): Response<VerifyPinResponse> {
    val request = VerifyPinRequest(groupName, pinCode)
    return ApiService.instance.verifyPin(request)
}

// Function to check if the group name already exists in the database
suspend fun checkIfGroupExists(groupName: String): Boolean {
    // Simulate an API call to check if the group name exists
    // Replace with actual API call to verify group name availability
    val response = ApiService.instance.checkGroupName(groupName)
    return response.isSuccessful && response.body()?.exists == true
}
