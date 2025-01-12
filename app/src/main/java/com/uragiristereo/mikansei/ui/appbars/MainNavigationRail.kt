package com.uragiristereo.mikansei.ui.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.NavRoute
import com.uragiristereo.mikansei.core.ui.navigation.routeOf

@Composable
fun MainNavigationRail(
    currentRoute: Int?,
    previousRoute: Int?,
    onNavigate: (NavRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onRequestScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight(),
        ) {
            MainNavigationItems.entries.forEach { item ->
                val itemRouteString = routeOf(item.route::class)
                val currentRouteIsItem = currentRoute == itemRouteString
                val previousRouteIsItem = previousRoute == itemRouteString

                val selected = when {
                    currentRouteIsItem -> true
                    currentRoute !in HomeRoutesString && previousRouteIsItem -> true
                    else -> false
                }

                NavigationRailItem(
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
                            listOf(itemRouteString, currentRoute).all {
                                it == routeOf<HomeRoute.Posts>()
                            } -> onRequestScrollToTop()

                            itemRouteString == routeOf<MainRoute.Search>() -> onNavigateSearch()
                            else -> onNavigate(item.route)
                        }
                    },
                    alwaysShowLabel = false,
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                )
            }
        }
    }
}
