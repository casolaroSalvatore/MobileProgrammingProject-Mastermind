package com.example.mastermind.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/* Tavolozza calda “wood & amber” */
private val LightColors = lightColorScheme(
    primary   = Color(0xFF8D6E63),
    onPrimary = Color.White,
    secondary = Color(0xFFFFB74D),
    onSecondary = Color.Black,
    background = Color.Transparent,
    surface    = Color.Transparent
)

private val DarkColors = darkColorScheme(
    primary   = Color(0xFFBCAAA4),
    onPrimary = Color.Black,
    secondary = Color(0xFFFFCC80),
    background = Color.Transparent,
    surface    = Color.Transparent
)

/* Tutte le “surface-like” trasparenti → il legno rimane visibile */
private fun transparentify(base: ColorScheme) = base.copy(
    surface        = Color.Transparent,
    surfaceVariant = Color.Transparent,
    background     = Color.Transparent,
    inverseSurface = Color.Transparent
)

@Composable
fun MastermindTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    content: @Composable () -> Unit
) {
    val baseScheme = when {
        dynamicColor -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme     -> DarkColors
        else          -> LightColors
    }
    val scheme = transparentify(baseScheme)

    /* Barra di stato e navigation bar in overlay trasparente */
    rememberSystemUiController()
        .setSystemBarsColor(Color.Transparent, darkIcons = !darkTheme)

    MaterialTheme(
        colorScheme = scheme,
        typography  = Typography(),
        shapes      = Shapes(),
        content     = content
    )
}


