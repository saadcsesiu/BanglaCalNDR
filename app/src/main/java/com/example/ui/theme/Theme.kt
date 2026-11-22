package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val OneUiDarkColorScheme = darkColorScheme(
    primary = OneUiBlue,
    onPrimary = OneUiTextPrimary,
    primaryContainer = OneUiBlueVariant,
    onPrimaryContainer = OneUiTextPrimary,
    secondary = OneUiBlueLight,
    onSecondary = OneUiBlack,
    secondaryContainer = OneUiCardVariant,
    onSecondaryContainer = OneUiTextPrimary,
    tertiary = OneUiAlertAmber,
    onTertiary = OneUiBlack,
    background = OneUiBlack,
    onBackground = OneUiTextPrimary,
    surface = OneUiCardDark,
    onSurface = OneUiTextPrimary,
    surfaceVariant = OneUiCardVariant,
    onSurfaceVariant = OneUiTextSecondary,
    outline = OneUiBorder
)

@Composable
fun BanglaCalendarTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = OneUiBlack.toArgb()
                window.navigationBarColor = OneUiBlack.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = OneUiDarkColorScheme,
        typography = BanglaTypography,
        content = content
    )
}
