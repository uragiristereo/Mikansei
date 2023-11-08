package com.uragiristereo.mikansei.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize

@Composable
fun MainContentResponsive(
    navController: NavHostController,
    navigationBarsVisible: Boolean,
    currentRoute: String?,
    previousRoute: String?,
    navigationRailPadding: Dp,
    onNavigationRailPaddingChange: (Dp) -> Unit,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (LocalWindowSizeHorizontal.current == WindowSize.COMPACT) {
        MainContentCompact(
            navController = navController,
            navigationBarsVisible = navigationBarsVisible,
            currentRoute = currentRoute,
            previousRoute = previousRoute,
            onNavigate = onNavigate,
            onNavigateSearch = onNavigateSearch,
            onRequestScrollToTop = onRequestScrollToTop,
            modifier = modifier,
        )
    } else {
        MainContentMediumWide(
            navController = navController,
            navigationBarsVisible = navigationBarsVisible,
            currentRoute = currentRoute,
            previousRoute = previousRoute,
            navigationRailPadding = navigationRailPadding,
            onNavigationRailPaddingChange = onNavigationRailPaddingChange,
            onNavigate = onNavigate,
            onNavigateSearch = onNavigateSearch,
            onRequestScrollToTop = onRequestScrollToTop,
            modifier = modifier,
        )
    }
}
