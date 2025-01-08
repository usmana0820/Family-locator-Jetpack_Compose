//LoginScreen.kt
package com.auth0.trackinapp.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.auth0.trackinapp.ApiService
import com.auth0.trackinapp.R
import com.auth0.trackinapp.TokenRequest
import com.auth0.trackinapp.ui.theme.TrackinAppTheme
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackinAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Login() {
    var user by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result -> user = result.user },
        onAuthError = { user = null }
    )
    val token = stringResource(id = R.string.web_client_id)
    val context = LocalContext.current

    // Navigate to the HomeScreen if the user is logged in
    user?.let {
        LaunchedEffect(Unit) {
            val intent = Intent(context, GroupScreen::class.java)
            context.startActivity(intent)
            (context as? ComponentActivity)?.finish() // Close login screen activity

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center, // Center content vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    ) {
        // App Logo and Name
        Image(
            painter = painterResource(id = R.drawable.applogo), // Replace with your app logo resource
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp) // Set the size of the logo
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF0070AC), // Your theme color
            modifier = Modifier.padding(bottom = 32.dp),
            fontFamily = FontFamily.SansSerif
        )

        // Google Sign-In Button
        if (user == null) {
            ElevatedButton(
                onClick = {
                    val gso = GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0070AC),
                    contentColor = White
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.continue_with_google),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (Exception) -> Unit // Updated to handle all exceptions
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    return rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            // Get the signed-in Google account
            val account = task.getResult(ApiException::class.java)!!
            val tokenId = account.idToken!!

            // Log the token ID
            Log.d("GoogleAuth", "Token ID: $tokenId")

            // Create Google credentials
            val credential = GoogleAuthProvider.getCredential(tokenId, null)

            // Launch a coroutine for Firebase sign-in
            scope.launch {
                try {
                    // Sign in with Firebase using Google credentials
                    val authResult = Firebase.auth.signInWithCredential(credential).await()

                    // Log success
                    Log.d("GoogleAuth", "Firebase Authentication Success")

                    // Send token to backend
                    val response = ApiService.instance.verifyToken(TokenRequest(tokenId))

                    if (response.isSuccessful) {
                        // Handle successful backend response
                        val userResponse = response.body()
                        userResponse?.let {
                            Log.d("GoogleAuth", "Token successfully sent to backend: ${it.name}, ${it.email}")
                        } ?: run {
                            Log.e("GoogleAuth", "Response body is null")
                        }
                    } else {
                        // Log backend error
                        Log.e("GoogleAuth", "Error sending token to backend: ${response.errorBody()?.string()}")
                    }

                    // Trigger onAuthComplete callback
                    onAuthComplete(authResult)
                } catch (e: Exception) {
                    // Log Firebase authentication error
                    Log.e("GoogleAuth", "Authentication failed: ${e.message}")
                    onAuthError(e) // Handle any exception, not just ApiException
                }
            }
        } catch (e: ApiException) {
            // Log Google Sign-In error
            Log.e("GoogleAuth", "Google Sign-In failed: ${e.message}")
            onAuthError(e)
        } catch (e: Exception) {
            // Log unexpected errors
            Log.e("GoogleAuth", "Unexpected error during sign-in: ${e.message}")
            onAuthError(e)
        }
    }
}
