package com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.LocalSnackbarHostState
import com.uragiristereo.mikansei.core.ui.composable.PostHeader
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto.column.FavoriteGroupsColumn
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto.core.CreateNewFavGroupButton
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddToFavGroupContent(
    onDismiss: suspend () -> Unit,
    onNewFavoriteGroupClick: (postId: Int) -> Unit,
    viewModel: AddToFavGroupViewModel = koinViewModel(),
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    val post = viewModel.post

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .padding(top = 16.dp),
    ) {
        PostHeader(
            title = stringResource(id = R.string.add_to_favorite_group),
            subtitle = "#${post.id}",
            previewUrl = post.medias.preview.url,
            aspectRatio = post.aspectRatio,
        )

        Divider()

        when {
            viewModel.isLoading && viewModel.items.isEmpty() -> {
                // Loading indicator
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    content = {
                        CircularProgressIndicator()
                    },
                )
            }

            viewModel.items.isEmpty() -> {
                // Item empty label
                Text(
                    text = stringResource(id = R.string.no_favorite_groups),
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 32.dp,
                            horizontal = 16.dp,
                        ),
                )
            }

            else -> {
                FavoriteGroupsColumn(
                    items = viewModel.items,
                    enabled = !viewModel.isLoading && !viewModel.isRemoving,
                    onAddClick = { item ->
                        scope.launch {
                            onDismiss()
                            viewModel.addPostToFavoriteGroup(
                                item = item,
                                onShowMessage = { message, duration ->
                                    snackbarHostState.showSnackbar(message = message, duration = duration)
                                }
                            )
                        }
                    },
                    onRemoveClick = viewModel::removePostFromFavoriteGroup,
                    modifier = Modifier.weight(1f, fill = false),
                )
            }
        }

        Divider()

        CreateNewFavGroupButton(
            postId = post.id,
            enabled = !viewModel.isRemoving,
            onClick = onNewFavoriteGroupClick,
        )
    }
}
