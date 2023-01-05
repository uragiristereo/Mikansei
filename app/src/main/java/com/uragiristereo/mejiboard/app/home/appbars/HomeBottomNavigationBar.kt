package com.uragiristereo.mejiboard.app.home.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.app.MainRoute
import com.uragiristereo.mejiboard.app.home.HomeRoute
import com.uragiristereo.mejiboard.core.common.ui.composable.NavigationBarSpacer
import com.uragiristereo.mejiboard.core.common.ui.extension.backgroundElevation
import com.uragiristereo.mejiboard.core.common.ui.navigation.NavigationRoute

@Composable
fun HomeBottomNavigationBar(
    currentRoute: String?,
    onNavigate: (NavigationRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    val alwaysShowLabel = remember { false }

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
            // Posts
            BottomNavigationItem(
                label = {
                    Text(text = stringResource(id = R.string.posts_label))
                },
                selected = currentRoute == HomeRoute.Posts.route,
                icon = {
                    Icon(
                        painter = painterResource(
                            id = when (currentRoute) {
                                HomeRoute.Posts.route -> R.drawable.home_fill
                                else -> R.drawable.home
                            },
                        ),
                        contentDescription = null,
                    )
                },
                onClick = {
                    onNavigate(HomeRoute.Posts)
                },
                alwaysShowLabel = alwaysShowLabel,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
            )

            // Search
            BottomNavigationItem(
                label = {
                    Text(text = stringResource(id = R.string.search_label))
                },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                    )
                },
                onClick = {
                    onNavigate(MainRoute.Search)
                },
                alwaysShowLabel = alwaysShowLabel,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
            )

            // Collections
            BottomNavigationItem(
                label = {
                    Text(text = stringResource(id = R.string.collections_label))
                },
                selected = currentRoute == HomeRoute.Collections.route,
                icon = {
                    BadgedBox(
                        badge = {
//                            Badge()
                        },
                        content = {
                            Icon(
                                painter = painterResource(
                                    id = when (currentRoute) {
                                        HomeRoute.Collections.route -> R.drawable.photo_library_fill
                                        else -> R.drawable.photo_library
                                    },
                                ),
                                contentDescription = null,
                            )
                        }
                    )
                },
                onClick = {
                    onNavigate(HomeRoute.Collections)
                },
                alwaysShowLabel = alwaysShowLabel,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
            )

            // More
            BottomNavigationItem(
                label = {
                    Text(text = stringResource(id = R.string.more_label))
                },
                selected = currentRoute == HomeRoute.More.route,
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.more_horiz),
                        contentDescription = null,
                    )
                },
                onClick = {
                    onNavigate(HomeRoute.More)
                },
                alwaysShowLabel = alwaysShowLabel,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
            )
        }

        NavigationBarSpacer()
    }
}
