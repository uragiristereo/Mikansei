package com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetState3
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Composable
fun rememberBottomSheetNavigator(
    navController: NavHostController = rememberNavController(),
    sheetState: SheetState3 = rememberModalBottomSheetState3(
        skipDismissToPartiallyExpanded = true,
    ),
): BottomSheetNavigator {
    val scope = rememberCoroutineScope()

    return remember(navController, sheetState) {
        BottomSheetNavigator(
            navController = navController,
            sheetState = sheetState,
            coroutineScope = scope,
        )
    }
}

@Stable
class BottomSheetNavigator internal constructor(
    internal val navController: NavHostController,
    internal val sheetState: SheetState3,
    internal val coroutineScope: CoroutineScope,
) {
    fun runHiding(block: suspend () -> Unit) {
        coroutineScope.launch {
            sheetState.hide()
            block()
        }
    }

    fun navigate(
        popBackStack: Boolean = true,
        block: (navController: NavHostController) -> Unit,
    ) {
        coroutineScope.launch(SupervisorJob()) {
            val currentBackStackEntry = navController.currentBackStackEntry
            val isCurrentBackStackEntryIndex =
                currentBackStackEntry?.destination?.route == INDEX_ROUTE

            if (!isCurrentBackStackEntryIndex && !sheetState.isVisible) {
                return@launch
            }

            if (sheetState.isVisible) {
                sheetState.hideTemporarily()
            }

            if (popBackStack && !isCurrentBackStackEntryIndex) {
                navController.popBackStack()
            }

            block(navController)

            if (!sheetState.isVisible) {
                sheetState.expand()
            }
        }
    }

    suspend fun hideSheet() {
        sheetState.hide()
    }
}

@Composable
fun InterceptBackGestureForBottomSheetNavigator() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val navController = bottomSheetNavigator.navController
    val sheetState = bottomSheetNavigator.sheetState
    val scope = bottomSheetNavigator.coroutineScope

    var enabled by remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        enabled = true

        onPauseOrDispose {
            enabled = false
        }
    }

    if (enabled) {
        BackHandler {
            val previousRoute = navController.previousBackStackEntry?.destination?.id
            val indexId = navController.getBackStackEntry(INDEX_ROUTE).destination.id

            if (!sheetState.isAnimationRunning) {
                scope.launch(SupervisorJob()) {
                    if (previousRoute !in listOf(null, indexId)) {
                        sheetState.hideTemporarily()
                        navController.popBackStack()
                        sheetState.expand()
                    } else {
                        sheetState.hide()
                    }
                }
            }
        }
    }
}

@Composable
internal fun NavigateToIndexWhenBottomSheetNavigatorHidden() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val sheetState = bottomSheetNavigator.sheetState
    val navController = bottomSheetNavigator.navController

    LaunchedEffect(key1 = sheetState.shouldVisible) {
        if (!sheetState.shouldVisible) {
            navController.navigate(INDEX_ROUTE) {
                popUpTo(id = 0)
            }
        }
    }
}

@Composable
fun Modifier.bottomSheetContentPadding(): Modifier {
    return then(Modifier.padding(LocalBottomSheetContentPadding.current))
}

internal const val INDEX_ROUTE = "bottom-sheet-navigator-index"
