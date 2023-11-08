package com.uragiristereo.mikansei.ui.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.ui.LocalMainScaffoldPadding
import com.uragiristereo.mikansei.ui.appbars.MainBottomNavigationBar
import com.uragiristereo.mikansei.ui.navgraphs.MainNavGraph

@Composable
fun MainContentCompact(
    navController: NavHostController,
    navigationBarsVisible: Boolean,
    currentRoute: String?,
    previousRoute: String?,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = navigationBarsVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                MainBottomNavigationBar(
                    currentRoute = currentRoute,
                    previousRoute = previousRoute,
                    onNavigate = onNavigate,
                    onNavigateSearch = onNavigateSearch,
                    onRequestScrollToTop = onRequestScrollToTop,
                )
            }
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        CompositionLocalProvider(LocalMainScaffoldPadding provides innerPadding) {
            MainNavGraph(navController = navController)
        }
    }
}
