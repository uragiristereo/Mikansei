package com.uragiristereo.mikansei.core.product.component

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
) {
    PullRefreshIndicator(
        refreshing = refreshing,
        state = state,
        backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
        contentColor = MaterialTheme.colors.primary,
        modifier = modifier,
    )
}
