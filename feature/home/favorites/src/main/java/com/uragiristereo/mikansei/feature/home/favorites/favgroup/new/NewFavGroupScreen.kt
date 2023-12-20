package com.uragiristereo.mikansei.feature.home.favorites.favgroup.new

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
import androidx.compose.material.Divider
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
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.composable.Scaffold2
import com.uragiristereo.mikansei.core.ui.composable.SettingTip
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.FabState
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.NewFavGroupFab
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.NewFavGroupTopAppBar
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewFavGroupScreen(
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: NewFavGroupViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scaffoldState = LocalScaffoldState.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is NewFavGroupViewModel.Event.Success -> {
                    launch(SupervisorJob()) {
                        onNavigateBack()
                        onSuccess()
                        scaffoldState.snackbarHostState.showSnackbar(message = context.getString(R.string.create_favorite_group_success))
                    }
                }

                is NewFavGroupViewModel.Event.Failed -> {
                    launch(SupervisorJob()) {
                        scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                    }
                }
            }
        }
    }

    Scaffold2(
        scaffoldState = scaffoldState,
        topBar = {
            ProductStatusBarSpacer {
                NewFavGroupTopAppBar(onNavigateBack = onNavigateBack)
            }
        },
        floatingActionButton = {
            NewFavGroupFab(
                state = viewModel.fabState,
                onSubmitFabClick = {
                    viewModel.createNewFavoriteGroup()
                },
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
        Column(modifier = Modifier.padding(innerPadding)) {
            if (viewModel.postId != null) {
                SettingTip(
                    text = stringResource(
                        id = R.string.post_n_will_be_added_to_favorite_group, viewModel.postId.toString(),
                    ),
                )

                Divider()
            }

            OutlinedTextField(
                enabled = viewModel.fabState != FabState.LOADING,
                value = viewModel.textField,
                onValueChange = {
                    viewModel.textField = it
                },
                label = {
                    Text(text = stringResource(id = R.string.name))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
                    .focusRequester(focusRequester),
            )

            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}
