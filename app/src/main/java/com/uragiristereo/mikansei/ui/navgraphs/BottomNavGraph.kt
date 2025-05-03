package com.uragiristereo.mikansei.ui.navgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.feature.home.favorites.favoritesBottomRoute
import com.uragiristereo.mikansei.feature.home.posts.core.postsBottomRoute
import com.uragiristereo.mikansei.feature.image.core.imageBottomRoute
import com.uragiristereo.mikansei.feature.saved_searches.savedSearchesBottomRoute
import com.uragiristereo.mikansei.feature.user.manage.core.manageBottomRoute

fun NavGraphBuilder.bottomNavGraph(
    mainNavController: NavHostController,
) {
    postsBottomRoute(mainNavController)

    manageBottomRoute(mainNavController)

    favoritesBottomRoute(mainNavController)

    savedSearchesBottomRoute(mainNavController)

    imageBottomRoute(mainNavController)
}
