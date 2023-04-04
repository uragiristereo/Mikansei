package com.uragiristereo.mikansei.feature.home.posts.post_dialog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.product.component.ProductDialog
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.rememberWindowSize

@Composable
internal fun PostDialog(
    post: Post,
    onDismiss: () -> Unit,
    onPostClick: () -> Unit,
    onDowloadClick: () -> Unit,
    onShareClick: () -> Unit,
//    onAddToClick: () -> Unit,
//    onBlockTagsClick: () -> Unit,
//    onHidePostClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    val windowSize = rememberWindowSize()

    var columnHeight by remember { mutableStateOf(0.dp) }

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
                        onClick = onPostClick,
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
                                onClick = onPostClick,
                            )
                        }
                    }

                    item {
                        ClickableSection(
                            title = stringResource(id = R.string.download_action),
                            onClick = onDowloadClick,
                            icon = painterResource(id = R.drawable.download),
                        )
                    }

                    item {
                        ClickableSection(
                            title = stringResource(id = R.string.share_action),
                            onClick = onShareClick,
                            icon = painterResource(id = R.drawable.share),
                        )
                    }

//                    item {
//                        ClickableSection(
//                            title = stringResource(id = R.string.add_to_action),
//                            onClick = onAddToClick,
//                            icon = painterResource(id = R.drawable.add_to_photos),
//                        )
//                    }
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
