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
import kotlin.random.Random

class CreateGroup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                CreateGroup()
            }
        }
    }

    @SuppressLint("NotConstructor")
    @Composable
    fun CreateGroup() {
        var groupName by remember { mutableStateOf("") }
        var pinCode by remember { mutableStateOf("") }

        fun generatePinCode(): String {
            return Random.nextInt(100000, 999999).toString()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create New Group", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    pinCode = generatePinCode()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Group")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (pinCode.isNotEmpty()) {
                Text("Your Group Pin Code: $pinCode", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
