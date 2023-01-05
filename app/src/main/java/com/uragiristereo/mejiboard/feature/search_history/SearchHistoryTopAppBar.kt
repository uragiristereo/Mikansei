package com.uragiristereo.mejiboard.feature.search_history

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mejiboard.R

@Composable
fun SearchHistoryTopAppBar(
    onNavigateBack: () -> Unit,
    onMoreClick: () -> Unit,
) {
    com.uragiristereo.mejiboard.core.product.component.ProductTopAppBar(
        title = {
            Text(text = "Search history")
        },
        actions = {
            IconButton(
                onClick = onMoreClick,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vert),
                        contentDescription = null,
                    )
                },
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = null,
                    )
                },
            )
        }
    )
}
