package com.auth0.trackinapp.auth

import RegisterRequest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Canvas
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.auth0.trackinapp.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    val blueishColor = colorResource(id = R.color.blueish)

    // Smooth button animation using animateDpAsState
    val buttonOffset by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Button Slide Animation"
    )

    // LaunchedEffect to trigger animations
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
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { -50 }),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = blueishColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                listOf(
                    "Email" to email,
                    "Password" to password,
                    "Confirm Password" to confirmPassword,
                    "Phone Number" to phoneNumber
                ).forEach { (label, value) ->
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { -50 }),
                        exit = fadeOut(animationSpec = tween(300))                    ) {
                        TextField(
                            value = value,
                            onValueChange = {
                                when (label) {
                                    "Email" -> email = it
                                    "Password" -> password = it
                                    "Confirm Password" -> confirmPassword = it
                                    "Phone Number" -> phoneNumber = it
                                }
                            },
                            label = { Text(label) },
                            visualTransformation = if (label.contains("Password")) PasswordVisualTransformation() else VisualTransformation.None,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = blueishColor,
                                unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                            ),
                            textStyle = TextStyle(color = Color.Black)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { -50 }),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    Button(
                        onClick = {
                            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
                                errorMessage = "All fields are required."
                                return@Button
                            }
                            if (password != confirmPassword) {
                                errorMessage = "Passwords do not match."
                                return@Button
                            }
                            isLoading = true
                            val request = RegisterRequest(email, password, confirmPassword, phoneNumber)
                            RetrofitService.api.signUp(request).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    isLoading = false
                                    if (response.isSuccessful) {
                                        navController.navigate("LoginScreen") {
                                            popUpTo("RegisterScreen") { inclusive = true }
                                        }
                                    } else {
                                        errorMessage = "Registration failed: ${response.errorBody()?.string() ?: response.message()}"
                                    }
                                }
                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    isLoading = false
                                    errorMessage = "Error: ${t.localizedMessage}"
                                }
                            })
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Sign Up", color = Color.White)
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController.navigate("LoginScreen") })
                {
                    Text(
                        "Already have an account? Login",
                        style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.light_sea_green))
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen(navController = rememberNavController())
    }
}
