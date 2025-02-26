package com.hackaton.sustaina.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,
    secondary = LeafyGreen,
    tertiary = NeonGreen,

    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),

    surfaceVariant = LightOlive,
    onSurfaceVariant = DarkGreen,

    onPrimary = SoftCream,
    onSecondary = NeonGreen,
    onTertiary = SoftCream,
    onBackground = SoftCream,
    onSurface = SoftCream
)

private val LightColorScheme = lightColorScheme(
    primary = LeafyGreen,
    secondary = LightOlive,
    tertiary = NeonGreen,

    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),

    surfaceVariant = LeafyGreen,
    onSurfaceVariant = SoftCream,

    onPrimary = DarkGreen,
    onSecondary = DarkGreen,
    onTertiary = DarkGreen,
    onBackground = Color(0xFF121212),
    onSurface = Color(0xFF121212)
)



@Composable
fun SustainaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
        typography = Typography,
        content = content
    )
}