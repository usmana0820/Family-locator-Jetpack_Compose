package com.auth0.trackinapp.groupmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auth0.trackinapp.ApiService
import com.auth0.trackinapp.R
import com.auth0.trackinapp.VerifyPinRequest
import com.auth0.trackinapp.VerifyPinResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class JoinGroup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoinGroupActivity(navController = rememberNavController())
        }
    }
}

@Composable
fun JoinGroupActivity(navController: NavController = rememberNavController()) {
    var groupName by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo and Name at the top
        Image(painter = painterResource(id = R.drawable.applogo), contentDescription = "App Logo", modifier = Modifier.size(120.dp))
        Text("TrackinApp", style = MaterialTheme.typography.labelLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Group Name Field with Border
        Text("Enter Group Name", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = groupName,
            onValueChange = { groupName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .border(1.dp, Color.Gray)
                .padding(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pin Code Field with Border
        Text("Enter Pin Code", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = pinCode,
            onValueChange = { pinCode = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .border(1.dp, Color.Gray)
                .padding(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Success Message
        if (successMessage.isNotEmpty()) {
            Text(successMessage, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        // Verify Pin with backend
                        val response = ApiService.instance.verifyPin(VerifyPinRequest(groupName, pinCode))
                        if (response.isSuccessful) {
                            val verifyPinResponse = response.body()
                            if (verifyPinResponse?.success == true) {
                                successMessage = "You have successfully joined the group!"
                                // Navigate to Home or Group Dashboard
                                navController.navigate("home_screen/${groupName}")
                            } else {
                                errorMessage = "Invalid Pin. Please try again."
                            }
                        } else {
                            errorMessage = "Failed to verify pin. Please try again."
                        }
                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Join Group", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
