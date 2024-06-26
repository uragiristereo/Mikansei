package com.uragiristereo.mikansei.feature.image.more.info

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.Rating
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun MoreInfoColumn(
    post: Post,
    scaledImageFileSizeStr: String,
    originalImageFileSizeStr: String,
    expanded: Boolean,
    uploaderName: String,
    shouldShowRating: Boolean,
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
            title = stringResource(id = R.string.image_status),
            subtitle = stringResource(
                id = when (post.status) {
                    Post.Status.ACTIVE -> R.string.image_status_active
                    Post.Status.PENDING -> R.string.image_status_pending
                    Post.Status.DELETED -> R.string.image_status_deleted
                    Post.Status.BANNED -> R.string.image_status__banned
                },
            ),
            modifier = Modifier.padding(bottom = 8.dp),
        )

        if (shouldShowRating) {
            MoreInfo(
                title = stringResource(id = R.string.image_rating),
                subtitle = stringResource(
                    id = when (post.rating) {
                        Rating.GENERAL -> R.string.image_rating_general
                        Rating.SENSITIVE -> R.string.image_rating_sensitive
                        Rating.QUESTIONABLE -> R.string.image_rating_questionable
                        Rating.EXPLICIT -> R.string.image_rating_explicit
                    },
                ),
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        MoreInfo(
            title = stringResource(id = R.string.image_uploaded_by),
            subtitle = uploaderName,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                (fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = 90)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90))) togetherWith
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
