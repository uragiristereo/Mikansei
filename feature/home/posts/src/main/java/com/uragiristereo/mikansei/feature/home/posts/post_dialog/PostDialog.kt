package com.uragiristereo.mikansei.feature.home.posts.post_dialog

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.model.user.isNotAnonymous
import com.uragiristereo.mikansei.core.product.component.ProductDialog
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.extension.forEach
import com.uragiristereo.mikansei.core.ui.rememberWindowSize
import com.uragiristereo.mikansei.feature.home.posts.post_dialog.core.FavoriteSection
import com.uragiristereo.mikansei.feature.home.posts.post_dialog.core.ScoreSection
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PostDialog(
    post: Post,
    onDismiss: () -> Unit,
    onPostClick: (Post) -> Unit,
    onDownloadClick: (Post) -> Unit,
    onShareClick: (Post) -> Unit,
    onAddToFavoriteGroupClick: (Post) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostDialogViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val windowSize = rememberWindowSize()

    var columnHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(key1 = viewModel) {
        viewModel.toastChannel.forEach { (message, duration) ->
            Toast.makeText(context, message, duration).show()
        }
    }

    ProductDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.widthIn(max = 540.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (windowSize != WindowSize.COMPACT) {
                    PostDialogHeaderResponsive(
                        post = post,
                        onClick = {
                            onDismiss()
                            onPostClick(post)
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(columnHeight),
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .onSizeChanged { size ->
                            columnHeight = with(density) { size.height.toDp() }
                        },
                ) {
                    if (windowSize == WindowSize.COMPACT) {
                        item {
                            PostDialogHeaderResponsive(
                                post = post,
                                onClick = {
                                    onDismiss()
                                    onPostClick(post)
                                },
                            )
                        }
                    }

                    item {
                        FavoriteSection(
                            checked = viewModel.isPostInFavorites,
                            onCheckedChange = viewModel::toggleFavorite,
                            count = viewModel.favoriteCount,
                            enabled = viewModel.favoriteButtonEnabled && viewModel.isPostUpdated,
                        )
                    }

                    item {
                        ScoreSection(
                            score = viewModel.score,
                            state = viewModel.scoreState,
                            enabled = viewModel.voteButtonEnabled && viewModel.isPostUpdated,
                            onVoteChange = viewModel::onVoteChange,
                        )
                    }

                    item {
                        ClickableSection(
                            title = stringResource(id = R.string.add_to_action),
                            onClick = {
                                if (viewModel.activeUser.isNotAnonymous()) {
                                    onDismiss()
                                    onAddToFavoriteGroupClick(post)
                                } else {
                                    Toast.makeText(context, "Please Login to use this feature!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            icon = painterResource(id = R.drawable.add_to_photos),
                        )
                    }

                    item {
                        Divider()
                    }

                    item {
                        ClickableSection(
                            title = stringResource(id = R.string.download_action),
                            onClick = {
                                onDismiss()
                                onDownloadClick(post)
                            },
                            icon = painterResource(id = R.drawable.download),
                        )
                    }

                    item {
                        ClickableSection(
                            title = stringResource(id = R.string.share_action),
                            onClick = {
                                onDismiss()
                                onShareClick(post)
                            },
                            icon = painterResource(id = R.drawable.share),
                        )
                    }
//
//                    item {
//                        Divider()
//                    }
//
//                    item {
//                        ClickableSection(
//                            title = stringResource(id = R.string.block_tags_post_action),
//                            onClick = onBlockTagsClick,
//                            icon = painterResource(id = R.drawable.label_off),
//                        )
//                    }
//
//                    item {
//                        ClickableSection(
//                            title = stringResource(id = R.string.hide_post_action),
//                            onClick = onHidePostClick,
//                            icon = painterResource(id = R.drawable.visibility_off),
//                        )
//                    }
                }
            }
        }
    )
}
