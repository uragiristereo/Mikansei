package com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.ModalBottomSheetState2
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.BottomSheetNavigator.Index
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState2
import com.uragiristereo.mikansei.core.ui.navigation.NavRoute
import com.uragiristereo.mikansei.core.ui.navigation.routeOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import timber.log.Timber

val LocalBottomSheetNavigator = staticCompositionLocalOf<BottomSheetNavigator> { error("No LocalBottomSheetNavigator provided!") }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetNavigator(
    navController: NavHostController,
    bottomSheetState: ModalBottomSheetState2 = rememberModalBottomSheetState2(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    ),
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
        internal set

    @OptIn(ExperimentalMaterialApi::class)
    fun runHiding(block: suspend () -> Unit) {
        if (!bottomSheetState.isAnimationRunning) {
            coroutineScope.launch {
                bottomSheetState.hide()
                block()
            }
        }
    }

    fun navigate(
        popBackStack: Boolean = true,
        block: (NavHostController) -> Unit,
    ) {
        val currentBackStackEntry = navController.currentBackStackEntry

        if (!bottomSheetState.isAnimationRunning) {
            coroutineScope.launch(SupervisorJob()) {
                isNavigating = true

                if (currentBackStackEntry?.destination?.hasRoute<Index>() != true) {
                    bottomSheetState.hide()

                    if (popBackStack) {
                        navController.popBackStack()
                    }
                }

                block(navController)

                coroutineScope.launch(SupervisorJob()) {
                    if (currentBackStackEntry?.destination?.hasRoute<Index>() != true) {
                        delay(timeMillis = 500L)
                    }

                    expand()
                }
            }
        }
    }

    internal suspend fun expand() {
        try {
            Timber.d("expanding")
            bottomSheetState.expand()
        } catch (_: CancellationException) {
            Timber.d("expand got cancelled, trying to expand again")
            bottomSheetState.expand()
        }
    }

    companion object {
        fun indexRoute(builder: NavGraphBuilder) {
            builder.composable<Index> {
                Spacer(modifier = Modifier.size(1.dp))
            }
        }
    }

    @Serializable
    data object Index : NavRoute
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InterceptBackGestureForBottomSheetNavigator() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val scope = rememberCoroutineScope()

    BackHandler(enabled = bottomSheetNavigator.bottomSheetState.isVisible) {
        val previousRoute = bottomSheetNavigator.navController.previousBackStackEntry?.destination?.id

        scope.launch(SupervisorJob()) {
            bottomSheetNavigator.isNavigating = true
            bottomSheetNavigator.bottomSheetState.hide()

            if (previousRoute !in listOf(null, routeOf<Index>())) {
                bottomSheetNavigator.navController.popBackStack()
                delay(timeMillis = 300L)
                bottomSheetNavigator.expand()
            }

            bottomSheetNavigator.isNavigating = false
        }
    }
}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigateToIndexWhenBottomSheetNavigatorHidden() {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = bottomSheetNavigator.bottomSheetState.isVisible) {
        if (bottomSheetNavigator.bottomSheetState.isVisible) {
            Timber.d("expanded")
            bottomSheetNavigator.isNavigating = false
        }
    }

    DisposableEffect(
        key1 = bottomSheetNavigator.bottomSheetState.isVisible,
        key2 = bottomSheetNavigator.isNavigating,
    ) {
        val job = scope.launch {
            if (!bottomSheetNavigator.bottomSheetState.isVisible && !bottomSheetNavigator.isNavigating) {
                Timber.d("popped")

                bottomSheetNavigator.navController.navigate(Index) {
                    popUpTo(id = 0)
                }
            }
        }

        onDispose {
            job.cancel()
        }
    }

    Spacer(modifier = Modifier.size(1.dp))
}
