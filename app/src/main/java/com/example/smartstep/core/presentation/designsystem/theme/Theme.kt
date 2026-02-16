package com.example.smartstep.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val ColorScheme = lightColorScheme(
    background = BackgroundColor,
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    tertiary = TertiaryColor,
    surface = SurfaceColor,
    surfaceContainer = SurfaceContainerColor,
    surfaceContainerHigh = SurfaceContainerHighColor,
    surfaceContainerHighest = SurfaceContainerHighestColor,
    surfaceContainerLow = SurfaceContainerLowColor,
)

@Composable
fun SmartStepTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}