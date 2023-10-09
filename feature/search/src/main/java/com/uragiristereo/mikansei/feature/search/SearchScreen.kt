package com.uragiristereo.mikansei.feature.search

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.search.core.SearchBar
import com.uragiristereo.mikansei.feature.search.core.SearchBrowseButton
import com.uragiristereo.mikansei.feature.search.quick_shortcut_bar.SearchQuickShortcutBar
import com.uragiristereo.mikansei.feature.search.result.SearchResultItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun SearchScreen(
    onNavigate: (MainRoute) -> Unit,
    onNavigateBack: () -> Unit,
    onSearchSubmit: (String) -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val columnState = rememberLazyListState()

    val focusRequester = remember { FocusRequester() }
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }

    DisposableEffect(key1 = viewModel) {
        when {
            query.text.isEmpty() -> {
                query = TextFieldValue(
                    text = viewModel.tags,
                    selection = TextRange(viewModel.tags.length),
                )
                viewModel.parsedQuery = viewModel.tags
            }

            else -> viewModel.searchTerm(
                text = query.text,
                cursorIndex = query.selection.end,
            )
        }

        onDispose {
            viewModel.cancelSearch()
        }
    }

    LaunchedEffect(key1 = viewModel.loading) {
        if (!viewModel.loading) {
            viewModel.boldWord = viewModel.searchWord.word

            columnState.scrollToItem(index = 0)
        }
    }

    LaunchedEffect(key1 = query.text) {
        viewModel.parseQuery(query.text)

        viewModel.searchTerm(
            text = query.text,
            cursorIndex = query.selection.end,
        )
    }

    LaunchedEffect(key1 = columnState.isScrollInProgress) {
        if (columnState.isScrollInProgress && viewModel.searches.isNotEmpty()) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(key1 = viewModel.errorMessage) {
        if (viewModel.errorMessage != null) {
            Toast.makeText(
                /* context = */ context,
                /* text = */ viewModel.errorMessage,
                /* duration = */ Toast.LENGTH_LONG,
            ).show()

            viewModel.errorMessage = null
        }
    }

    SetSystemBarsColors(Color.Transparent)

    Scaffold(
        topBar = {
            ProductStatusBarSpacer {
                SearchBar(
                    query = query,
                    placeholder = stringResource(id = R.string.field_placeholder_example),
                    actionsRowExpanded = viewModel.actionsRowExpanded,
                    loading = viewModel.loading,
                    focusRequester = focusRequester,
                    onNavigateBack = onNavigateBack,
                    onQueryChange = {
                        query = it
                    },
                    onQuerySubmit = {
                        onSearchSubmit(viewModel.parsedQuery)
                    },
                    onClearClick = {
                        query = TextFieldValue()
                    },
                    onActionsRowExpandedChange = viewModel::onActionsRowExpandedChange,
                )
            }
        },
        bottomBar = {
            Column {
                SearchQuickShortcutBar(
                    query = query,
                    onQueryChange = {
                        query = it
                    },
                )

                ProductNavigationBarSpacer(
                    color = MaterialTheme.colors.background.backgroundElevation(),
                    modifier = Modifier.imePadding(),
                )
            }
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal)),
    ) { innerPadding ->
        LazyColumn(
            state = columnState,
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                SearchBrowseButton(
                    text = viewModel.parsedQuery,
                    onClick = {
                        onSearchSubmit(viewModel.parsedQuery)
                    },
                )
            }

            item {
                Divider()
            }

            if (viewModel.searchWord.word.isNotEmpty()) {
                items(
                    items = viewModel.searches,
                    key = { it.toString() },
                ) { item ->
                    SearchResultItem(
                        tag = item,
                        delimiter = viewModel.delimiter,
                        boldWord = viewModel.boldWord,
                        onClick = remember {
                            {
                                viewModel.searchAllowed = false

                                val result = query.text.replaceRange(
                                    startIndex = viewModel.searchWord.startIndex,
                                    endIndex = viewModel.searchWord.endIndex,
                                    replacement = "${viewModel.delimiter}${item.name} ",
                                )

                                val newQuery = "$result ".replace(regex = "\\s+".toRegex(), replacement = " ")

                                query = TextFieldValue(
                                    text = newQuery,
                                    selection = TextRange(index = newQuery.length)
                                )

                                viewModel.searchAllowed = true

                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                        },
                        onLongClick = { /*TODO*/ },
                    )
                }
            }
        }
    }

    DisposableEffect(key1 = viewModel) {
        scope.launch {
            delay(timeMillis = 200L)
            focusRequester.requestFocus()
            keyboardController?.show()
        }

        onDispose {
            keyboardController?.hide()
        }
    }
}
