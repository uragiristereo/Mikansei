package com.uragiristereo.mikansei.feature.filters.appbars

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun FiltersTopAppBar(
    onNavigateBack: () -> Unit,
    onSelectAll: () -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    ProductTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.filters_label))
        },
        actions = {
            IconButton(
                onClick = { dropdownExpanded = !dropdownExpanded },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vert),
                        contentDescription = null,
                    )
                },
            )

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = {
                    dropdownExpanded = false
                },
                content = {
                    DropdownMenuItem(
                        onClick = {
                            dropdownExpanded = false
                            onSelectAll()
                        },
                        content = {
                            Text(text = stringResource(id = R.string.select_all))
                        },
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
