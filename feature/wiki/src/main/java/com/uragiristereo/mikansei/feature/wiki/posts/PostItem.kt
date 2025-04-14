package com.uragiristereo.mikansei.feature.wiki.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.product.component.ProductPostPlaceholder
import com.uragiristereo.mikansei.core.ui.composable.PostLabel
import kotlin.math.roundToInt

@Composable
fun PostItem(
    post: Post,
    onClick: (Post) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val size = 160.dp
    val sizePx = density.run { 160.dp.toPx().roundToInt() }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = {
                    onClick(post)
                },
                interactionSource = null,
                indication = ripple(color = Color.Black),
            ),
    ) {
        ProductPostPlaceholder()

        AsyncImage(
            model = remember(post) {
                ImageRequest.Builder(context)
                    .data(post.medias.preview.url)
                    .crossfade(durationMillis = 170)
                    .size(sizePx)
                    .build()
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        PostLabel(
            post = post,
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}
