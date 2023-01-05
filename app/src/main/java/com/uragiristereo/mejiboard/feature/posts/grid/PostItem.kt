package com.uragiristereo.mejiboard.feature.posts.grid

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
import com.uragiristereo.mejiboard.core.common.data.Constants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItem(
    post: com.uragiristereo.mejiboard.core.model.booru.post.Post,
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
//        var loading by rememberSaveable { mutableStateOf(true) }
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
//
//        AsyncImage(
//            model = remember {
//                ImageRequest.Builder(context)
//                    .data(post.previewImage.url)
//                    .listener(
//                        onStart = {
//                            loading = true
//                        },
//                        onSuccess = { _, _ ->
//                            loading = false
//                        },
//                    )
//                    .size(
//                        width = post.previewImage.width,
//                        height = post.previewImage.height,
//                    )
//                    .build()
//            },
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize(),
//        )
//
//        AnimatedVisibility(
//            visible = loading,
//            enter = fadeIn(),
//            exit = fadeOut(),
//            content = {
//                PostPlaceholder()
//            },
//        )

        val fileType = post.originalImage.fileType

        if (fileType in Constants.SUPPORTED_TYPES_ANIMATED) {
            PostLabel(
                fileType = fileType,
                modifier = Modifier.align(Alignment.BottomStart),
            )
        }
    }
}
