package com.uragiristereo.mejiboard.ui

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
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
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.uragiristereo.mejiboard.core.model.ShareOption
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.preferences.model.ThemePreference
import com.uragiristereo.mejiboard.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mejiboard.core.product.theme.MejiboardTheme
import com.uragiristereo.mejiboard.core.product.theme.Theme
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.LocalHomeNavController
import com.uragiristereo.mejiboard.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mejiboard.core.ui.LocalLambdaOnShare
import com.uragiristereo.mejiboard.core.ui.LocalMainNavController
import com.uragiristereo.mejiboard.core.ui.LocalPostsNavController
import com.uragiristereo.mejiboard.core.ui.animation.holdIn
import com.uragiristereo.mejiboard.core.ui.animation.holdOut
import com.uragiristereo.mejiboard.core.ui.animation.translateXFadeIn
import com.uragiristereo.mejiboard.core.ui.animation.translateXFadeOut
import com.uragiristereo.mejiboard.core.ui.animation.translateYFadeIn
import com.uragiristereo.mejiboard.core.ui.animation.translateYFadeOut
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.core.ui.navigation.PostsRoute
import com.uragiristereo.mejiboard.feature.about.AboutScreen
import com.uragiristereo.mejiboard.feature.filters.FiltersScreen
import com.uragiristereo.mejiboard.feature.home.posts.HomeScreen
import com.uragiristereo.mejiboard.feature.image.ImageScreen
import com.uragiristereo.mejiboard.feature.search.SearchScreen
import com.uragiristereo.mejiboard.feature.search_history.SearchHistoryScreen
import com.uragiristereo.mejiboard.feature.settings.SettingsScreen
import com.uragiristereo.mejiboard.lib.navigation_extension.accompanist.AnimatedNavHost
import com.uragiristereo.mejiboard.lib.navigation_extension.accompanist.composable
import com.uragiristereo.mejiboard.lib.navigation_extension.core.navigate
import com.uragiristereo.mejiboard.lib.navigation_extension.core.route
import com.uragiristereo.mejiboard.saved_searches.SavedSearchesScreen
import com.uragiristereo.mejiboard.ui.core.ShareDownloadDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

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

    val lambdaOnDownload: (Post) -> Unit = remember {
        { post ->
            if (!notificationPermissionState.status.isGranted || notificationPermissionState.status.shouldShowRationale) {
                viewModel.selectedPost = post
                notificationPermissionState.launchPermissionRequest()
            } else {
                viewModel.downloadPost(context, post)
            }
        }
    }

    val lambdaOnShare: (Post, ShareOption) -> Unit = remember {
        { post, shareOption ->
            viewModel.sharePost(
                context = context,
                post = post,
                shareOption = shareOption,
                onDownloadCompleted = {
                    Timber.d("download completed")
                },
                onDownloadFailed = {
                    Timber.d("download failed = $it")
                },
            )
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

    MejiboardTheme(
        theme = when (viewModel.preferences.theme) {
            ThemePreference.KEY_LIGHT -> Theme.LIGHT
            ThemePreference.KEY_DARK -> Theme.DARK
            else -> Theme.SYSTEM
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
                LocalLambdaOnShare provides lambdaOnShare,
            ),
        ) {
            ProductSetSystemBarsColor()

            if (viewModel.shareDialogVisible) {
                ShareDownloadDialog(
                    downloadState = viewModel.downloadState,
                    onCancelClick = viewModel::cancelShare,
                )
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                content = {
                    AnimatedNavHost(
                        navController = mainNavController,
                        startDestination = MainRoute.Home,
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
                            ),
                    ) {
                        // Home
                        composable(
                            route = MainRoute.Home,
                            disableDeserialization = true,
                            enterTransition = {
                                when (initialState.destination.route) {
                                    MainRoute.Home.route -> fadeIn()
                                    MainRoute.Search().route -> holdIn()
                                    else -> translateXFadeIn(forward = true)
                                }
                            },
                            exitTransition = {
                                when (targetState.destination.route) {
                                    MainRoute.Home.route -> fadeOut()
                                    MainRoute.Search().route -> holdOut()
                                    MainRoute.Image::class.route -> holdOut(durationMillis = 350)
                                    else -> translateXFadeOut(forward = true)
                                }
                            },
                            popEnterTransition = {
                                when (initialState.destination.route) {
                                    MainRoute.Home.route -> fadeIn()
                                    MainRoute.Search().route -> fadeIn()
                                    MainRoute.Image::class.route -> holdIn(durationMillis = 350)
                                    else -> translateXFadeIn(forward = false)
                                }
                            },
                            popExitTransition = {
                                when (targetState.destination.route) {
                                    MainRoute.Home.route -> fadeOut()
                                    MainRoute.Search().route -> holdOut()
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
                                        mainNavController.navigate(route = MainRoute.Search(tags)) {
                                            popUpTo(id = mainNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                        }
                                    },
                                    onNavigateImage = remember {
                                        { item ->
                                            viewModel.navigatedBackByGesture = false

                                            mainNavController.navigate(route = MainRoute.Image(post = item))
                                        }
                                    }
                                )
                            },
                        )

                        // Search
                        composable(
                            route = MainRoute.Search(),
                            disableDeserialization = true,
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
                                            route = PostsRoute.Index(searchTags),
                                        )
                                    },
                                )
                            },
                        )

                        // Image
                        composable(
                            route = MainRoute.Image::class,
                            disableDeserialization = true,
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
                            disableDeserialization = true,
                            content = {
                                SettingsScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // Filters
                        composable(
                            route = MainRoute.Filters,
                            disableDeserialization = true,
                            content = {
                                FiltersScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // Saved searches
                        composable(
                            route = MainRoute.SavedSearches,
                            disableDeserialization = true,
                            content = {
                                SavedSearchesScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // Search history
                        composable(
                            route = MainRoute.SearchHistory,
                            disableDeserialization = true,
                            content = {
                                SearchHistoryScreen(
                                    onNavigateBack = lambdaOnNavigateBack,
                                )
                            },
                        )

                        // About
                        composable(
                            route = MainRoute.About,
                            disableDeserialization = true,
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
