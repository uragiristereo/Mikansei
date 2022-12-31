package com.uragiristereo.mejiboard.presentation.home.core

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import com.uragiristereo.mejiboard.presentation.common.navigation.NavigationRoute
import com.uragiristereo.mejiboard.presentation.home.HomeNavGraph
import com.uragiristereo.mejiboard.presentation.home.appbars.HomeBottomNavigationBar
import com.uragiristereo.mejiboard.presentation.home.appbars.HomeNavigationRail

@Composable
fun HomeContentResponsive(
    currentRoute: String?,
    scaffoldState: ScaffoldState,
    onNavigate: (NavigationRoute) -> Unit,
    onHomeNavigate: (NavigationRoute) -> Unit,
    onNavigateImage: (Post) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(modifier = modifier) {
            HomeNavigationRail(
                currentRoute = currentRoute,
                onNavigate = onHomeNavigate,
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
    } else {
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
                    onNavigate = onHomeNavigate,
                )
            }
        }
    }
}
