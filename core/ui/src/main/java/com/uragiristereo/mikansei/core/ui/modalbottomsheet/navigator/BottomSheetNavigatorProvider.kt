package com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.ModalBottomSheet3

val LocalBottomSheetNavigator = staticCompositionLocalOf<BottomSheetNavigator> { error("No LocalBottomSheetNavigator provided!") }
val LocalBottomSheetContentPadding = compositionLocalOf { PaddingValues() }

@Composable
fun ProvideBottomSheetNavigator(
    navGraphBuilder: NavGraphBuilder.() -> Unit,
    content: @Composable () -> Unit,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = bottomSheetNavigator.navController
    val sheetState = bottomSheetNavigator.sheetState

    val navGraphContent = remember {
        movableContentOf {
            NavigateToIndexWhenBottomSheetNavigatorHidden()

            NavHost(
                navController = navController,
                startDestination = INDEX_ROUTE,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.fillMaxWidth(),
            ) {
                composable(INDEX_ROUTE) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )
                }

                navGraphBuilder()
            }
        }
    }

    CompositionLocalProvider(LocalBottomSheetNavigator provides bottomSheetNavigator) {
        content()

        Box(modifier = Modifier.size(0.dp)) {
            if (!sheetState.shouldVisible) {
                navGraphContent()
            }
        }

        ModalBottomSheet3(sheetState = sheetState) { contentPadding ->
            CompositionLocalProvider(LocalBottomSheetContentPadding provides contentPadding) {
                navGraphContent()
            }
        }
    }
}
