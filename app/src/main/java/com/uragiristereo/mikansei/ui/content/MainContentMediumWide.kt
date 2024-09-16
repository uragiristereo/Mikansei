package com.uragiristereo.mikansei.ui.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsStartWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.LocalMainScaffoldPadding
import com.uragiristereo.mikansei.core.ui.composable.DimensionSubcomposeLayout
import com.uragiristereo.mikansei.core.ui.composable.RailScaffold
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.extension.copy
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.ui.appbars.MainNavigationRail
import com.uragiristereo.serializednavigationextension.runtime.NavRoute
import com.uragiristereo.serializednavigationextension.runtime.routeOf

@Composable
fun MainContentMediumWide(
    navigationBarsVisible: Boolean,
    currentRoute: String?,
    previousRoute: String?,
    navigationRailPadding: Dp,
    onNavigationRailPaddingChange: (Dp) -> Unit,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    RailScaffold(
        startBar = {
            AnimatedVisibility(
                visible = navigationBarsVisible,
                enter = slideInHorizontally(
                    animationSpec = tween(durationMillis = 200),
                    initialOffsetX = { -it },
                ),
                exit = slideOutHorizontally(
                    animationSpec = tween(durationMillis = 200),
                    targetOffsetX = { -it },
                ),
            ) {
                DimensionSubcomposeLayout {
                    LaunchedEffect(key1 = size.width) {
                        onNavigationRailPaddingChange(size.width)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(MaterialTheme.colors.background.backgroundElevation())
                    ) {
                        Spacer(
                            modifier = Modifier
                                .windowInsetsPadding(
                                    WindowInsets.navigationBars.only(WindowInsetsSides.Start)
                                )
                                .windowInsetsPadding(
                                    WindowInsets.displayCutout.only(WindowInsetsSides.Start)
                                ),
                        )

                        MainNavigationRail(
                            currentRoute = currentRoute,
                            previousRoute = previousRoute,
                            onNavigate = onNavigate,
                            onNavigateSearch = onNavigateSearch,
                            onRequestScrollToTop = onRequestScrollToTop,
                        )

                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .statusBarsPadding(),
                        )
                    }
                }
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        CompositionLocalProvider(
            values = arrayOf(
                LocalMainScaffoldPadding provides innerPadding.copy(start = navigationRailPadding),
            ),
        ) {
            Box {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(navigationRailPadding)
                        .background(Color.Black)
                        .align(Alignment.CenterStart),
                )

                content()

                if (currentRoute !in listOf(
                        routeOf<MainRoute.Image>(),
                        routeOf<HomeRoute.Posts>(),
                    )
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .windowInsetsStartWidth(WindowInsets.displayCutout)
                            .background(Color.Black)
                            .align(Alignment.CenterStart),
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .windowInsetsEndWidth(WindowInsets.displayCutout)
                            .background(Color.Black)
                            .align(Alignment.CenterEnd),
                    )
                }
            }
        }
    }
}
