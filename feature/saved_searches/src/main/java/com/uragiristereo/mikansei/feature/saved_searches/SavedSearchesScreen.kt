package com.uragiristereo.mikansei.feature.saved_searches

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductPullRefreshIndicator
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.saved_searches.core.SavedSearchesTopAppBar
import com.uragiristereo.mikansei.feature.saved_searches.list.SavedSearchesList
import com.uragiristereo.mikansei.feature.saved_searches.list.SavedSearchesListItemActions
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedSearchesScreen(
    onNavigateBack: () -> Unit,
    onNavigateNewSavedSearch: () -> Unit,
    actions: SavedSearchesListItemActions,
    viewModel: SavedSearchesViewModel = koinViewModel(),
) {
    val scaffoldState = LocalScaffoldState.current
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isLoading,
        onRefresh = viewModel::refreshSavedSearches,
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SavedSearchesViewModel.Event.OnDeleteSuccess -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Saved search is successfully deleted!",
                        )
                    }
                }

                is SavedSearchesViewModel.Event.OnFailed -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Error ${event.message}",
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SavedSearchesTopAppBar(
                onNavigateBack = onNavigateBack,
                onRefreshClick = viewModel::refreshSavedSearches,
                onBrowseAllClick = {
                    actions.onQueryClick("search:all ")
                },
            )
        },
        bottomBar = {
            ProductNavigationBarSpacer()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateNewSavedSearch,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = null,
                    )
                },
            )
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
        ) {
            SavedSearchesList(
                isLoading = viewModel.isLoading,
                items = viewModel.items,
                contentPadding = innerPadding,
                expandedItemId = viewModel.expandedItemId,
                onExpandedItemIdChange = viewModel::onExpandedItemIdChange,
                actions = actions,
            )

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                ProductPullRefreshIndicator(
                    refreshing = viewModel.isLoading,
                    state = pullRefreshState,
                )
            }
        }
    }
}
