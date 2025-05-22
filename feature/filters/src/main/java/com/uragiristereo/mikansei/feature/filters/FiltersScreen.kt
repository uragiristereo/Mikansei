package com.uragiristereo.mikansei.feature.filters

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductPullRefreshIndicator
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetValue
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState3
import com.uragiristereo.mikansei.feature.filters.appbars.FiltersTopAppBar
import com.uragiristereo.mikansei.feature.filters.column.FiltersColumn
import com.uragiristereo.mikansei.feature.filters.core.FiltersAddTagsDialog
import com.uragiristereo.mikansei.feature.filters.core.FiltersFab
import com.uragiristereo.mikansei.feature.filters.core.FiltersModalBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun FiltersScreen(
    viewModel: FiltersViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState3()
    val scope = rememberCoroutineScope()
    val columnState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isLoading,
        onRefresh = viewModel::refreshFilters,
    )

    BackHandler(
        enabled = viewModel.selectedItems.isNotEmpty() && !sheetState.isVisible,
        onBack = viewModel::deselectAll,
    )

    BackHandler(
        enabled = sheetState.isVisible,
        onBack = {
            scope.launch {
                sheetState.hide()
            }
        },
    )

    Scaffold(
        topBar = {
            ProductStatusBarSpacer {
                FiltersTopAppBar(
                    title = when (viewModel.selectedItems.size) {
                        0 -> stringResource(id = R.string.filters_label)
                        viewModel.items.size -> stringResource(id = R.string.selected_all)
                        else -> stringResource(id = R.string.n_selected, viewModel.selectedItems.size)
                    },
                    username = viewModel.username,
                    selectionMode = viewModel.selectedItems.isNotEmpty(),
                    onNavigateBack = onNavigateBack,
                    onClose = viewModel::deselectAll,
                    onSelectAll = viewModel::selectAll,
                    onSelectInverse = viewModel::selectInverse,
                    onRefresh = viewModel::refreshFilters,
                    onDelete = {
                        scope.launch {
                            sheetState.show()
                        }
                    },
                )
            }
        },
        floatingActionButton = {
            FiltersFab(
                visible = sheetState.targetValue == SheetValue.Hidden,
                isDeleteButton = viewModel.selectedItems.isNotEmpty(),
                onAdd = viewModel::showDialog,
                onDelete = {
                    scope.launch {
                        sheetState.show()
                    }
                },
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
        FiltersColumn(
            columnState = columnState,
            selectionMode = viewModel.selectedItems.isNotEmpty(),
            enabled = !viewModel.isLoading,
            items = viewModel.items,
            onItemSelected = viewModel::updateSelectedItem,
            onOutsideTapped = viewModel::deselectAll,
            contentPadding = innerPadding,
            modifier = Modifier.pullRefresh(pullRefreshState),
        )

        if (viewModel.selectedItems.isEmpty()) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
            ) {
                ProductPullRefreshIndicator(
                    refreshing = viewModel.isLoading,
                    state = pullRefreshState,
                )
            }
        }
    }

    FiltersModalBottomSheet(
        sheetState = sheetState,
        onDeleteClick = {
            scope.launch {
                viewModel.deleteSelectedItems()

                sheetState.hide()
            }
        },
    )

    FiltersAddTagsDialog(
        dialogShown = viewModel.dialogShown,
        onDismiss = viewModel::hideDialog,
        onDone = { tags ->
            viewModel.hideDialog()
            viewModel.addTags(tags)

            scope.launch {
                columnState.animateScrollToItem(
                    index = viewModel.items.size - 1 + 3,
                )
            }
        },
    )
}
