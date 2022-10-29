package com.android.crazy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Material 3 color schemes
private val crazyDarkColorScheme = darkColorScheme(
    primary = crazyDarkPrimary,
    onPrimary = crazyDarkOnPrimary,
    primaryContainer = crazyDarkPrimaryContainer,
    onPrimaryContainer = crazyDarkOnPrimaryContainer,
    inversePrimary = crazyDarkPrimaryInverse,
    secondary = crazyDarkSecondary,
    onSecondary = crazyDarkOnSecondary,
    secondaryContainer = crazyDarkSecondaryContainer,
    onSecondaryContainer = crazyDarkOnSecondaryContainer,
    tertiary = crazyDarkTertiary,
    onTertiary = crazyDarkOnTertiary,
    tertiaryContainer = crazyDarkTertiaryContainer,
    onTertiaryContainer = crazyDarkOnTertiaryContainer,
    error = crazyDarkError,
    onError = crazyDarkOnError,
    errorContainer = crazyDarkErrorContainer,
    onErrorContainer = crazyDarkOnErrorContainer,
    background = crazyDarkBackground,
    onBackground = crazyDarkOnBackground,
    surface = crazyDarkSurface,
    onSurface = crazyDarkOnSurface,
    inverseSurface = crazyDarkInverseSurface,
    inverseOnSurface = crazyDarkInverseOnSurface,
    surfaceVariant = crazyDarkSurfaceVariant,
    onSurfaceVariant = crazyDarkOnSurfaceVariant,
    outline = crazyDarkOutline
)

private val crazyLightColorScheme = lightColorScheme(
    primary = crazyLightPrimary,
    onPrimary = crazyLightOnPrimary,
    primaryContainer = crazyLightPrimaryContainer,
    onPrimaryContainer = crazyLightOnPrimaryContainer,
    inversePrimary = crazyLightPrimaryInverse,
    secondary = crazyLightSecondary,
    onSecondary = crazyLightOnSecondary,
    secondaryContainer = crazyLightSecondaryContainer,
    onSecondaryContainer = crazyLightOnSecondaryContainer,
    tertiary = crazyLightTertiary,
    onTertiary = crazyLightOnTertiary,
    tertiaryContainer = crazyLightTertiaryContainer,
    onTertiaryContainer = crazyLightOnTertiaryContainer,
    error = crazyLightError,
    onError = crazyLightOnError,
    errorContainer = crazyLightErrorContainer,
    onErrorContainer = crazyLightOnErrorContainer,
    background = crazyLightBackground,
    onBackground = crazyLightOnBackground,
    surface = crazyLightSurface,
    onSurface = crazyLightOnSurface,
    inverseSurface = crazyLightInverseSurface,
    inverseOnSurface = crazyLightInverseOnSurface,
    surfaceVariant = crazyLightSurfaceVariant,
    onSurfaceVariant = crazyLightOnSurfaceVariant,
    outline = crazyLightOutline
)

@Composable
fun CrazyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val crazyColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> crazyDarkColorScheme
        else -> crazyLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = crazyColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = crazyColorScheme,
        typography = crazyTypography,
        shapes = shapes,
        content = content
    )
}