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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.ui.extension.backgroundElevation
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute

@Composable
fun HomeNavigationRail(
    currentRoute: String?,
    onNavigate: (HomeRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            HomeNavigationItems.values().forEach { item ->
                NavigationRailItem(
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
    }
}
