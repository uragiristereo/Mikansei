package com.uragiristereo.mejiboard.feature.search_history

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.uragiristereo.mejiboard.core.ui.extension.defaultPaddings

@Composable
fun SearchHistoryScreen(
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
