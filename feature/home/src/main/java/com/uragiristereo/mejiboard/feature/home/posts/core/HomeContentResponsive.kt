package com.uragiristereo.mejiboard.feature.home.posts.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.ui.WindowSize
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.core.ui.rememberWindowSize
import com.uragiristereo.mejiboard.feature.home.posts.HomeNavGraph
import com.uragiristereo.mejiboard.feature.home.posts.appbars.HomeBottomNavigationBar
import com.uragiristereo.mejiboard.feature.home.posts.appbars.HomeNavigationRail

@Composable
fun HomeContentResponsive(
    currentRoute: String?,
    scaffoldState: ScaffoldState,
    onNavigate: (MainRoute) -> Unit,
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateImage: (Post) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSize = rememberWindowSize()

    if (windowSize == WindowSize.COMPACT) {
        Box(modifier = modifier) {
            HomeNavGraph(
                onNavigate = onNavigate,
                onNavigateImage = onNavigateImage,
                onCurrentTagsChange = onCurrentTagsChange,
            )

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxSize(),
            ) {
                HomeSnackbar(snackbarHostState = scaffoldState.snackbarHostState)

                HomeBottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = onNavigateHome,
                    onNavigateSearch = onNavigateSearch,
                )
            }
        }
    } else {
        Row(modifier = modifier) {
            HomeNavigationRail(
                currentRoute = currentRoute,
                onNavigate = onNavigateHome,
                onNavigateSearch = onNavigateSearch,
            )

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
            )

            Box {
                HomeNavGraph(
                    onNavigate = onNavigate,
                    onNavigateImage = onNavigateImage,
                    onCurrentTagsChange = onCurrentTagsChange,
                )

                HomeSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
    }
}
