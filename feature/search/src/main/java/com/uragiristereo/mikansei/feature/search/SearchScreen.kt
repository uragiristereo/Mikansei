package com.uragiristereo.mikansei.feature.search

import android.text.InputType
import android.util.TypedValue
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.core.view.setPadding
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.product.component.ProductNavigationBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.extension.copy
import com.uragiristereo.mikansei.core.ui.view.SelectionAwareEditText
import com.uragiristereo.mikansei.feature.search.browse_chips.BrowseChips
import com.uragiristereo.mikansei.feature.search.core.SearchBar
import com.uragiristereo.mikansei.feature.search.quick_shortcut_bar.SearchQuickShortcutBar
import com.uragiristereo.mikansei.feature.search.result.SearchResultItem
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(FlowPreview::class)
@Composable
internal fun SearchScreen(
    onNavigateBack: () -> Unit,
    onSearchSubmit: (String) -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = LocalScaffoldState.current
    val editText = remember(context) {
        SelectionAwareEditText(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            imeOptions = EditorInfo.IME_ACTION_SEARCH or EditorInfo.IME_FLAG_NO_FULLSCREEN
            inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            isSingleLine = true
            setBackgroundResource(android.R.color.transparent)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setPadding(0)
        }
    }

    val scope = rememberCoroutineScope()
    val columnState = rememberLazyListState()
    val browseChips by viewModel.browseChips.collectAsState()

    fun showKeyboard() {
        context.getSystemService<InputMethodManager>()?.showSoftInput(editText, 0)
    }

    fun hideKeyboard() {
        context.getSystemService<InputMethodManager>()
            ?.hideSoftInputFromWindow(editText.windowToken, 0)
        keyboardController?.hide()
    }

    LaunchedEffect(key1 = viewModel.loading) {
        if (!viewModel.loading) {
            viewModel.boldWord = viewModel.searchWord.word

            columnState.scrollToItem(index = 0)
        }
    }

    DisposableEffect(key1 = Unit) {
        val job = scope.launch {
            snapshotFlow { viewModel.query }
                .debounce(timeoutMillis = 250L)
                .distinctUntilChangedBy { it.text }
                .collect {
                    viewModel.searchTerm()
                }
        }

        onDispose {
            job.cancel()
        }
    }

    LaunchedEffect(key1 = columnState.isScrollInProgress) {
        if (columnState.isScrollInProgress && viewModel.searches.isNotEmpty()) {
            hideKeyboard()
        }
    }

    LaunchedEffect(key1 = viewModel.errorMessage) {
        viewModel.errorMessage?.let { errorMessage ->
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessage)

                viewModel.errorMessage = null
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ProductStatusBarSpacer {
                SearchBar(
                    editText = editText,
                    query = viewModel.query,
                    placeholder = stringResource(id = R.string.field_placeholder_example),
                    loading = viewModel.loading,
                    onNavigateBack = {
                        hideKeyboard()
                        onNavigateBack()
                    },
                    onQueryChange = {
                        viewModel.query = it.copy(text = it.text.lowercase())
                    },
                    onQuerySubmit = {
                        hideKeyboard()
                        onSearchSubmit(viewModel.parsedQuery)
                    },
                    onClearClick = {
                        viewModel.query = TextFieldValue()
                    },
                )
            }
        },
        bottomBar = {
            Column {
                SearchQuickShortcutBar(
                    query = viewModel.query,
                    onQueryChange = {
                        viewModel.query = it
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
        val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()

        LazyColumn(
            state = columnState,
            contentPadding = innerPadding.copy(
                bottom = navigationBarsPadding.calculateBottomPadding() + 36.dp + 8.dp,
            ),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                BrowseChips(
                    chips = browseChips,
                    onClick = {
                        hideKeyboard()
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

                                val result = viewModel.query.text.replaceRange(
                                    startIndex = viewModel.searchWord.startIndex,
                                    endIndex = viewModel.searchWord.endIndex,
                                    replacement = "${viewModel.delimiter}${item.name} ",
                                )

                                val newQuery = "$result "
                                    .replace("\\s+".toRegex(), " ")
                                    .lowercase()

                                viewModel.query = TextFieldValue(
                                    text = newQuery,
                                    selection = TextRange(index = newQuery.length)
                                )

                                viewModel.searchAllowed = true
                                viewModel.searchTerm()

                                editText.requestFocus()
                                showKeyboard()
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
            editText.requestFocus()
            showKeyboard()
        }

        onDispose {
            editText.clearFocus()
        }
    }
}
