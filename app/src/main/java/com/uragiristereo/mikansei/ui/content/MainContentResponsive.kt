package com.uragiristereo.mikansei.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    testMode: Boolean,
    onNavigationRailPaddingChange: (Dp) -> Unit,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
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
