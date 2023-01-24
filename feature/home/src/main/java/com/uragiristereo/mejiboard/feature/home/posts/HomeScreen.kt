package com.uragiristereo.mejiboard.feature.home.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mejiboard.core.ui.LocalHomeNavController
import com.uragiristereo.mejiboard.core.ui.extension.backgroundElevation
import com.uragiristereo.mejiboard.feature.home.posts.core.HomeContentResponsive
import com.uragiristereo.mejiboard.lib.navigation_extension.core.NavRoute
import com.uragiristereo.mejiboard.lib.navigation_extension.core.navigate
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (route: NavRoute) -> Unit,
    onNavigateSearch: (tags: String) -> Unit,
    onNavigateImage: (Post) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val homeNavController = LocalHomeNavController.current

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(key1 = currentRoute) {
        currentRoute?.let {
            viewModel.currentRoute = it
        }
    }

    ProductSetSystemBarsColor(
        navigationBarColor = Color.Transparent,
    )

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        modifier = Modifier
            .statusBarsPadding()
            .windowInsetsPadding(
                insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Horizontal),
            )
            .background(MaterialTheme.colors.background.backgroundElevation())
            .displayCutoutPadding(),
    ) { innerPadding ->
        HomeContentResponsive(
            currentRoute = viewModel.currentRoute,
            scaffoldState = scaffoldState,
            onNavigate = onNavigate,
            onNavigateHome = { route ->
                homeNavController.navigate(route) {
                    popUpTo(id = homeNavController.graph.findStartDestination().id) {
                        saveState = true
                    }

                    restoreState = true
                    launchSingleTop = true
                }
            },
            onNavigateSearch = {
                onNavigateSearch(viewModel.currentTags)
            },
            onNavigateImage = onNavigateImage,
            onCurrentTagsChange = remember { { viewModel.currentTags = it } },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
