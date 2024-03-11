package com.uragiristereo.mikansei.ui.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.serializednavigationextension.runtime.NavRoute
import com.uragiristereo.serializednavigationextension.runtime.route
import com.uragiristereo.serializednavigationextension.runtime.routeOf

@Composable
fun MainBottomNavigationBar(
    currentRoute: String?,
    previousRoute: String?,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.background
                    .backgroundElevation()
                    .copy(
                        alpha = when {
                            MaterialTheme.colors.isLight -> 0.9f
                            else -> 0.95f
                        },
                    ),
            )
    ) {
        Box(
            modifier = Modifier.background(color = MaterialTheme.colors.background),
        ) {
            Divider()
        }

        BottomNavigation(
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.primary,
            windowInsets = WindowInsets.navigationBars,
        ) {
            MainNavigationItems.entries.forEach { item ->
                val itemRouteString = item.route.route
                val currentRouteIsItem = currentRoute == itemRouteString
                val previousRouteIsItem = previousRoute == itemRouteString

                val selected = when {
                    currentRouteIsItem -> true
                    currentRoute !in HomeRoutesString && previousRouteIsItem -> true
                    else -> false
                }

                BottomNavigationItem(
                    selected = selected,
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = when {
                                    selected -> item.selectedIcon
                                    else -> item.unselectedIcon
                                },
                            ),
                            contentDescription = stringResource(id = item.label),
                        )
                    },
                    onClick = {
                        when {
                            listOf(item.route.route, currentRoute).all {
                                it == routeOf<HomeRoute.Posts>()
                            } -> onRequestScrollToTop()

                            item.route.route == routeOf<MainRoute.Search>() -> onNavigateSearch()
                            else -> onNavigate(item.route)
                        }
                    },
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                )
            }
        }
    }
}
