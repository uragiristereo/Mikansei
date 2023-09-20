package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.animation.translateYFadeIn
import com.uragiristereo.mikansei.core.ui.animation.translateYFadeOut
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.image.ImageScreen
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.imageRoute(
    navController: NavHostController,
    navigatedBackByGesture: State<Boolean>,
    onNavigatedBackByGesture: (Boolean) -> Unit,
) {
    composable<MainRoute.Image>(
        enterTransition = {
            translateYFadeIn(
                initialOffsetY = { it / 5 },
                durationMillis = 350,
            )
        },
        popExitTransition = {
            Timber.d("navigatedBackByGesture ${navigatedBackByGesture.value}")
            when {
                navigatedBackByGesture.value -> fadeOut()
                else -> translateYFadeOut(
                    targetOffsetY = { it / 5 },
                    durationMillis = 350,
                )
            }
        },
    ) {
        ImageScreen(
            onNavigateBack = {
                onNavigatedBackByGesture(it)

                navController.navigateUp()
            },
            onNavigateToAddToFavGroup = { post ->
                navController.navigate(HomeRoute.AddToFavGroup(post))
            }
        )
    }
}
