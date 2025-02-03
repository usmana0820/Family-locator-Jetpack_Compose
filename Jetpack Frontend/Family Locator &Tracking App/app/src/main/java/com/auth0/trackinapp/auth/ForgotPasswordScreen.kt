@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.auth0.trackinapp.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    val blueishColor = colorResource(id = R.color.blueish)

    // Smooth button animation
    val buttonOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Button Slide Animation"
    )

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, size.height * 0.3f)
                    cubicTo(size.width / 1, size.height * 0.1f, size.width, size.height * 0.3f, size.width, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path = path, color = blueishColor)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .offset(y = buttonOffset), // Smooth slide animation
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically()
                ) {
                    Text(
                        text = "Forgot Password",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = blueishColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Enter your email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = blueishColor,
                        unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically()
                ) {
                    Button(
                        onClick = {
                            val request = ForgotPasswordRequest(email)
                            RetrofitService.api.forgotPassword(request).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        successMessage = "Reset link sent to your email."
                                    } else {
                                        errorMessage = "Failed to send reset link: ${response.message()}"
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    errorMessage = "Error: ${t.localizedMessage}"
                                }
                            })
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                    ) {
                        Text("Send Reset Link", color = Color.White)
                    }
                }

                if (successMessage.isNotEmpty()) {
                    Text(successMessage, color = MaterialTheme.colorScheme.primary)
                }
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}


@Composable
fun ResetPasswordScreen(navController: NavHostController, token: String) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    val blueishColor = colorResource(id = R.color.blueish)

    // Smooth button animation
    val buttonOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Button Slide Animation"
    )

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, size.height * 0.3f)
                    cubicTo(size.width / 1, size.height * 0.1f, size.width, size.height * 0.3f, size.width, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path = path, color = blueishColor)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .offset(y = buttonOffset), // Smooth slide animation
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically()
                ) {
                    Text(
                        text = "Reset Password",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = blueishColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = blueishColor,
                        unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = blueishColor,
                        unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically()
                ) {
                    Button(
                        onClick = {
                            val request = ResetPasswordRequest(token, newPassword, confirmPassword)
                            RetrofitService.api.resetPassword(token, request).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        successMessage = "Password reset successfully!"
                                        navController.navigate("LoginScreen")
                                    } else {
                                        errorMessage = "Failed to reset password: ${response.message()}"
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    errorMessage = "Error: ${t.localizedMessage}"
                                }
                            })
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                    ) {
                        Text("Reset Password", color = Color.White)
                    }
                }

                if (successMessage.isNotEmpty()) {
                    Text(successMessage, color = MaterialTheme.colorScheme.primary)
                }
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(navController: NavHostController) {
    var token by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    val blueishColor = colorResource(id = R.color.blueish)

    // Smooth button animation
    val buttonOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Button Slide Animation"
    )

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.white)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, size.height * 0.3f)
                    cubicTo(size.width / 1, size.height * 0.1f, size.width, size.height * 0.3f, size.width, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path = path, color = blueishColor)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .offset(y = buttonOffset), // Smooth slide animation
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically()
                ) {
                    Text(
                        text = "Email Verification",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = blueishColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                TextField(
                    value = token,
                    onValueChange = { token = it },
                    label = { Text("Enter Verification Token") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = blueishColor,
                        unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                    ),
                    textStyle = TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically()
                ) {
                    Button(
                        onClick = {
                            RetrofitService.api.verifyEmail(token.text).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        navController.navigate("LoginScreen")
                                    } else {
                                        errorMessage = "Verification failed: ${response.message()}"
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    errorMessage = "Error: ${t.localizedMessage}"
                                }
                            })
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                    ) {
                        Text("Verify Email", color = Color.White)
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
