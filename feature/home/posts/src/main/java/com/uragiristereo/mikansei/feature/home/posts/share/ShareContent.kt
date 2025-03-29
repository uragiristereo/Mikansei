package com.uragiristereo.mikansei.feature.home.posts.share

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.composable.PostHeader
import com.uragiristereo.mikansei.core.ui.extension.copyToClipboard
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.bottomSheetContentPadding
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShareContent(
    onDismiss: suspend () -> Unit,
    onPostClick: (Post) -> Unit,
    onShareClick: (post: Post, option: ShareOption) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShareViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val post = viewModel.navArgs.post
    val showThumbnail = viewModel.navArgs.showThumbnail
    val source = post.source

    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .bottomSheetContentPadding()
            .padding(bottom = 8.dp),
    ) {
        PostHeader(
            title = stringResource(id = R.string.share_post),
            subtitle = when {
                showThumbnail -> "#${post.id}"
                else -> null
            },
            previewUrl = when {
                showThumbnail -> post.medias.preview.url
                else -> null
            },
            aspectRatio = when {
                showThumbnail -> post.aspectRatio
                else -> null
            },
            onClick = when {
                showThumbnail -> {
                    {
                        onPostClick(post)
                    }
                }

                else -> null
            },
        )

        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        ClickableSection(
            title = stringResource(id = R.string.post_link),
            subtitle = viewModel.postLink,
            icon = painterResource(id = R.drawable.link),
            onClick = {
                scope.launch {
                    onDismiss()

                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, viewModel.postLink)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(intent, null)
                    context.startActivity(shareIntent)
                }
            },
            onLongClick = {
                context.copyToClipboard(
                    text = viewModel.postLink,
                    message = "Post link copied to clipboard!",
                )
            },
        )

        if (source != null) {
            val isSourceLink = source.startsWith("http")

            ClickableSection(
                title = when {
                    isSourceLink -> "Source link"
                    else -> "Source"
                },
                subtitle = source,
                icon = painterResource(
                    id = when {
                        isSourceLink -> R.drawable.link
                        else -> R.drawable.description
                    },
                ),
                onClick = {
                    scope.launch {
                        onDismiss()

                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, source)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(intent, null)
                        context.startActivity(shareIntent)
                    }
                },
                onLongClick = {
                    context.copyToClipboard(
                        text = source,
                        message = when {
                            isSourceLink -> "Source link copied to clipboard!"
                            else -> "Source copied to clipboard!"
                        },
                    )
                },
            )
        }

        post.medias.scaled?.let {
            it.apply {
                ClickableSection(
                    title = stringResource(id = R.string.compressed_image),
                    subtitle = "$width x $height・${viewModel.scaledImageFileSizeStr}・$fileType",
                    icon = painterResource(id = R.drawable.aspect_ratio),
                    onClick = {
                        scope.launch {
                            onDismiss()
                            onShareClick(post, ShareOption.COMPRESSED)
                        }
                    },
                )
            }
        }

        if (post.type != Post.Type.UGOIRA) {
            post.medias.original.apply {
                ClickableSection(
                    title = stringResource(id = R.string.full_size_image),
                    subtitle = "$width x $height・${viewModel.originalImageFileSizeStr}・$fileType",
                    icon = painterResource(id = R.drawable.open_in_full),
                    onClick = {
                        scope.launch {
                            onDismiss()
                            onShareClick(post, ShareOption.ORIGINAL)
                        }
                    },
                )
            }
        }
    }
}
