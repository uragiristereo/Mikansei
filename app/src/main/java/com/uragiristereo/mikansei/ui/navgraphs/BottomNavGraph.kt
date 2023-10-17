package com.uragiristereo.mikansei.ui.navgraphs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.BottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.NavigateToIndexWhenBottomSheetNavigatorHidden
import com.uragiristereo.mikansei.feature.home.favorites.core.favoritesBottomRoute
import com.uragiristereo.mikansei.feature.home.posts.core.postsBottomRoute
import com.uragiristereo.mikansei.feature.user.manage.core.manageBottomRoute
import com.uragiristereo.mikansei.ui.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BottomNavGraph(
    mainNavController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
) {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    NavigateToIndexWhenBottomSheetNavigatorHidden()

    NavHost(
        navController = bottomSheetNavigator.navController,
        startDestination = BottomSheetNavigator.INDEX_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier.fillMaxWidth(),
    ) {
        BottomSheetNavigator.indexRoute(builder = this)

        postsBottomRoute(
            mainNavController = mainNavController,
            onNavigatedBackByGesture = viewModel::setNavigatedBackByGesture,
        )

        manageBottomRoute(mainNavController)

        favoritesBottomRoute(mainNavController)
    }
}
