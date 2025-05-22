package com.uragiristereo.mikansei.feature.home.posts.grid

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uragiristereo.mikansei.core.model.danbooru.Post

@Composable
internal fun PostItem(
    post: Post,
    maxWidth: Int,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    PostBorder(
        status = post.status,
        relationshipType = post.relationshipType,
        modifier = modifier
            .aspectRatio(post.aspectRatio)
            .clip(RoundedCornerShape(size = 8.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress,
                interactionSource = null,
                indication = ripple(color = Color.Black),
            ),
    ) {
        PostPlaceholder()

        AsyncImage(
            model = remember {
                ImageRequest.Builder(context)
                    .data(post.medias.preview.url)
                    .size(
                        width = maxWidth,
                        height = (maxWidth * post.aspectRatio).toInt(),
                    )
                    .crossfade(durationMillis = 170)
                    .build()
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        if (post.type in arrayOf(Post.Type.ANIMATED_GIF, Post.Type.VIDEO, Post.Type.UGOIRA)) {
            PostLabel(
                fileType = when {
                    post.type == Post.Type.UGOIRA -> "ugoira"
                    else -> post.medias.original.fileType
                },
                hasSound = remember(post) {
                    "sound" in post.tags
                },
                modifier = Modifier.align(Alignment.BottomStart),
            )
        }
    }
}
