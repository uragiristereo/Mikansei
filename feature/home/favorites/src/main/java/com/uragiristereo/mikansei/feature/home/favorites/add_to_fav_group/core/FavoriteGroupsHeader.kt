package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
internal fun FavoriteGroupsHeader(
    postId: Int,
    previewUrl: String,
    aspectRatio: Float,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
    ) {
        Column {
            // Add to Favorite Group Label
            Text(
                text = "Add to Favorite Group",
                style = MaterialTheme.typography.subtitle1,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary.copy(alpha = 0.87f),
                modifier = modifier,
            )

            // Post ID Label
            Text(
                text = "#$postId",
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
            )
        }

        // Thumbnail
        AsyncImage(
            model = previewUrl,
            contentDescription = null,
            modifier = Modifier
                .height(72.dp)
                .aspectRatio(aspectRatio)
                .clip(RoundedCornerShape(size = 4.dp)),
        )
    }
}
