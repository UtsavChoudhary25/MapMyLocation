package com.example.mapmylocation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = CardWhite,
    primaryContainer = SageGreenLight,
    onPrimaryContainer = ForestGreen,
    secondary = SageGreen,
    onSecondary = CharcoalText,
    secondaryContainer = SageGreenLight,
    onSecondaryContainer = CharcoalText,
    tertiary = Terracotta,
    onTertiary = CardWhite,
    background = WarmIvory,
    onBackground = CharcoalText,
    surface = CardWhite,
    onSurface = CharcoalText,
    surfaceVariant = WarmIvory,
    onSurfaceVariant = MutedText,
    outline = BorderColor
)

@Composable
fun MapMyLocationTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = WarmIvory.toArgb()
            window.navigationBarColor = WarmIvory.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
