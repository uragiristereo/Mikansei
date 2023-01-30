package com.uragiristereo.mejiboard.feature.home.posts.post_dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.ui.WindowSize
import com.uragiristereo.mejiboard.core.ui.rememberWindowSize

@Composable
internal fun PostDialogHeaderResponsive(
    post: Post,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val windowSize = rememberWindowSize()

    @Composable
    fun PostThumbnail(modifier: Modifier = Modifier) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(post.previewImage.url)
                .crossfade(durationMillis = 170)
                .size(
                    width = post.previewImage.width,
                    height = post.previewImage.height,
                )
                .build(),
            contentDescription = "#${post.id}",
            contentScale = ContentScale.FillWidth,
            modifier = modifier,
        )
    }

    if (windowSize == WindowSize.COMPACT) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                ),
        ) {
            PostThumbnail(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(size = 4.dp))
                    .width(48.dp)
                    .heightIn(
                        min = 32.dp,
                        max = 48.dp * 2.5f,
                    ),
            )

            Text(
                text = "#${post.id}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .clickable(onClick = onClick)
                .padding(all = 16.dp),
        ) {
            Text(
                text = "#${post.id}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            PostThumbnail(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 4.dp))
                    .width(72.dp)
                    .heightIn(
                        min = 32.dp,
                        max = 72.dp * 2.5f,
                    ),
            )
        }
    }
}
