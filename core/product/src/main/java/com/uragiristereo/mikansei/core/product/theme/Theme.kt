package com.uragiristereo.mikansei.core.product.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource


enum class Theme {
    SYSTEM,
    LIGHT,
    DARK,
}

private val LightColorPalette = lightColors(
    primary = ProductPrimaryLight,
    primaryVariant = ProductPrimaryLight,
    secondary = ProductPrimaryLight,
    secondaryVariant = ProductPrimaryLight,
//    secondary = ProductSecondaryLight,
//    secondaryVariant = ProductSecondaryDark,
    background = BackgroundLight,
    surface = BackgroundLight,
    onSurface = TextLight,
    onBackground = TextLight,
)

private val DarkColorPalette = darkColors(
    primary = ProductPrimaryDark,
    primaryVariant = ProductPrimaryDark,
//    secondary = ProductSecondaryDark,
//    secondaryVariant = ProductSecondaryLight,
    secondary = ProductPrimaryDark,
    secondaryVariant = ProductPrimaryDark,
    background = BackgroundDark,
    surface = BackgroundDark,
    onSurface = TextDark,
    onBackground = TextDark,
)

private val BlackColorPalette = darkColors(
    primary = ProductPrimaryDark,
    primaryVariant = ProductPrimaryLight,
//    secondary = ProductSecondaryDark,
//    secondaryVariant = ProductSecondaryLight,
    secondary = ProductPrimaryDark,
    secondaryVariant = ProductPrimaryDark,
    background = BackgroundBlack,
    surface = BackgroundBlack,
    onSurface = TextDark,
    onBackground = TextDark,
)

@Composable
fun MikanseiTheme(
    theme: Theme = Theme.SYSTEM,
    blackTheme: Boolean = false,
    monetEnabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val darkColors = when {
        blackTheme -> BlackColorPalette
        else -> DarkColorPalette
    }

    val colors = when (theme) {
        Theme.SYSTEM ->
            when {
                isSystemInDarkTheme() -> darkColors
                else -> LightColorPalette
            }

        Theme.LIGHT -> LightColorPalette
        Theme.DARK -> darkColors
    }

    val monetColors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && monetEnabled -> {
            val primary200 = colorResource(android.R.color.system_accent1_200)
            val primary300 = colorResource(android.R.color.system_accent1_300)
            val primary500 = colorResource(android.R.color.system_accent1_500)
            val primary700 = colorResource(android.R.color.system_accent1_700)

            when {
                colors.isLight -> colors.copy(
                    primary = primary500,
                    primaryVariant = primary700,
                    secondary = primary300,
                    secondaryVariant = primary700,
                )

                else -> colors.copy(
                    primary = primary200,
                    primaryVariant = primary700,
                    secondary = primary200,
                    secondaryVariant = primary200,
                )
            }
        }

        else -> colors
    }

    MaterialTheme(
        colors = monetColors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
