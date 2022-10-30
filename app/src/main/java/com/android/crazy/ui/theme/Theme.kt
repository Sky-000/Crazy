package com.android.crazy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Material 3 color schemes
private val crazyLightColors = lightColorScheme(
    primary = crazyLightPrimary,
    onPrimary = crazyLightOnPrimary,
    primaryContainer = crazyLightPrimaryContainer,
    onPrimaryContainer = crazyLightOnPrimaryContainer,
    secondary = crazyLightSecondary,
    onSecondary = crazyLightOnSecondary,
    secondaryContainer = crazyLightSecondaryContainer,
    onSecondaryContainer = crazyLightOnSecondaryContainer,
    tertiary = crazyLightTertiary,
    onTertiary = crazyLightOnTertiary,
    tertiaryContainer = crazyLightTertiaryContainer,
    onTertiaryContainer = crazyLightOnTertiaryContainer,
    error = crazyLightError,
    errorContainer = crazyLightErrorContainer,
    onError = crazyLightOnError,
    onErrorContainer = crazyLightOnErrorContainer,
    background = crazyLightBackground,
    onBackground = crazyLightOnBackground,
    surface = crazyLightSurface,
    onSurface = crazyLightOnSurface,
    surfaceVariant = crazyLightSurfaceVariant,
    onSurfaceVariant = crazyLightOnSurfaceVariant,
    outline = crazyLightOutline,
    inverseOnSurface = crazyLightInverseOnSurface,
    inverseSurface = crazyLightInverseSurface,
    inversePrimary = crazyLightInversePrimary,
    surfaceTint = crazyLightSurfaceTint,
    outlineVariant = crazyLightOutlineVariant,
    scrim = crazyLightScrim,
)


private val crazyDarkColors = darkColorScheme(
    primary = crazyDarkPrimary,
    onPrimary = crazyDarkOnPrimary,
    primaryContainer = crazyDarkPrimaryContainer,
    onPrimaryContainer = crazyDarkOnPrimaryContainer,
    secondary = crazyDarkSecondary,
    onSecondary = crazyDarkOnSecondary,
    secondaryContainer = crazyDarkSecondaryContainer,
    onSecondaryContainer = crazyDarkOnSecondaryContainer,
    tertiary = crazyDarkTertiary,
    onTertiary = crazyDarkOnTertiary,
    tertiaryContainer = crazyDarkTertiaryContainer,
    onTertiaryContainer = crazyDarkOnTertiaryContainer,
    error = crazyDarkError,
    errorContainer = crazyDarkErrorContainer,
    onError = crazyDarkOnError,
    onErrorContainer = crazyDarkOnErrorContainer,
    background = crazyDarkBackground,
    onBackground = crazyDarkOnBackground,
    surface = crazyDarkSurface,
    onSurface = crazyDarkOnSurface,
    surfaceVariant = crazyDarkSurfaceVariant,
    onSurfaceVariant = crazyDarkOnSurfaceVariant,
    outline = crazyDarkOutline,
    inverseOnSurface = crazyDarkInverseOnSurface,
    inverseSurface = crazyDarkInverseSurface,
    inversePrimary = crazyDarkInversePrimary,
    surfaceTint = crazyDarkSurfaceTint,
    outlineVariant = crazyDarkOutlineVariant,
    scrim = crazyDarkScrim,
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
        darkTheme -> crazyDarkColors
        else -> crazyLightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = crazyColorScheme.inverseOnSurface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            window.navigationBarColor = crazyColorScheme.secondaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = crazyColorScheme,
        typography = crazyTypography,
        shapes = shapes,
        content = content
    )
}