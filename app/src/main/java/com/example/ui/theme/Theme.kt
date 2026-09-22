package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = IpakLime,
    onPrimary = IpakDeepNavy,
    primaryContainer = IpakNavyLight,
    onPrimaryContainer = IpakLimeLight,
    secondary = IpakLimeLight,
    onSecondary = IpakDeepNavy,
    background = IpakDeepNavy,
    surface = IpakNavy,
    onSurface = IpakSurface,
    onBackground = IpakSurface,
    surfaceVariant = IpakNavyLight,
    onSurfaceVariant = IpakTextMuted,
    outline = IpakNavyLight,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = IpakNavy,
    onPrimary = IpakSurface,
    primaryContainer = IpakLightBg,
    onPrimaryContainer = IpakNavy,
    secondary = IpakLime,
    onSecondary = IpakNavy,
    secondaryContainer = IpakLimeLight.copy(alpha = 0.35f),
    onSecondaryContainer = IpakNavy,
    tertiary = IpakLimeDark,
    background = IpakLightBg,
    onBackground = IpakTextPrimary,
    surface = IpakSurface,
    onSurface = IpakTextPrimary,
    surfaceVariant = IpakBorderLight,
    onSurfaceVariant = IpakTextSecondary,
    outline = IpakBorder,
    error = IpakEmergencyRed,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color by default to strictly preserve authentic IPAK brand colors
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
