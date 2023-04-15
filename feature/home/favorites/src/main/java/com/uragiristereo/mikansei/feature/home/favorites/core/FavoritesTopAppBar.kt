package com.uragiristereo.mikansei.feature.home.favorites.core

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun FavoritesTopAppBar(
    activeUserName: String,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }

    ProductTopAppBar(
        title = {
            Column {
                Text(text = stringResource(id = R.string.favorites_label))

                Text(
                    text = activeUserName,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                )
            }
        },
        actions = {
            IconButton(
                onClick = { dropdownExpanded = true },
                content = {
                    Icon(
                        painterResource(id = R.drawable.more_vert),
                        contentDescription = null,
                    )
                },
            )

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        dropdownExpanded = false
                        onRefreshClick()
                    },
                    content = {
                        Text(text = stringResource(id = R.string.refresh))
                    },
                )
            }
        },
        modifier = modifier,
    )
}
