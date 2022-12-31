package com.uragiristereo.mejiboard.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import com.uragiristereo.mejiboard.presentation.MainRoute
import com.uragiristereo.mejiboard.presentation.common.LocalHomeNavController
import com.uragiristereo.mejiboard.presentation.common.composable.product.ProductSetSystemBarsColor
import com.uragiristereo.mejiboard.presentation.common.navigation.NavigationRoute
import com.uragiristereo.mejiboard.presentation.common.navigation.navigate
import com.uragiristereo.mejiboard.presentation.home.core.HomeContentResponsive
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (route: NavigationRoute) -> Unit,
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

    val lambdaOnHomeNavigate: (NavigationRoute) -> Unit = { route ->
        when (route) {
            MainRoute.Search -> onNavigateSearch(viewModel.currentTags)

            else -> {
                homeNavController.navigate(route) {
                    popUpTo(id = homeNavController.graph.findStartDestination().id) {
                        saveState = true
                    }

                    restoreState = true
                    launchSingleTop = true
                }
            }
        }
    }

    ProductSetSystemBarsColor(
        navigationBarColor = Color.Transparent,
    )

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        modifier = Modifier.statusBarsPadding(),
    ) { innerPadding ->
        HomeContentResponsive(
            currentRoute = viewModel.currentRoute,
            scaffoldState = scaffoldState,
            onNavigate = { route ->
                when (route) {
                    MainRoute.Search -> onNavigateSearch(viewModel.currentTags)
                    else -> onNavigate(route)
                }
            },
            onHomeNavigate = lambdaOnHomeNavigate,
            onNavigateImage = onNavigateImage,
            onCurrentTagsChange = remember { { viewModel.currentTags = it } },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
