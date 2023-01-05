package com.uragiristereo.mejiboard.ui

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.uragiristereo.mejiboard.core.common.ui.LocalHomeNavController
import com.uragiristereo.mejiboard.core.common.ui.LocalLambdaOnDownload
import com.uragiristereo.mejiboard.core.common.ui.LocalMainNavController
import com.uragiristereo.mejiboard.core.common.ui.LocalPostsNavController
import com.uragiristereo.mejiboard.core.common.ui.animation.holdIn
import com.uragiristereo.mejiboard.core.common.ui.animation.holdOut
import com.uragiristereo.mejiboard.core.common.ui.animation.translateXFadeIn
import com.uragiristereo.mejiboard.core.common.ui.animation.translateXFadeOut
import com.uragiristereo.mejiboard.core.common.ui.animation.translateYFadeIn
import com.uragiristereo.mejiboard.core.common.ui.animation.translateYFadeOut
import com.uragiristereo.mejiboard.core.model.navigation.MainRoute
import com.uragiristereo.mejiboard.core.model.navigation.composable
import com.uragiristereo.mejiboard.core.model.navigation.navigate
import com.uragiristereo.mejiboard.core.preferences.model.ThemePreference
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.feature.about.AboutScreen
import com.uragiristereo.mejiboard.feature.filters.FiltersScreen
import com.uragiristereo.mejiboard.feature.home.posts.HomeRoute
import com.uragiristereo.mejiboard.feature.home.posts.HomeScreen
import com.uragiristereo.mejiboard.feature.home.posts.PostsRoute
import com.uragiristereo.mejiboard.feature.image.ImageScreen
import com.uragiristereo.mejiboard.feature.search.SearchScreen
import com.uragiristereo.mejiboard.feature.search_history.SearchHistoryScreen
import com.uragiristereo.mejiboard.feature.settings.SettingsScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
) {
    val mainNavController = rememberAnimatedNavController()
    val homeNavController = rememberAnimatedNavController()
    val postsNavController = rememberAnimatedNavController()

    val homeScaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val lambdaOnNavigateBack: () -> Unit = {
        mainNavController.navigateUp()
    }

    val notificationPermissionState = rememberPermissionState(
        permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> android.Manifest.permission.POST_NOTIFICATIONS
            else -> android.Manifest.permission.INTERNET
        },
        onPermissionResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(
                    /* context = */ context,
                    /* text = */ context.getText(R.string.download_permission_denied),
                    /* duration = */ Toast.LENGTH_LONG,
                ).show()
            }

            viewModel.selectedPost?.let {
                viewModel.downloadPost(context, it)
            }
        }
    )

    val lambdaOnDownload: (com.uragiristereo.mejiboard.core.model.booru.post.Post) -> Unit = remember {
        { post ->
            if (!notificationPermissionState.status.isGranted || notificationPermissionState.status.shouldShowRationale) {
                viewModel.selectedPost = post
                notificationPermissionState.launchPermissionRequest()
            } else {
                viewModel.downloadPost(context, post)
            }
        }
    }

    BackHandler(
        enabled = viewModel.confirmExit && currentRoute == MainRoute.Home.route,
        onBack = remember {
            {
                scope.launch {
                    viewModel.confirmExit = false

                    homeScaffoldState.snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.press_back_again_to_exit),
                    )

                    viewModel.confirmExit = true
                }
            }
        },
    )

    BackHandler(
        enabled = !viewModel.confirmExit && currentRoute == MainRoute.Home.route,
        onBack = {
            (context as Activity).finishAffinity()
        },
    )

    com.uragiristereo.mejiboard.core.product.theme.MejiboardTheme(
        theme = when (viewModel.preferences.theme) {
            ThemePreference.KEY_LIGHT -> com.uragiristereo.mejiboard.core.product.theme.Theme.LIGHT
            ThemePreference.KEY_DARK -> com.uragiristereo.mejiboard.core.product.theme.Theme.DARK
            else -> com.uragiristereo.mejiboard.core.product.theme.Theme.SYSTEM
        },
        blackTheme = viewModel.preferences.blackTheme,
        monetEnabled = viewModel.preferences.monetEnabled,
    ) {
        CompositionLocalProvider(
            values = arrayOf(
                LocalMainNavController provides mainNavController,
                LocalHomeNavController provides homeNavController,
                LocalPostsNavController provides postsNavController,
                LocalLambdaOnDownload provides lambdaOnDownload,
            ),
        ) {
            com.uragiristereo.mejiboard.core.product.component.ProductSetSystemBarsColor()

            Surface(
                modifier = Modifier.fillMaxSize(),
                content = {
                    AnimatedNavHost(
                        navController = mainNavController,
                        startDestination = MainRoute.Home.route,
                        enterTransition = {
                            translateXFadeIn(forward = true)
                        },
                        exitTransition = {
                            translateXFadeOut(forward = true)
                        },
                        popEnterTransition = {
                            translateXFadeIn(forward = false)
                        },
                        popExitTransition = {
                            translateXFadeOut(forward = false)
                        },
                        modifier = Modifier
                            .background(
                                color = when {
                                    MaterialTheme.colors.isLight -> Color.White
                                    else -> Color.Black
                                },
                            )
                            .windowInsetsPadding(
                                insets = WindowInsets.navigationBars
                                    .only(sides = WindowInsetsSides.Start + WindowInsetsSides.End),
                            ),
                    ) {
                        // Home
                        composable(
                            route = MainRoute.Home,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    MainRoute.Home.route -> fadeIn()
                                    MainRoute.Search.route -> holdIn()
                                    else -> translateXFadeIn(forward = true)
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    MainRoute.Home.route -> fadeOut()
                                    MainRoute.Search.route -> holdOut()
                                    MainRoute.Image.route -> holdOut(durationMillis = 350)
                                    else -> translateXFadeOut(forward = true)
                                }
                            },
                            popEnterTransition = {
                                when (initialState.destination.route) {
                                    MainRoute.Home.route -> fadeIn()
                                    MainRoute.Search.route -> fadeIn()
                                    MainRoute.Image.route -> holdIn(durationMillis = 350)
                                    else -> translateXFadeIn(forward = false)
                                }
                            },
                            popExitTransition = {
                                when (targetState.destination.route) {
                                    MainRoute.Home.route -> fadeOut()
                                    MainRoute.Search.route -> holdOut()
                                    else -> translateXFadeOut(forward = false)
                                }
                            },
                            content = {
                                HomeScreen(
                                    scaffoldState = homeScaffoldState,
                                    onNavigate = { route ->
                                        mainNavController.navigate(route = route)
                                    },
                                    onNavigateSearch = { tags ->
                                        mainNavController.navigate(
                                            route = MainRoute.Search,
                                            data = mapOf("tags" to tags),
                                        ) {
                                            popUpTo(id = mainNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                        }
                                    },
                                    onNavigateImage = remember {
                                        { item ->
                                            viewModel.navigatedBackByGesture = false

                                            mainNavController.navigate(
                                                route = MainRoute.Image,
                                                data = mapOf("post" to item),
                                            )
                                        }
                                    }
                                )
                            },
                        )

                        // Search
                        composable(
                            route = MainRoute.Search,
                            enterTransition = {
                                holdIn()
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    MainRoute.Home.route -> fadeOut()
                                    else -> translateXFadeOut(forward = true)
                                }
                            },
                            popEnterTransition = {
                                translateXFadeIn(forward = false)
                            },
                            popExitTransition = {
                                when (targetState.destination.route) {
                                    MainRoute.Home.route -> fadeOut()
                                    else -> translateXFadeOut(forward = false)
                                }
                            },
                            content = {
                                SearchScreen(
                                    onNavigate = { route ->
                                        mainNavController.navigate(route)
                                    },
                                    onNavigateBack = lambdaOnNavigateBack,
                                    onSearchSubmit = { searchTags ->
                                        mainNavController.navigate(route = MainRoute.Home) {
                                            popUpTo(id = mainNavController.graph.findStartDestination().id)

                                            restoreState = true
                                            launchSingleTop = true
                                        }

                                        homeNavController.navigate(route = HomeRoute.Posts) {
                                            popUpTo(id = homeNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            restoreState = true
                                            launchSingleTop = true
                                        }

                                        postsNavController.navigate(
                                            route = PostsRoute.Index,
                                            data = mapOf("tags" to searchTags),
                                        )
                                    },
                                )
                            },
                        )

                        // Image
                        composable(
                            route = MainRoute.Image,
                            enterTransition = {
                                translateYFadeIn(
                                    initialOffsetY = { it / 5 },
                                    durationMillis = 350,
                                )
                            },
                            popExitTransition = {
                                when {
                                    viewModel.navigatedBackByGesture -> fadeOut()
                                    else -> translateYFadeOut(
                                        targetOffsetY = { it / 5 },
                                        durationMillis = 350,
                                    )
                                }

                            },
                            content = {
                                ImageScreen(
                                    onNavigateBack = {
                                        viewModel.navigatedBackByGesture = it
                                        mainNavController.navigateUp()
                                    },
                                )
                            },
                        )

                        // Settings
                        composable(
                            route = MainRoute.Settings,
                            content = {
                                SettingsScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // Filters
                        composable(
                            route = MainRoute.Filters,
                            content = {
                                FiltersScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // Saved searches
                        composable(
                            route = MainRoute.SavedSearches,
                            content = {
                                SearchHistoryScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // Search history
                        composable(
                            route = MainRoute.SearchHistory,
                            content = {
                                SearchHistoryScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // About
                        composable(
                            route = MainRoute.About,
                            content = {
                                AboutScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )
                    }
                },
            )
        }
    }
}
