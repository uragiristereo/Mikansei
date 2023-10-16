package com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.ModalBottomSheetState2
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

val LocalBottomSheetNavigator = staticCompositionLocalOf<BottomSheetNavigator> { error("No LocalBottomSheetNavigator provided!") }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetNavigator(
    navController: NavHostController,
    bottomSheetState: ModalBottomSheetState2 = rememberModalBottomSheetState2(initialValue = ModalBottomSheetValue.Hidden),
): BottomSheetNavigator {
    val scope = rememberCoroutineScope()

    return remember {
        BottomSheetNavigator(
            bottomSheetState = bottomSheetState,
            navController = navController,
            coroutineScope = scope,
        )
    }
}

@Stable
@OptIn(ExperimentalMaterialApi::class)
class BottomSheetNavigator(
    val bottomSheetState: ModalBottomSheetState2,
    val navController: NavHostController,
    private val coroutineScope: CoroutineScope,
) {
    var isNavigating by mutableStateOf(false)
        private set

    @OptIn(ExperimentalMaterialApi::class)
    fun runHiding(block: suspend () -> Unit) {
        if (!bottomSheetState.isAnimationRunning) {
            coroutineScope.launch {
                bottomSheetState.hide()
                block()
            }
        }
    }

    fun navigate(block: (NavHostController) -> Unit) {
        val currentBackStackEntry = navController.currentBackStackEntry
        val currentRoute = currentBackStackEntry?.destination?.route

        if (!bottomSheetState.isAnimationRunning) {
            coroutineScope.launch {
                if (currentRoute != "index") {
                    bottomSheetState.hide()
                    navController.popBackStack()
                }

                isNavigating = true

                block(navController)

                if (currentRoute != "index") {
                    delay(timeMillis = 500L)
                }

                isNavigating = false

                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
            }
        }
    }

    companion object {
        const val INDEX_ROUTE = "index"

        @OptIn(ExperimentalAnimationApi::class)
        fun indexRoute(builder: NavGraphBuilder) {
            builder.composable(route = INDEX_ROUTE) {
                Spacer(modifier = Modifier.size(1.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InterceptBackGestureForBottomSheetNavigator() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val scope = rememberCoroutineScope()

    BackHandler(enabled = bottomSheetNavigator.bottomSheetState.isVisible) {
        scope.launch {
            bottomSheetNavigator.bottomSheetState.hide()
        }
    }
}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigateToIndexWhenBottomSheetNavigatorHidden() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val isCurrentValueHidden by remember {
        derivedStateOf {
            bottomSheetNavigator.bottomSheetState.currentValue == ModalBottomSheetValue.Hidden
        }
    }

    val isTargetValueHidden by remember {
        derivedStateOf {
            bottomSheetNavigator.bottomSheetState.targetValue == ModalBottomSheetValue.Hidden
        }
    }

    LaunchedEffect(
        key1 = isCurrentValueHidden,
        key2 = isTargetValueHidden,
        key3 = bottomSheetNavigator.isNavigating,
    ) {
        if (isCurrentValueHidden && isTargetValueHidden && !bottomSheetNavigator.isNavigating) {
            Timber.d("popped")

            bottomSheetNavigator.navController.navigate(BottomSheetNavigator.INDEX_ROUTE) {
                popUpTo(id = 0)
            }
        }
    }

    Spacer(modifier = Modifier.size(1.dp))
}
