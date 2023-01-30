package com.uragiristereo.mejiboard.ui.appbars

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute

@Stable
enum class MainNavigationItems(
    val route: NavRoute,
    @StringRes val label: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int = unselectedIcon,
) {
    Posts(
        route = HomeRoute.Posts(),
        label = R.string.posts_label,
        unselectedIcon = R.drawable.home,
        selectedIcon = R.drawable.home_fill,
    ),
    Search(
        route = MainRoute.Search(),
        label = R.string.search_label,
        unselectedIcon = R.drawable.search,
    ),
    Collections(
        route = HomeRoute.Collections,
        label = R.string.collections_label,
        unselectedIcon = R.drawable.photo_library,
        selectedIcon = R.drawable.photo_library_fill,
    ),
    More(
        route = HomeRoute.More,
        label = R.string.more_label,
        unselectedIcon = R.drawable.more_horiz,
    ),
}
