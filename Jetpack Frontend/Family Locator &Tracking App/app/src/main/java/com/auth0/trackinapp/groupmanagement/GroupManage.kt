package com.auth0.trackinapp.groupmanagement


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*

@Composable
fun GroupManagementActivity(navController: NavController, token: String) {
    var isVisible by remember { mutableStateOf(false) }

    // Animation for buttons sliding up
    val buttonOffset by animateFloatAsState(
        targetValue = if (isVisible) 0f else 100f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Button Slide Animation"
    )

    // Trigger animation after composition
    LaunchedEffect(Unit) {
        delay(300) // Small delay before starting animation
        isVisible = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
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
                drawPath(
                    path = path,
                    color = Color(0xFF0098c3)
                )

                drawContext.canvas.nativeCanvas.apply {
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 60f
                        textAlign = android.graphics.Paint.Align.CENTER
                        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                    }
                    drawText(
                        "",
                        size.width / 2,
                        size.height * 0.1f,
                        textPaint
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Group Management",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color(0xFF0098c3),
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Gray,
                            offset = Offset(4f, 4f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Create Group Button with Slide Animation
                Button(
                    onClick = { navController.navigate("group_creation_screen/$token") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                     //   .shadow(5.dp, shape = RoundedCornerShape(1.dp))
                        .offset(y = buttonOffset.dp), // Slide up animation
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0098c3))
                ) {
                    Text(text = "Create Group", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Divider Between Buttons
                Divider(modifier = Modifier.fillMaxWidth(0.7f).height(1.dp), color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                // Join Group Button with Slide Animation
                Button(
                    onClick = { navController.navigate("group_join_screen/$token") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(55.dp)
                      //  .shadow(5.dp, shape = RoundedCornerShape(1.dp))
                        .offset(y = buttonOffset.dp), // Slide up animation
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF55a7a6))
                ) {
                    Text(text = "Join Group", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GroupManagementActivityPreview() {
    MaterialTheme {
        GroupManagementActivity(navController = rememberNavController(), token = "token")
    }
}
