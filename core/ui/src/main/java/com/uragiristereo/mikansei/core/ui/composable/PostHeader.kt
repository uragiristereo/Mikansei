package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PostHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    previewUrl: String? = null,
    aspectRatio: Float? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .let {
                when {
                    onClick != null -> it.clickable(onClick = onClick)
                    else -> it
                }
            }
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
    ) {
        Column {
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary.copy(alpha = 0.87f),
                modifier = modifier,
            )

            // Subtitle
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                )
            }
        }

        // Thumbnail
        if (previewUrl != null && aspectRatio != null) {
            AsyncImage(
                model = previewUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(72.dp)
                    .aspectRatio(aspectRatio)
                    .clip(RoundedCornerShape(size = 4.dp)),
            )
        }
    }
}
