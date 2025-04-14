package com.uragiristereo.mikansei.feature.wiki.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun WikiTopAppBar(
    wikiName: String,
    hasPosts: Boolean,
    onNavigateBack: () -> Unit,
    onSearchClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onOpenExternallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    ProductStatusBarSpacer {
        ProductTopAppBar(
            title = {
                Column {
                    Text(text = "Wiki")

                    Text(
                        text = wikiName,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
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
            },
            actions = {
                IconButton(
                    onClick = onSearchClick,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = null,
                        )
                    },
                )

                IconButton(
                    onClick = {
                        dropdownExpanded = !dropdownExpanded
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.more_vert),
                            contentDescription = null,
                        )
                    },
                )

                WikiTopAppBarDropdownMenu(
                    expanded = dropdownExpanded,
                    hasPosts = hasPosts,
                    onDismiss = { dropdownExpanded = false },
                    onRefreshClick = onRefreshClick,
                    onBrowseClick = onBrowseClick,
                    onOpenExternallyClick = onOpenExternallyClick,
                )
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun WikiTopAppBarDropdownMenu(
    expanded: Boolean,
    hasPosts: Boolean,
    onDismiss: () -> Unit,
    onRefreshClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onOpenExternallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier.background(MaterialTheme.colors.background.backgroundElevation()),
    ) {
        DropdownMenuItem(
            onClick = {
                onDismiss()
                onRefreshClick()
            },
            content = {
                Text(text = "Refresh")
            },
        )

        if (hasPosts) {
            DropdownMenuItem(
                onClick = {
                    onDismiss()
                    onBrowseClick()
                },
                content = {
                    Text(text = "Browse this tag")
                },
            )
        }

        Divider()

        DropdownMenuItem(
            onClick = {
                onDismiss()
                onOpenExternallyClick()
            },
            content = {
                Text(text = "Open externally")
            },
        )
    }
}
