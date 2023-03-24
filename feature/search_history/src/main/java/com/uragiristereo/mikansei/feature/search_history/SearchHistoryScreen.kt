package com.uragiristereo.mikansei.feature.search_history

import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.uragiristereo.mikansei.core.ui.extension.defaultPaddings
import com.uragiristereo.mikansei.feature.search_history.core.SearchHistoryTopAppBar

@Composable
internal fun SearchHistoryScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    var checked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchHistoryTopAppBar(
                onNavigateBack = onNavigateBack,
                onMoreClick = {
                    // TODO
                },
            )
        },
        modifier = modifier.defaultPaddings(),
    ) { innerPadding ->
        SearchHistoryColumn(
            contentPadding = innerPadding,
            toggleChecked = checked,
            onToggleChecked = { checked = it },
        )
    }
}
