package com.auth0.trackinapp.groupmanagement

import android.content.ClipData
import android.os.Bundle
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auth0.trackinapp.ApiService
import com.auth0.trackinapp.CreateGroupRequest
import com.auth0.trackinapp.CreateGroupResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.LightColorScheme

class CreateGroup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateGroupActivity(navController = rememberNavController())
        }
    }
}

@Composable
fun CreateGroupActivity(navController: NavController = rememberNavController()) {
    var groupName by remember { mutableStateOf("") }
    var generatedPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var groupCreated by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Show Pin Dialog if group is created
    if (groupCreated) {
        ShowPinDialog(generatedPin = generatedPin, onDismiss = { groupCreated = false }, navController, groupName)
    }

    MaterialTheme(
        colorScheme = LightColorScheme // Use your custom color scheme here
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo and Name
                Image(
                    painter = painterResource(id = R.drawable.applogo), // Replace with your app logo resource
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp) // Set the size of the logo
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                if (!groupCreated) {
                    // UI for Group Creation
                    Text("Create a Group", style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Group Name Text Field with Border
                    OutlinedTextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = { Text("Group Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true
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
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error Message
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
@Composable
fun ShowPinDialog(generatedPin: String, onDismiss: () -> Unit, navController: NavController, groupName: String) {
    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipboardData = ClipData.newPlainText("Generated Pin", generatedPin)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Group Created!") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Your group has been created successfully!")
                Text("Generated Pin: $generatedPin")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        clipboard.setPrimaryClip(clipboardData)
                        Toast.makeText(context, "Pin copied to clipboard", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Copy Pin")
                    }

                    Button(onClick = {
                        val shareIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, "Join my group using the pin: $generatedPin")
                            type = "text/plain"
                        }
                        context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Pin"))
                    }) {
                        Text("Share Pin")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onDismiss() // Dismiss the dialog and navigate to HomeScreen
                navController.navigate("home_screen/$groupName") // Pass the groupName in the route
            }) {
                Text("OK")
            }
        }
    )
}

// API Calls
suspend fun createGroup(groupName: String, pinCode: String): Response<CreateGroupResponse> {
    val request = CreateGroupRequest(groupName, pinCode)
    return ApiService.instance.createGroup(request)
}

// Function to check if the group name already exists in the database
suspend fun checkIfGroupExists(groupName: String): Boolean {
    val response = ApiService.instance.checkGroupName(groupName)
    return response.isSuccessful && response.body()?.exists == true
}
