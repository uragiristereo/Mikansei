package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.column

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core.FavoriteGroup

@Composable
internal fun FavoriteGroupItem(
    item: FavoriteGroup,
    enabled: Boolean,
    onAddClick: (FavoriteGroup) -> Unit,
    onRemoveClick: (FavoriteGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled,
                onClick = {
                    if (!item.isPostAlreadyExits) {
                        onAddClick(item)
                    } else {
                        onRemoveClick(item)
                    }
                },
            )
            .padding(
                start = 8.dp,
                end = 16.dp,
            ),
    ) {
        Checkbox(
            enabled = enabled,
            checked = item.isPostAlreadyExits,
            interactionSource = interactionSource,
            onCheckedChange = {
                if (!item.isPostAlreadyExits) {
                    onAddClick(item)
                } else {
                    onRemoveClick(item)
                }
            },
            modifier = Modifier.padding(end = 16.dp),
        )

        Text(
            text = item.name,
            color = LocalContentColor.current.copy(
                alpha = when {
                    enabled -> ContentAlpha.high
                    else -> ContentAlpha.disabled
                },
            ),
            modifier = Modifier.weight(1f),
        )
    }
}
