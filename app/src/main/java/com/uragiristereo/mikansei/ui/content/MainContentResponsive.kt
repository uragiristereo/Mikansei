package com.uragiristereo.mikansei.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.ui.navgraphs.MainNavGraph
import com.uragiristereo.serializednavigationextension.runtime.NavRoute

@Composable
fun MainContentResponsive(
    navController: NavHostController,
    navigationBarsVisible: Boolean,
    currentRoute: String?,
    previousRoute: String?,
    navigationRailPadding: Dp,
    testMode: Boolean,
    onNavigationRailPaddingChange: (Dp) -> Unit,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mainNavGraphContent = remember(navController) {
        movableContentOf {
            MainNavGraph(navController = navController)
        }
    }

    Box {
        if (LocalWindowSizeHorizontal.current == WindowSize.COMPACT) {
            MainContentCompact(
                navigationBarsVisible = navigationBarsVisible,
                currentRoute = currentRoute,
                previousRoute = previousRoute,
                onNavigate = onNavigate,
                onNavigateSearch = onNavigateSearch,
                onRequestScrollToTop = onRequestScrollToTop,
                modifier = modifier,
                content = mainNavGraphContent,
            )
        } else {
            MainContentMediumWide(
                navigationBarsVisible = navigationBarsVisible,
                currentRoute = currentRoute,
                previousRoute = previousRoute,
                navigationRailPadding = navigationRailPadding,
                onNavigationRailPaddingChange = onNavigationRailPaddingChange,
                onNavigate = onNavigate,
                onNavigateSearch = onNavigateSearch,
                onRequestScrollToTop = onRequestScrollToTop,
                modifier = modifier,
                content = mainNavGraphContent,
            )
        }

        if (testMode) {
            Text(
                text = "test mode".uppercase(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.overline,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(size = 4.dp))
                    .background(
                        MaterialTheme.colors.primary.copy(alpha = 0.5f)
                    )
                    .padding(all = 4.dp),
            )
        }
    }
}
