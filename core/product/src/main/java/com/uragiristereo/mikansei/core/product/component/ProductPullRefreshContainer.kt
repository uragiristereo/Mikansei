package com.uragiristereo.mikansei.core.product.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductPullRefreshContainer(
    refreshing: Boolean,
    state: PullRefreshState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.pullRefresh(state)) {
        content()

        ProductPullRefreshIndicator(
            refreshing = refreshing,
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(contentPadding),
        )
    }
}
