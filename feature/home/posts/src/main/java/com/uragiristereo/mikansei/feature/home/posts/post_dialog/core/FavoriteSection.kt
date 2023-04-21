package com.uragiristereo.mikansei.feature.home.posts.post_dialog.core

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun FavoriteSection(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    count: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val contentColor = when {
        checked && enabled -> MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
        enabled -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.high)
        else -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onCheckedChange(!checked)
                },
                enabled = enabled,
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
    ) {
        Icon(
            painter = painterResource(
                id = when {
                    checked -> R.drawable.favorite_fill
                    else -> R.drawable.favorite
                },
            ),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .padding(end = 32.dp)
                .size(24.dp),
        )

        Row {
            Text(
                text = when {
                    checked -> "Favorited"
                    else -> "Favorite"
                },
                style = MaterialTheme.typography.body1,
                color = contentColor,
                modifier = Modifier.animateContentSize(),
            )

            Text(
                text = " ($count)",
                style = MaterialTheme.typography.body1,
                color = contentColor,
            )
        }
    }
}
