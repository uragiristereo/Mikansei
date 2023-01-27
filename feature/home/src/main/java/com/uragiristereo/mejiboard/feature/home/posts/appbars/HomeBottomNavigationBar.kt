package com.uragiristereo.mejiboard.feature.home.posts.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mejiboard.core.ui.extension.backgroundElevation
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute

@Composable
fun HomeBottomNavigationBar(
    currentRoute: String?,
    onNavigate: (HomeRoute) -> Unit,
    onNavigateSearch: () -> Unit,
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
            HomeNavigationItems.values().forEach { item ->
                BottomNavigationItem(
                    label = {
                        Text(text = stringResource(id = item.label))
                    },
                    selected = currentRoute == item.route.route,
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = when (currentRoute) {
                                    item.route.route -> item.selectedIcon
                                    else -> item.unselectedIcon
                                },
                            ),
                            contentDescription = stringResource(id = item.label),
                        )
                    },
                    onClick = {
                        when (item) {
                            HomeNavigationItems.Search -> onNavigateSearch()
                            else -> onNavigate(item.route as HomeRoute)
                        }
                    },
                    alwaysShowLabel = false,
                    unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
                )
            }
        }

        NavigationBarSpacer()
    }
}
