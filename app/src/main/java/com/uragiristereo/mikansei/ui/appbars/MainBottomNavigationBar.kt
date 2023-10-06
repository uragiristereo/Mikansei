package com.uragiristereo.mikansei.ui.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.navigation.HomeDialogRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute

@Composable
fun MainBottomNavigationBar(
    currentRoute: String,
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
        ) {
            MainNavigationItems.entries.forEach { item ->
                val currentRouteIsDialog = currentRoute in HomeDialogRoutesString && previousRoute == item.route.route
                val selected = currentRoute == item.route::class.route || currentRouteIsDialog

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
                                it == HomeRoute.Posts::class.route
                            } -> onRequestScrollToTop()

                            item.route.route == MainRoute.Search::class.route -> onNavigateSearch()
                            else -> onNavigate(item.route)
                        }
                    },
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                )
            }
        }

        NavigationBarSpacer()
    }
}
