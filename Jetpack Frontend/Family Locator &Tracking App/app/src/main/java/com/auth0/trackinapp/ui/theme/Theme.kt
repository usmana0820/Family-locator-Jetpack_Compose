package com.auth0.trackinapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = Blue200,
    onPrimary = Blue700,
    secondary = Teal700,
    onSecondary = Blue200,
    background = Blue700,
    onBackground = White,
    surface = Blue500,
    onSurface = White,
    error = Purple700,
    onError = White
)

val LightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = White,
    secondary = Teal200,
    onSecondary = Blue500,
    background = White,
    onBackground = Blue700,
    surface = White,
    onSurface = Blue700,
    error = Purple500,
    onError = White
)

@Composable
fun TrackinAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // Ensure you have Typography defined somewhere in your project
        content = content
    )
}
