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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.auth0.trackinapp.R
import com.auth0.trackinapp.SharedPrefManager
import kotlinx.coroutines.delay

@Composable
fun WaveHeader2() {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupJoinScreen(
    navController: NavController,
    token: String,
    groupViewModel: GroupViewModel = viewModel()
) {
    var pin by remember { mutableStateOf(TextFieldValue("")) }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    SharedPrefManager.init(context)

    val blueishColor = colorResource(id = R.color.blueish)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 🌊 Call the reusable wave header
            WaveHeader2()

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

                Spacer(modifier = Modifier.height(16.dp))

                // PIN TextField (with numeric keyboard and black text)
                TextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("Group PIN", color = Color.Black) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Join Group Button
                Button(
                    onClick = {
                        groupViewModel.joinGroup(
                            token,
                            pin.text.toIntOrNull() ?: 0,
                            onSuccess = { group ->
                                successMessage = "Joined group: ${group.group_name}"
                                SharedPrefManager.saveToken(token)
                                navController.navigate("home_screen/${group.group_name}/$token")
                            },
                            onError = { error ->
                                errorMessage = error
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                ) {
                    Text("Join Group", color = Color.White)
                }

                // Success and Error Messages
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
