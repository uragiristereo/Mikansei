package com.uragiristereo.mikansei.feature.home.favorites.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.product.component.ProductPostPlaceholder
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun FavoriteItem(
    item: Favorite,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(size = 8.dp))
                .let {
                    if (item.id == 0) {
                        it.clickable(
                            onClick = onClick,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.Black),
                        )
                    } else {
                        it.combinedClickable(
                            onClick = onClick,
                            onLongClick = onLongClick,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.Black),
                        )
                    }
                },
        ) {
            ProductPostPlaceholder()

            AsyncImage(
                model = remember(item.thumbnailUrl) {
                    ImageRequest.Builder(context)
                        .data(item.thumbnailUrl)
                        .crossfade(durationMillis = 170)
                        .build()
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Text(
            text = when (item.id) {
                0 -> stringResource(id = R.string.my_favorites)
                else -> item.name
            },
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                )
                .padding(top = 4.dp),
        )
    }
}
