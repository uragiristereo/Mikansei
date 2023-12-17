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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.composable.Scaffold2
import com.uragiristereo.mikansei.core.ui.composable.SettingTip
import com.uragiristereo.mikansei.core.ui.extension.forEach
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.NewFavGroupFab
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.NewFavGroupState
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.NewFavGroupTopAppBar
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewFavGroupScreen(
    onNavigateBack: () -> Unit,
    viewModel: NewFavGroupViewModel = koinViewModel(),
) {
    val scaffoldState = LocalScaffoldState.current
    val scope = rememberCoroutineScope()

    var textField by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.forEach { state ->
            when (state) {
                NewFavGroupState.Success -> {
                    scope.launch(SupervisorJob()) {
                        onNavigateBack()

                        scaffoldState.snackbarHostState.showSnackbar(message = "Favorite group is successfully created")
                    }
                }

                is NewFavGroupState.Failed -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message = state.message)
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
                isLoading = viewModel.isLoading,
                onSubmitFabClick = {
                    when {
                        textField.text.isBlank() -> {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(message = "Please enter a name!")
                            }
                        }

                        else -> viewModel.createNewFavoriteGroup(name = textField.text)
                    }
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
                SettingTip(text = "Post #${viewModel.postId} will be added to this new favorite group.")

                Divider()
            }

            OutlinedTextField(
                value = textField,
                onValueChange = {
                    if (!viewModel.isLoading) {
                        textField = it
                    }
                },
                label = {
                    Text(text = "Name")
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
