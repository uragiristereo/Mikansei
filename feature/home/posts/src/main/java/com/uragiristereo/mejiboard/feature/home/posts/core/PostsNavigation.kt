package com.uragiristereo.mejiboard.feature.home.posts.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mejiboard.core.ui.LocalNavigationRailPadding
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.feature.home.posts.PostsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.postsRoute(
    navController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
) {
    composable(
        route = HomeRoute.Posts(),
        content = { data ->
            LaunchedEffect(key1 = data.tags) {
                onCurrentTagsChange(data.tags)
            }

            PostsScreen(
                onNavigate = navController::navigate,
                onNavigateImage = remember {
                    { item ->
                        onNavigatedBackByGesture(false)

                        navController.navigate(
                            route = MainRoute.Image(post = item),
                        )
                    }
                },
                modifier = Modifier.padding(start = LocalNavigationRailPadding.current),
            )
        },
    )
}
