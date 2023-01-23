package com.uragiristereo.mejiboard.feature.filters.appbars

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.product.component.ProductTopAppBar
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.core.ui.extension.backgroundElevation

@Composable
fun FiltersSelectionTopAppBar(
    title: String,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onSelectAll: () -> Unit,
    onSelectInverse: () -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    ProductTopAppBar(
        backgroundColor = MaterialTheme.colors.background.backgroundElevation(5.dp),
        title = {
            Text(text = title)
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
                            onDelete()
                        },
                        content = {
                            Text(text = stringResource(id = R.string.delete))
                        },
                    )

                    DropdownMenuItem(
                        onClick = {
                            dropdownExpanded = false
                            onSelectAll()
                        },
                        content = {
                            Text(text = stringResource(id = R.string.select_all))
                        },
                    )

                    DropdownMenuItem(
                        onClick = {
                            dropdownExpanded = false
                            onSelectInverse()
                        },
                        content = {
                            Text(text = stringResource(id = R.string.select_inverse))
                        },
                    )
                },
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClose,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = null,
                    )
                },
            )
        }
    )
}
