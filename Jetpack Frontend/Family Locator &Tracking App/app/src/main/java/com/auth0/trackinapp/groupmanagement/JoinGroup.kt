package com.auth0.trackinapp.groupmanagement
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.auth0.trackinapp.ui.theme.TrackinAppTheme

class JoinGroup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                JoinGroup()
            }
        }
    }

    @SuppressLint("NotConstructor")
    @Composable
    fun JoinGroup() {
        var pinCode by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }

        fun joinGroup(pin: String) {
            // Simulate the logic to join the group
            if (pin == "123456") {
                message = "Joined the group successfully!"
            } else {
                message = "Invalid pin code!"
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Join an Existing Group", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = pinCode,
                onValueChange = { pinCode = it },
                label = { Text("Enter Group Pin Code") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { joinGroup(pinCode) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Join Group")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
