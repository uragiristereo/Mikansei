package com.uragiristereo.mikansei.feature.home.favorites

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.product.component.ProductPullRefreshIndicator
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalMainScaffoldPadding
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.composable.Banner
import com.uragiristereo.mikansei.core.ui.composable.Scaffold2
import com.uragiristereo.mikansei.core.ui.extension.horizontalOnly
import com.uragiristereo.mikansei.core.ui.extension.verticalOnly
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.feature.home.favorites.core.FavoritesTopAppBar
import com.uragiristereo.mikansei.feature.home.favorites.core.LoadingIndicator
import com.uragiristereo.mikansei.feature.home.favorites.core.LoadingState
import com.uragiristereo.mikansei.feature.home.favorites.grid.FavoritesGrid
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    onFavoriteClick: (id: Int, username: String) -> Unit,
    onFavGroupLongClick: (Favorite) -> Unit,
    onAddClick: () -> Unit,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.loadingState == LoadingState.FROM_REFRESH,
        onRefresh = viewModel::getFavoritesAndFavoriteGroups,
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            launch(SupervisorJob()) {
                when (event) {
                    is FavoritesViewModel.Event.OnError -> {
                        scaffoldState.snackbarHostState.showSnackbar(message = "Error: ${event.message}")
                    }

                    is FavoritesViewModel.Event.OnDeleteSuccess -> {
                        scaffoldState.snackbarHostState.showSnackbar(message = context.getString(R.string.delete_favorite_group_success))
                    }
                }
            }
        }
    }

    InterceptBackGestureForBottomSheetNavigator()

    Scaffold2(
        scaffoldState = scaffoldState,
        topBar = {
            ProductStatusBarSpacer {
                FavoritesTopAppBar(
                    activeUserName = viewModel.activeUser.name,
                    onRefreshClick = viewModel::getFavoritesAndFavoriteGroups,
                )
            }
        },
        floatingActionButton = {
            if (viewModel.activeUser.isNotAnonymous()) {
                FloatingActionButton(
                    onClick = onAddClick,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = null,
                        )
                    },
                )
            }
        },
        contentPadding = LocalMainScaffoldPadding.current.verticalOnly,
        modifier = Modifier
            .padding(LocalMainScaffoldPadding.current.horizontalOnly)
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.End))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.End)),
    ) { innerPadding ->
        if (viewModel.activeUser.isNotAnonymous()) {
            Box {
                AnimatedContent(
                    targetState = viewModel.loadingState == LoadingState.FROM_LOAD,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "grid_and_loading",
                ) { state ->
                    when {
                        state -> {
                            LoadingIndicator(
                                modifier = Modifier.padding(innerPadding),
                            )
                        }

                        else -> FavoritesGrid(
                            items = viewModel.favorites,
                            contentPadding = innerPadding,
                            onFavoriteClick = { id ->
                                onFavoriteClick(id, viewModel.activeUser.name)
                            },
                            onFavGroupLongClick = onFavGroupLongClick,
                            modifier = Modifier.pullRefresh(pullRefreshState),
                        )
                    }
                }

                ProductPullRefreshIndicator(
                    refreshing = viewModel.loadingState == LoadingState.FROM_REFRESH,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(innerPadding),
                )
            }
        } else {
            Banner(
                icon = painterResource(id = R.drawable.login),
                text = {
                    Text(
                        text = stringResource(id = R.string.please_login),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                    )
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
