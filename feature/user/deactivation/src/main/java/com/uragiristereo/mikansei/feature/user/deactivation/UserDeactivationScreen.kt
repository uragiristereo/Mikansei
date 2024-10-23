package com.uragiristereo.mikansei.feature.user.deactivation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.user.deactivation.core.UserDeactivationTopAppBar
import com.uragiristereo.mikansei.feature.user.deactivation.navigation.UserDeactivationNavGraph
import com.uragiristereo.mikansei.feature.user.deactivation.navigation.UserDeactivationRoute
import com.uragiristereo.serializednavigationextension.runtime.routeOf
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDeactivationScreen(
    onNavigateBack: () -> Unit,
    onNavigateMore: () -> Unit,
    viewModel: UserDeactivationViewModel = koinViewModel(),
) {
    val scaffoldState = LocalScaffoldState.current
    val activeUser by viewModel.activeUser.collectAsState()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val currentPage = when (currentRoute) {
        routeOf<UserDeactivationRoute.Agreement>() -> UserDeactivationRoute.Agreement
        routeOf<UserDeactivationRoute.Methods>() -> UserDeactivationRoute.Methods
        routeOf<UserDeactivationRoute.InApp>() -> UserDeactivationRoute.InApp
        routeOf<UserDeactivationRoute.InWeb>() -> UserDeactivationRoute.InWeb
        else -> UserDeactivationRoute.Agreement
    }

    val popPage: () -> Unit = {
        if (!navController.navigateUp()) {
            onNavigateBack()
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is UserDeactivationViewModel.Event.OnDeactivateSuccess -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Your account has successfully deactivated.",
                            duration = SnackbarDuration.Long,
                        )
                    }

                    onNavigateMore()
                }

                is UserDeactivationViewModel.Event.OnDeactivateFailed -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Error: ${event.message}",
                        )
                    }
                }

                is UserDeactivationViewModel.Event.OnNavigateMore -> {
                    onNavigateMore()
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            UserDeactivationTopAppBar(
                activeUserName = activeUser.name,
                currentPage = currentPage,
                isNavigationButtonEnabled = !viewModel.isLoading,
                isLoading = viewModel.isLoading,
                onNavigateBack = popPage,
            )
        },
        bottomBar = {
            ProductNavigationBarSpacer()
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        UserDeactivationNavGraph(
            navController = navController,
            innerPadding = innerPadding,
        )
    }
}
