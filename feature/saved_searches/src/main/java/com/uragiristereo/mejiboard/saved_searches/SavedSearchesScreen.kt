package com.uragiristereo.mejiboard.saved_searches

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mejiboard.core.common.ui.extension.defaultPaddings
import com.uragiristereo.mejiboard.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mejiboard.saved_searches.appbars.SavedSearchesTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SavedSearchesScreen(
    modifier: Modifier = Modifier,
    viewModel: SavedSearchesViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
) {
    ProductSetSystemBarsColor()

    Scaffold(
        topBar = {
            SavedSearchesTopAppBar(
                onNavigateBack = onNavigateBack,
                onSelectAll = { },
            )
        },
        modifier = modifier.defaultPaddings(),
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
        ) {

        }
    }
}
