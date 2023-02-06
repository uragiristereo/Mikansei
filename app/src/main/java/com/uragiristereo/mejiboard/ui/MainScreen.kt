package com.uragiristereo.mejiboard.ui

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
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
import com.uragiristereo.mejiboard.core.model.ShareOption
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.network.model.ThemePreference
import com.uragiristereo.mejiboard.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mejiboard.core.product.theme.MejiboardTheme
import com.uragiristereo.mejiboard.core.product.theme.Theme
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mejiboard.core.ui.LocalLambdaOnShare
import com.uragiristereo.mejiboard.core.ui.LocalMainNavController
import com.uragiristereo.mejiboard.core.ui.LocalNavigationRailPadding
import com.uragiristereo.mejiboard.core.ui.WindowSize
import com.uragiristereo.mejiboard.core.ui.composable.DimensionSubcomposeLayout
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.core.ui.rememberWindowSize
import com.uragiristereo.mejiboard.ui.appbars.MainBottomNavigationBar
import com.uragiristereo.mejiboard.ui.appbars.MainNavigationRail
import com.uragiristereo.mejiboard.ui.core.ShareDownloadDialog
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
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

    LaunchedEffect(key1 = currentRoute) {
        currentRoute?.let { viewModel.currentRoute = it }
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
                onDownloadCompleted = { uri ->
                    Timber.d("download completed")

                    val uriProvider = FileProvider.getUriForFile(
                        /* context = */ context,
                        /* authority = */ "${context.packageName}.provider",
                        /* file = */ uri.toFile(),
                    )

                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, uriProvider)
                        clipData = ClipData.newRawUri(/* label = */ null, /* uri = */ uriProvider)
                        type = context.contentResolver.getType(uriProvider)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    context.startActivity(
                        /* intent = */
                        Intent.createChooser(
                            /* target = */ intent,
                            /* title = */ context.getString(R.string.share_label),
                        ),
                    )
                },
                onDownloadFailed = {
                    Timber.d("download failed = $it")
                },
            )
        }
    }

    BackHandler(
        enabled = viewModel.confirmExit && currentRoute == MainRoute.Home::class.route,
        onBack = remember {
            {
                scope.launch {
                    viewModel.confirmExit = false

                    homeScaffoldState.snackbarHostState.showSnackbar(
                        message = context.resources.getString(
                            /* id = */ R.string.press_back_again_to_exit,
                            /* ...formatArgs = */ context.resources.getString(R.string.app_name),
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
                LocalMainNavController provides navController,
                LocalLambdaOnDownload provides lambdaOnDownload,
                LocalLambdaOnShare provides lambdaOnShare,
                LocalNavigationRailPadding provides when {
                    windowSize != WindowSize.COMPACT -> viewModel.navigationRailPadding
                    else -> 0.dp
                },
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

            Surface {
                if (windowSize == WindowSize.COMPACT) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainNavGraph(navController = navController)

                        AnimatedVisibility(
                            visible = viewModel.currentRoute in HomeRoutesString,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }),
                            modifier = Modifier.align(Alignment.BottomCenter),
                        ) {
                            MainBottomNavigationBar(
                                currentRoute = viewModel.currentRoute,
                                onNavigate = lambdaOnNavigate,
                                onNavigateSearch = lambdaOnNavigateSearch,
                                onRequestScrollToTop = viewModel::requestScrollToTop,
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainNavGraph(navController = navController)

                        AnimatedVisibility(
                            visible = viewModel.currentRoute in HomeRoutesString,
                            enter = slideInHorizontally(initialOffsetX = { -it }),
                            exit = slideOutHorizontally(targetOffsetX = { -it }),
                        ) {
                            DimensionSubcomposeLayout {
                                LaunchedEffect(key1 = size) {
                                    viewModel.navigationRailPadding = size.width
                                }

                                Row {
                                    MainNavigationRail(
                                        currentRoute = viewModel.currentRoute,
                                        onNavigate = lambdaOnNavigate,
                                        onNavigateSearch = lambdaOnNavigateSearch,
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
