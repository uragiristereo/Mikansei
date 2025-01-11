package com.uragiristereo.mikansei.ui.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.ui.LocalMainScaffoldPadding
import com.uragiristereo.mikansei.core.ui.navigation.NavRoute
import com.uragiristereo.mikansei.ui.appbars.MainBottomNavigationBar

@Composable
fun MainContentCompact(
    navigationBarsVisible: Boolean,
    currentRoute: Int?,
    previousRoute: Int?,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = navigationBarsVisible,
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 200),
                    initialOffsetY = { it },
                ),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 200),
                    targetOffsetY = { it },
                ),
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
        CompositionLocalProvider(
            LocalMainScaffoldPadding provides innerPadding,
            content = content,
        )
    }
}
