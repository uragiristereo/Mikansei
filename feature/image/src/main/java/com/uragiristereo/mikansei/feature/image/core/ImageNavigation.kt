package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.animation.fadeOut
import androidx.compose.runtime.State
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.animation.translateYFadeIn
import com.uragiristereo.mikansei.core.ui.animation.translateYFadeOut
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import com.uragiristereo.mikansei.feature.image.ImageScreen

fun NavGraphBuilder.imageRoute(
    navController: NavHostController,
    navigatedBackByGesture: State<Boolean>,
    onNavigatedBackByGesture: (Boolean) -> Unit,
) {
    composable<MainRoute.Image>(
        typeMap = PostNavType,
        enterTransition = {
            translateYFadeIn(
                initialOffsetY = { it / 5 },
                durationMillis = 350,
            )
        },
        popExitTransition = {
            when {
                navigatedBackByGesture.value -> fadeOut()
                else -> translateYFadeOut(
                    targetOffsetY = { it / 5 },
                    durationMillis = 350,
                )
            }
        },
    ) {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        ImageScreen(
            onNavigateBack = {
                onNavigatedBackByGesture(it)

                navController.navigateUp()
            },
            onNavigateToAddToFavGroup = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.AddToFavGroup(post))
                }
            }
        )
    }
}
