package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.column

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun FavoriteGroupItem(
    item: FavoriteGroup,
    onClick: (FavoriteGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    onClick(item)
                },
                enabled = !item.isPostAlreadyExits,
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.circle_fill),
            contentDescription = null,
            tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
            modifier = Modifier
                .padding(end = 32.dp)
                .size(18.dp),
        )

        Column {
            Text(
                text = item.name,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
            )

            if (item.isPostAlreadyExits) {
                Text(
                    text = "Post is already exist in this favorite group",
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}
