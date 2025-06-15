package com.uragiristereo.mikansei.feature.saved_searches.new_or_edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core.FabState
import com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core.NewOrEditSavedSearchFab
import com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core.NewOrEditSavedSearchTopAppBar
import com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core.UnsavedConfirmationDialog
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewOrEditSavedSearchScreen(
    onNavigateBack: () -> Unit,
    onRefreshList: () -> Unit,
    viewModel: NewOrEditSavedSearchViewModel = koinViewModel(),
) {
    val scaffoldState = LocalScaffoldState.current
    val queryFocusRequester = remember { FocusRequester() }
    val labelsFocusRequester = remember { FocusRequester() }
    val forbidExit = viewModel.isQueryEdited || viewModel.areLabelsEdited

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is NewOrEditSavedSearchViewModel.Event.OnSuccess -> {
                    onNavigateBack()
                    onRefreshList()

                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = when {
                                viewModel.savedSearchId != null -> "Saved search is successfully edited!"
                                else -> "Saved search is successfully added!"
                            },
                        )
                    }
                }

                is NewOrEditSavedSearchViewModel.Event.OnFailed -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Error: ${event.message}",
                        )
                    }
                }
            }
        }
    }

    BackHandler(enabled = forbidExit) {
        viewModel.showUnsavedConfirmationDialog()
    }

    UnsavedConfirmationDialog(
        visible = viewModel.isUnsavedConfirmationDialogVisible,
        onDiscard = onNavigateBack,
        onHide = viewModel::hideUnsavedConfirmationDialog,
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            NewOrEditSavedSearchTopAppBar(
                savedSearchId = viewModel.savedSearchId,
                onNavigateBack = {
                    if (forbidExit) {
                        viewModel.showUnsavedConfirmationDialog()
                    } else {
                        onNavigateBack()
                    }
                },
            )
        },
        bottomBar = {
            ProductNavigationBarSpacer()
        },
        floatingActionButton = {
            NewOrEditSavedSearchFab(
                state = viewModel.fabState,
                onSubmitFabClick = viewModel::submit,
            )
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(vertical = 16.dp)
        ) {
            OutlinedTextField(
                enabled = viewModel.fabState != FabState.LOADING,
                singleLine = true,
                value = viewModel.queryTextField,
                onValueChange = viewModel::onQueryTextFieldChange,
                label = {
                    Text(text = "Query")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    autoCorrectEnabled = false,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(queryFocusRequester),
            )

            OutlinedTextField(
                enabled = viewModel.fabState != FabState.LOADING,
                value = viewModel.labelsTextField,
                onValueChange = viewModel::onLabelsTextFieldChange,
                singleLine = true,
                label = {
                    Text(text = "Labels")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    autoCorrectEnabled = false,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(labelsFocusRequester),
            )

            LaunchedEffect(key1 = Unit) {
                if (viewModel.shouldFocusToLabelsTextField) {
                    labelsFocusRequester.requestFocus()
                } else {
                    queryFocusRequester.requestFocus()
                }
            }
        }
    }
}
