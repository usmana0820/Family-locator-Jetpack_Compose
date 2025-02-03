package com.auth0.trackinapp.groupmanagement

import GroupDetailsResponse
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.auth0.trackinapp.R
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text



import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.auth0.trackinapp.auth.LoginScreen

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
//
//
//@Composable
//fun GroupManagementScreen(
//    navController: NavController,
//    token: String,
//    groupId: Int,
//    viewModel: GroupViewModel = viewModel()
//) {
//    var groupName by remember { mutableStateOf("") }
//    var userEmail by remember { mutableStateOf("") }
//    var userRole by remember { mutableStateOf("") }
//    var userStatus by remember { mutableStateOf("") }
//    var totalMembers by remember { mutableStateOf(0) }
//    var maxAllowedMembers by remember { mutableStateOf(0) }
//
//    val context = LocalContext.current
//
//    // Fetch Group Data
//    LaunchedEffect(Unit) {
//        viewModel.getGroupDetails(token, groupId, onSuccess = { group ->
//            groupName = group.groupName
//            userEmail = group.userEmail
//            userRole = group.userRole
//            userStatus = group.userStatus
//            totalMembers = group.totalMembers
//            maxAllowedMembers = group.maxAllowedMembers
//        }, onError = {
//            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//        })
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text("Group Management", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Group Details
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            elevation = 4.dp
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("Group Name: $groupName", fontWeight = FontWeight.Bold)
//                Text("User Email: $userEmail")
//                Text("User Role: $userRole")
//                Text("User Status: $userStatus")
//                Text("Total Members: $totalMembers / $maxAllowedMembers")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Action Buttons
//        Button(
//            onClick = { navController.navigate("SwitchGroupScreen") },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
//        ) {
//            Text("Switch Group")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Button(
//            onClick = { navController.navigate("AddMemberScreen/$token/$groupId") },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
//        ) {
//            Text("Add Member")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Button(
//            onClick = {
//                viewModel.run {
//                    leaveGroup(token, groupId, onSuccess = {
//                                Toast.makeText(context, "Left group successfully", Toast.LENGTH_SHORT).show()
//                                navController.popBackStack()
//                            }, onError = {
//                                Toast.makeText(context, "Failed to leave: $it", Toast.LENGTH_SHORT).show()
//                            })
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
//        ) {
//            Text("Leave Group")
//        }
//    }
//}



