package com.uragiristereo.mejiboard.feature.home.posts.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.common.ui.extension.backgroundElevation
import com.uragiristereo.mejiboard.core.model.navigation.MainRoute
import com.uragiristereo.mejiboard.core.model.navigation.NavigationRoute
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.feature.home.posts.HomeRoute

@Composable
fun HomeNavigationRail(
    currentRoute: String?,
    onNavigate: (NavigationRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    val alwaysShowLabel = remember { false }

    NavigationRail(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
//        header = {
//            Icon(
//                painter = painterResource(id = R.drawable.meji),
//                contentDescription = null,
//                tint = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
//                modifier = Modifier
//                    .padding(top = 4.dp)
//                    .size(32.dp),
//            )
//        },
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight(),
        ) {
            // Posts
            NavigationRailItem(
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
            NavigationRailItem(
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
            NavigationRailItem(
                label = {
                    Text(text = stringResource(id = R.string.collections_label))
                },
                selected = currentRoute == HomeRoute.Collections.route,
                icon = {
                    Icon(
                        painter = painterResource(
                            id = when (currentRoute) {
                                HomeRoute.Collections.route -> R.drawable.photo_library_fill
                                else -> R.drawable.photo_library
                            },
                        ),
                        contentDescription = null,
                    )
                },
                onClick = {
                    onNavigate(HomeRoute.Collections)
                },
                alwaysShowLabel = alwaysShowLabel,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.74f),
            )

            // More
            NavigationRailItem(
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
    }
}
