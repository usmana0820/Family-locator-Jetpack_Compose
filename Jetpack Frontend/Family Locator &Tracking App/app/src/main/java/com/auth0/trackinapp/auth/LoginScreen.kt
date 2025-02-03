package com.auth0.trackinapp.auth

import androidx.compose.animation.AnimatedVisibility

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.auth0.trackinapp.R
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
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

                drawContext.canvas.nativeCanvas.apply {
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 60f
                        textAlign = android.graphics.Paint.Align.CENTER
                        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                    }
                    drawText("Family Locator", size.width / 2, size.height * 0.1f, textPaint)
                }
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
                        text = "Login",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = blueishColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { 40 })
                    ) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
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

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { 40 })
                    ) {
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = blueishColor,
                                unfocusedIndicatorColor = colorResource(id = R.color.light_sea_green)
                            ),
                            textStyle = TextStyle(color = Color.Black)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { 50 })
                    ) {
                        Button(
                            onClick = {
                                if (email.text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                                    errorMessage = "Invalid email format"
                                    return@Button
                                }
                                if (password.text.length < 6) {
                                    errorMessage = "Password must be at least 6 characters"
                                    return@Button
                                }

                                isLoading = true
                                loginViewModel.login(
                                    email.text,
                                    password.text,
                                    onSuccess = { loginResponse ->
                                        isLoading = false
                                        navController.navigate("group_management/${loginResponse.access}")
                                    },
                                    onError = {
                                        isLoading = false
                                        errorMessage = it
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = blueishColor)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Login",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { 30 })
                    ) {
                        TextButton(onClick = { navController.navigate("Register") }) {
                            Text(
                                text = "Don't have an account? Register",
                                style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.light_sea_green))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { 30 })
                    ) {
                        TextButton(onClick = { navController.navigate("forgot-password") }) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.light_sea_green))
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = errorMessage.isNotEmpty(),
                        enter = fadeIn(animationSpec = tween(500))
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }




                    Button(
                        onClick = { navController.navigate("home_screen/{groupName}") },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp)
                            //   .shadow(5.dp, shape = RoundedCornerShape(1.dp))
                        //colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
                    ) {
                        Text(text = "Home Screen", color = Color.White, fontWeight = FontWeight.Bold)
                    }


//
//                    Button(
//                        onClick = { navController.navigate("group_creation_screen/$token") },
//                        modifier = Modifier
//                            .fillMaxWidth(0.8f)
//                            .height(50.dp)
//                            //   .shadow(5.dp, shape = RoundedCornerShape(1.dp))
//                            .offset(y = buttonOffset.dp), // Slide up animation
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
//                    ) {
//                        Text(text = "Create Group", color = Color.White, fontWeight = FontWeight.Bold)
//                    }
//
//
//                    Button(
//                        onClick = { navController.navigate("group_creation_screen/$token") },
//                        modifier = Modifier
//                            .fillMaxWidth(0.8f)
//                            .height(50.dp)
//                            //   .shadow(5.dp, shape = RoundedCornerShape(1.dp))
//                            .offset(y = buttonOffset.dp), // Slide up animation
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
//                    ) {
//                        Text(text = "Create Group", color = Color.White, fontWeight = FontWeight.Bold)
//                    }


                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}


