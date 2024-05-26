package com.uragiristereo.mikansei.core.ui.dfm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

fun interface DynamicComposable {
    @Composable
    fun Render(navController: NavHostController)
}

@Composable
fun DynamicComposable(
    module: String,
    screen: String,
    navController: NavHostController,
) {
    val instance = remember {
        val modulePascalCased = module
            .split('_')
            .joinToString("") { word ->
                word.replaceFirstChar { char -> char.uppercase() }
            }

        val screenPascalCased = screen.replaceFirstChar { it.uppercase() }

        Class.forName("com.uragiristereo.mikansei.feature.$module.${modulePascalCased}ModuleKt")
            .getMethod("get$screenPascalCased")
            .invoke(null) as DynamicComposable
    }

    instance.Render(navController)
}
