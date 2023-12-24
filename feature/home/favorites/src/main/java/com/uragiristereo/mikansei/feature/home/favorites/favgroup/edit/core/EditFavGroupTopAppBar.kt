package com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit.core

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun EditFavGroupTopAppBar(
    favoriteGroupId: Int,
    isUndoEnabled: Boolean,
    onNavigateBack: () -> Unit,
    onUndoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProductTopAppBar(
        title = {
            Column {
                Text(text = stringResource(id = R.string.edit_favorite_group))

                Text(
                    text = favoriteGroupId.toString(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
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
                enabled = isUndoEnabled,
                onClick = onUndoClick,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.undo),
                        contentDescription = null,
                    )
                },
            )
        },
        modifier = modifier,
    )
}
