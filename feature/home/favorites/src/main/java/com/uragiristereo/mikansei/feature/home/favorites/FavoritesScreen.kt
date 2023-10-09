package com.uragiristereo.mikansei.feature.home.favorites

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductPullRefreshIndicator
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalMainScaffoldPadding
import com.uragiristereo.mikansei.core.ui.composable.Banner
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.extension.forEach
import com.uragiristereo.mikansei.core.ui.extension.horizontalOnly
import com.uragiristereo.mikansei.core.ui.extension.verticalOnly
import com.uragiristereo.mikansei.feature.home.favorites.core.FavoritesTopAppBar
import com.uragiristereo.mikansei.feature.home.favorites.core.LoadingIndicator
import com.uragiristereo.mikansei.feature.home.favorites.core.LoadingState
import com.uragiristereo.mikansei.feature.home.favorites.grid.FavoritesGrid
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun FavoritesScreen(
    onFavoritesClick: (id: Int, userName: String) -> Unit,
    onAddClick: () -> Unit,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.loadingState == LoadingState.FROM_REFRESH,
        onRefresh = viewModel::getFavoritesAndFavoriteGroups,
    )

    LaunchedEffect(key1 = viewModel) {
        viewModel.errorMessageChannel.forEach { errorMessage ->
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
        }
    }

    SetSystemBarsColors(Color.Transparent)

    Scaffold(
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
                    transitionSpec = { fadeIn() with fadeOut() },
                    label = "grid_and_loading",
                ) { state ->
                    when {
                        state -> {
                            LoadingIndicator(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .navigationBarsPadding()
                                    .padding(bottom = 57.dp),
                            )
                        }

                        else -> FavoritesGrid(
                            items = viewModel.favorites,
                            contentPadding = innerPadding,
                            onFavoritesClick = { id ->
                                onFavoritesClick(id, viewModel.activeUser.name)
                            },
                            modifier = Modifier.pullRefresh(pullRefreshState),
                        )
                    }
                }

                ProductPullRefreshIndicator(
                    refreshing = viewModel.loadingState == LoadingState.FROM_REFRESH,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        } else {
            Banner(
                icon = painterResource(id = R.drawable.login),
                text = {
                    Text(
                        text = "Please login to use this feature",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6,
                    )
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
