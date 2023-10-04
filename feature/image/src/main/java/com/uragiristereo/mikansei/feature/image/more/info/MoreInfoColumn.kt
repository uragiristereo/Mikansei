package com.uragiristereo.mikansei.feature.image.more.info

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
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.Rating
import com.uragiristereo.mikansei.core.resources.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun MoreInfoColumn(
    post: Post,
    scaledImageFileSizeStr: String,
    originalImageFileSizeStr: String,
    expanded: Boolean,
    uploaderName: String,
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
            title = stringResource(id = R.string.image_rating),
            subtitle = when (post.rating) {
                Rating.GENERAL -> stringResource(id = R.string.image_rating_general)
                Rating.SENSITIVE -> stringResource(id = R.string.image_rating_sensitive)
                Rating.QUESTIONABLE -> stringResource(id = R.string.image_rating_questionable)
                Rating.EXPLICIT -> stringResource(id = R.string.image_rating_explicit)
            },
            modifier = Modifier.padding(bottom = 8.dp),
        )

        MoreInfo(
            title = stringResource(id = R.string.image_uploaded_by),
            subtitle = uploaderName,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)) with
                        fadeOut(animationSpec = tween(durationMillis = 0))
            },
            label = "",
            content = { stateExpanded ->
                when {
                    stateExpanded -> {
                        Column {
                            post.medias.scaled?.let {
                                it.apply {
                                    MoreInfo(
                                        title = stringResource(id = R.string.image_compressed),
                                        subtitle = "$width x $height\n$scaledImageFileSizeStr・$fileType",
                                        modifier = Modifier.padding(bottom = 8.dp),
                                    )
                                }
                            }

                            post.medias.original.apply {
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
