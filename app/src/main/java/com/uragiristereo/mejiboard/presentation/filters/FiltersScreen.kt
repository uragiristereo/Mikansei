package com.uragiristereo.mejiboard.presentation.filters

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.presentation.common.composable.product.ProductSetSystemBarsColor
import com.uragiristereo.mejiboard.presentation.filters.appbars.FiltersSelectionTopAppBar
import com.uragiristereo.mejiboard.presentation.filters.appbars.FiltersTopAppBar
import com.uragiristereo.mejiboard.presentation.filters.column.FiltersColumn
import com.uragiristereo.mejiboard.presentation.filters.core.FiltersAddTagsDialog
import com.uragiristereo.mejiboard.presentation.filters.core.FiltersFab
import com.uragiristereo.mejiboard.presentation.filters.core.FiltersModalBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FiltersScreen(
    modifier: Modifier = Modifier,
    viewModel: FiltersViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val columnState = rememberLazyListState()

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
            Crossfade(
                targetState = viewModel.selectedItems.isNotEmpty(),
                content = { notEmpty ->
                    when {
                        notEmpty -> FiltersSelectionTopAppBar(
                            title = when (viewModel.selectedItems.size) {
                                0 -> stringResource(id = R.string.filters_label)
                                viewModel.items.size -> stringResource(id = R.string.selected_all)
                                else -> stringResource(id = R.string.n_selected, viewModel.selectedItems.size)
                            },
                            onClose = viewModel::deselectAll,
                            onDelete = {
                                scope.launch {
                                    sheetState.show()
                                }
                            },
                            onSelectAll = viewModel::selectAll,
                            onSelectInverse = viewModel::selectInverse,
                        )

                        else -> FiltersTopAppBar(
                            onNavigateBack = onNavigateBack,
                            onSelectAll = viewModel::selectAll,
                        )
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
        modifier = modifier.statusBarsPadding(),
    ) { innerPadding ->
        FiltersColumn(
            columnState = columnState,
            selectionMode = viewModel.selectedItems.isNotEmpty(),
            items = viewModel.items,
            onItemChange = viewModel::updateItem,
            onItemSelected = viewModel::updateSelectedItem,
            toggleChecked = viewModel.toggleChecked,
            onToggleChecked = viewModel::onToggleChecked,
            onOutsideTapped = viewModel::deselectAll,
            contentPadding = innerPadding,
        )
    }

    FiltersModalBottomSheet(
        sheetState = sheetState,
        onDeleteClick = remember {
            {
                scope.launch {
                    viewModel.deleteSelectedItems()

                    sheetState.hide()
                }
            }
        },
    )

    FiltersAddTagsDialog(
        dialogShown = viewModel.dialogShown,
        onDialogShownChange = viewModel::changeDialogState,
        onDone = remember {
            { tags ->
                viewModel.changeDialogState(value = false)

                viewModel.addTags(context, tags)

                scope.launch {
                    columnState.animateScrollToItem(
                        index = viewModel.items.size - 1 + 3,
                    )
                }
            }
        },
    )
}
