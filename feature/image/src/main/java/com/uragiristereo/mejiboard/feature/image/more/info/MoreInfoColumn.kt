package com.uragiristereo.mejiboard.feature.image.more.info

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.model.booru.post.Rating
import com.uragiristereo.mejiboard.core.resources.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MoreInfoColumn(
    post: Post,
    scaledImageFileSizeStr: String,
    originalImageFileSizeStr: String,
    expanded: Boolean,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
        ),
    ) {
        MoreInfo(
            title = stringResource(id = R.string.image_source),
            subtitle = stringResource(id = post.source.nameResId),
            modifier = Modifier.padding(bottom = 8.dp),
        )

        MoreInfo(
            title = stringResource(id = R.string.image_rating),
            subtitle = when (post.rating) {
                Rating.GENERAL -> stringResource(id = R.string.image_rating_general)
                Rating.SENSITIVE -> stringResource(id = R.string.image_rating_sensitive)
                Rating.QUESTIONABLE -> stringResource(id = R.string.image_rating_questionable)
                Rating.EXPLICIT -> stringResource(id = R.string.image_rating_explicit)
            },
            modifier = Modifier.padding(bottom = 8.dp),
        )

        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)) with
                        fadeOut(animationSpec = tween(durationMillis = 0))
            },
            content = { state ->
                when {
                    state -> {
                        Column {
                            MoreInfo(
                                title = stringResource(id = R.string.image_uploaded_by),
                                subtitle = post.uploader,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )

                            if (post.scaled) {
                                post.scaledImage.apply {
                                    MoreInfo(
                                        title = stringResource(id = R.string.image_compressed),
                                        subtitle = "$width x $height\n$scaledImageFileSizeStr・$fileType",
                                        modifier = Modifier.padding(bottom = 8.dp),
                                    )
                                }
                            }

                            post.originalImage.apply {
                                MoreInfo(
                                    title = stringResource(id = R.string.image_original),
                                    subtitle = "$width x $height\n$originalImageFileSizeStr・$fileType",
                                    modifier = Modifier.padding(bottom = 16.dp),
                                )
                            }
                        }
                    }

                    else -> {
                        MoreInfoButton(
                            onClick = onMoreClick,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                }
            },
        )

    }
}
