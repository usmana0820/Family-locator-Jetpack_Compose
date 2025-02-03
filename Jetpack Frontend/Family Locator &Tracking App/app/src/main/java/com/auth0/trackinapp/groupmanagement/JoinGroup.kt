package com.auth0.trackinapp.groupmanagement


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.data.Group
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.foundation.Canvas
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.auth0.trackinapp.R
import kotlinx.coroutines.delay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupJoinScreen(navController: NavController, token: String, groupViewModel: GroupViewModel = viewModel()) {
    var pin by remember { mutableStateOf(TextFieldValue("")) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val blueishColor = colorResource(id = R.color.blueish)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, size.height * 0.3f)
                    cubicTo(
                        size.width / 1,
                        size.height * 0.1f,
                        size.width,
                        size.height * 0.3f,
                        size.width,
                        0f
                    )
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path = path, color = blueishColor)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Join Group",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = blueishColor,
                        fontWeight = FontWeight.Bold
                    )
                )
                TextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("Group PIN") },
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)), // Rounded corners for text field
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = blueishColor,
                        unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        groupViewModel.joinGroup(
                            token,
                            pin.text.toInt(),
                            onSuccess = {
                                successMessage = "Joined group: ${it.group_name}"
                                navController.navigate("home_screen/${it.group_name}") // Pass group name
                            },
                            onError = {
                                errorMessage = it
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(blueishColor) // Custom button color
                ) {
                    Text("Join Group", color = Color.White)
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
        }
    }
}
