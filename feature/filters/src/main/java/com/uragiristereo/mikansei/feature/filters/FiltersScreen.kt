package com.uragiristereo.mikansei.feature.filters

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.uragiristereo.mikansei.core.product.component.ProductPullRefreshIndicator
import com.uragiristereo.mikansei.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.defaultPaddings
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.rememberModalBottomSheetState2
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
    modifier: Modifier = Modifier,
    viewModel: FiltersViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState2(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val columnState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isLoading,
        onRefresh = viewModel::refreshFilters,
    )

    ProductSetSystemBarsColor()

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
        },
        floatingActionButton = {
            FiltersFab(
                visible = sheetState.targetValue == ModalBottomSheetValue.Hidden,
                isDeleteButton = viewModel.selectedItems.isNotEmpty(),
                onAdd = viewModel::showDialog,
                onDelete = {
                    scope.launch {
                        sheetState.show()
                    }
                },
            )
        },
        modifier = modifier.defaultPaddings(),
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
                modifier = Modifier.fillMaxWidth(),
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
