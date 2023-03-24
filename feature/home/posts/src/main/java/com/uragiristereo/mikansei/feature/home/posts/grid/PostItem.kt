package com.uragiristereo.mikansei.feature.home.posts.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.post.Post

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PostItem(
    post: Post,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .aspectRatio(post.aspectRatio)
            .clip(RoundedCornerShape(size = 4.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress,
            ),
    ) {
        PostPlaceholder()

        SubcomposeAsyncImage(
            model = remember {
                ImageRequest.Builder(context)
                    .data(post.previewImage.url)
                    .crossfade(durationMillis = 170)
                    .size(
                        width = post.previewImage.width,
                        height = post.previewImage.height,
                    )
                    .build()
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = {
                PostPlaceholder()
            },
            error = {
                PostPlaceholder()
            },
            modifier = Modifier.fillMaxSize(),
        )

        val fileType = remember(post) { post.image.fileType }

        if (fileType in Constants.SUPPORTED_TYPES_ANIMATED) {
            PostLabel(
                fileType = fileType,
                modifier = Modifier.align(Alignment.BottomStart),
            )
        }
    }
}
