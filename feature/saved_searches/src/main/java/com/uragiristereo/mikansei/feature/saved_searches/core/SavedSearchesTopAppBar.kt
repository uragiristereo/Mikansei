package com.uragiristereo.mikansei.feature.saved_searches.core

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun SavedSearchesTopAppBar(
    onNavigateBack: () -> Unit,
    onRefreshClick: () -> Unit,
    onBrowseAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    ProductStatusBarSpacer {
        ProductTopAppBar(
            title = {
                Text(text = "Saved searches")
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
                    onClick = {
                        isDropdownExpanded = !isDropdownExpanded
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.more_vert),
                            contentDescription = null,
                        )
                    },
                )

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            isDropdownExpanded = false
                            onRefreshClick()
                        },
                        content = {
                            Text(text = "Refresh")
                        },
                    )

                    DropdownMenuItem(
                        onClick = {
                            isDropdownExpanded = false
                            onBrowseAllClick()
                        },
                        content = {
                            Text(text = "Browse all")
                        },
                    )
                }
            },
            modifier = modifier,
        )
    }
}
