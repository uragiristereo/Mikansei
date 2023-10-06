package com.uragiristereo.mikansei.ui

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import com.uragiristereo.mikansei.core.preferences.model.ThemePreference
import com.uragiristereo.mikansei.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.product.theme.Theme
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.LocalMainNavController
import com.uragiristereo.mikansei.core.ui.LocalNavigationRailPadding
import com.uragiristereo.mikansei.core.ui.LocalScrollToTopChannel
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.composable.DimensionSubcomposeLayout
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.navigation.HomeAndDialogRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.rememberWindowSize
import com.uragiristereo.mikansei.ui.appbars.MainBottomNavigationBar
import com.uragiristereo.mikansei.ui.appbars.MainNavigationRail
import com.uragiristereo.mikansei.ui.core.ShareDownloadDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val navController = rememberAnimatedNavController()
    val homeScaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val windowSize = rememberWindowSize()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var previousRoute by remember { mutableStateOf<String?>(null) }

    val preferences by viewModel.preferences.collectAsState()

    val notificationPermissionState = rememberPermissionState(
        permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> android.Manifest.permission.POST_NOTIFICATIONS
            else -> android.Manifest.permission.INTERNET
        },
        onPermissionResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, context.getText(R.string.download_permission_denied), Toast.LENGTH_LONG).show()
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
            )
        }
    }

    val lambdaOnRequestScrollToTop: () -> Unit = {
        scope.launch {
            viewModel.scrollToTopChannel.send(UUID.randomUUID().toString())
        }
    }

    LaunchedEffect(key1 = currentRoute) {
        previousRoute = viewModel.currentRoute
        currentRoute?.let { viewModel.currentRoute = it }
    }

    BackHandler(
        enabled = viewModel.confirmExit && currentRoute == MainRoute.Home::class.route,
        onBack = remember {
            {
                scope.launch {
                    viewModel.confirmExit = false

                    homeScaffoldState.snackbarHostState.showSnackbar(
                        message = context.resources.getString(
                            /* id = */
                            R.string.press_back_again_to_exit,
                            /* ...formatArgs = */
                            context.resources.getString(R.string.app_name),
                        ),
                    )

                    viewModel.confirmExit = true
                }
            }
        },
    )

    BackHandler(
        enabled = !viewModel.confirmExit && currentRoute == MainRoute.Home::class.route,
        onBack = {
            (context as Activity).finishAffinity()
        },
    )

    MikanseiTheme(
        theme = when (preferences.theme) {
            ThemePreference.LIGHT -> Theme.LIGHT
            ThemePreference.DARK -> Theme.DARK
            else -> Theme.SYSTEM
        },
        blackTheme = preferences.blackTheme,
        monetEnabled = preferences.monetEnabled,
    ) {
        CompositionLocalProvider(
            values = arrayOf(
                LocalMainNavController provides navController,
                LocalLambdaOnDownload provides lambdaOnDownload,
                LocalLambdaOnShare provides lambdaOnShare,
                LocalNavigationRailPadding provides when {
                    windowSize != WindowSize.COMPACT -> viewModel.navigationRailPadding
                    else -> 0.dp
                },
                LocalScrollToTopChannel provides viewModel.scrollToTopChannel,
            ),
        ) {
            ProductSetSystemBarsColor()

            if (viewModel.shareDialogVisible) {
                ShareDownloadDialog(
                    downloadState = viewModel.downloadState,
                    onCancelClick = viewModel::cancelShare,
                )
            }

            val lambdaOnNavigate: (NavRoute) -> Unit = remember {
                { route ->
                    navController.navigate(route) {
                        popUpTo(id = navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        restoreState = true
                        launchSingleTop = true
                    }
                }
            }

            val lambdaOnNavigateSearch: () -> Unit = remember {
                {
                    navController.navigate(
                        route = MainRoute.Search(tags = viewModel.currentTags)
                    )
                }
            }

            val navigationBarsVisible = when {
                previousRoute in HomeRoutesString && viewModel.currentRoute in HomeAndDialogRoutesString -> true
                viewModel.currentRoute in HomeRoutesString -> true
                else -> false
            }

            Surface {
                if (windowSize == WindowSize.COMPACT) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainNavGraph(navController = navController)

                        AnimatedVisibility(
                            visible = navigationBarsVisible,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }),
                            modifier = Modifier.align(Alignment.BottomCenter),
                        ) {
                            MainBottomNavigationBar(
                                currentRoute = viewModel.currentRoute,
                                previousRoute = previousRoute,
                                onNavigate = lambdaOnNavigate,
                                onNavigateSearch = lambdaOnNavigateSearch,
                                onRequestScrollToTop = lambdaOnRequestScrollToTop,
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainNavGraph(navController = navController)

                        AnimatedVisibility(
                            visible = navigationBarsVisible,
                            enter = slideInHorizontally(initialOffsetX = { -it }),
                            exit = slideOutHorizontally(targetOffsetX = { -it }),
                            modifier = Modifier,
                        ) {
                            Row {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .background(MaterialTheme.colors.background.backgroundElevation())
                                        .displayCutoutPadding(),
                                )

                                DimensionSubcomposeLayout {
                                    LaunchedEffect(key1 = size) {
                                        viewModel.navigationRailPadding = size.width
                                    }

                                    Row {
                                        MainNavigationRail(
                                            currentRoute = viewModel.currentRoute,
                                            previousRoute = previousRoute,
                                            onNavigate = lambdaOnNavigate,
                                            onNavigateSearch = lambdaOnNavigateSearch,
                                            onRequestScrollToTop = lambdaOnRequestScrollToTop,
                                        )

                                        Divider(
                                            modifier = Modifier
                                                .width(1.dp)
                                                .fillMaxHeight(),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
