package com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.composable.Scaffold2
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit.core.EditFavGroupTopAppBar
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit.core.UnsavedConfirmationDialog
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.FabState
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.NewFavGroupFab
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditFavGroupScreen(
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: EditFavGroupViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            launch(SupervisorJob()) {
                when (event) {
                    is EditFavGroupViewModel.Event.OnSuccess -> {
                        onNavigateBack()
                        onSuccess()
                        scaffoldState.snackbarHostState.showSnackbar(message = context.getString(R.string.edit_favorite_group_success))
                    }

                    is EditFavGroupViewModel.Event.OnFailed -> {
                        scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                    }
                }
            }
        }
    }

    BackHandler(enabled = viewModel.isNameEdited || viewModel.arePostIdsEdited) {
        viewModel.showUnsavedConfirmationDialog()
    }

    UnsavedConfirmationDialog(
        visible = viewModel.unsavedConfirmationDialogVisible,
        onDiscard = onNavigateBack,
        onHide = viewModel::hideUnsavedConfirmationDialog,
    )

    Scaffold2(
        scaffoldState = scaffoldState,
        topBar = {
            ProductStatusBarSpacer {
                EditFavGroupTopAppBar(
                    favoriteGroupId = viewModel.favoriteGroup.id,
                    isUndoEnabled = viewModel.isNameEdited || viewModel.arePostIdsEdited,
                    onNavigateBack = {
                        if (viewModel.isNameEdited || viewModel.arePostIdsEdited) {
                            viewModel.showUnsavedConfirmationDialog()
                        } else {
                            onNavigateBack()
                        }
                    },
                    onUndoClick = viewModel::onUndoClick,
                )
            }
        },
        floatingActionButton = {
            NewFavGroupFab(
                state = viewModel.fabState,
                onSubmitFabClick = viewModel::editFavoriteGroup,
            )
        },
        bottomBar = {
            ProductNavigationBarSpacer()
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
            // Name TextField
            OutlinedTextField(
                enabled = viewModel.fabState != FabState.LOADING,
                singleLine = true,
                value = viewModel.nameTextField,
                onValueChange = viewModel::onNameTextFieldChange,
                label = {
                    Text(text = stringResource(id = R.string.name))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
            )

            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }

            // Post IDs TextField
            OutlinedTextField(
                enabled = viewModel.fabState != FabState.LOADING,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = viewModel.postIdsTextField,
                onValueChange = viewModel::onPostIdsTextFieldChange,
                label = {
                    Text(text = stringResource(id = R.string.post_ids))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
