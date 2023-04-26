package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.uragiristereo.mikansei.core.ui.composable.SectionTitle
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.column.FavoriteGroupItem
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core.CenterText
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core.CreateNewFavGroupButton
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AddToFavGroupDialog(
    onDismiss: () -> Unit,
    onNewFavoriteGroupClick: (postId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddToFavGroupViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Column(
            modifier = modifier
                .sizeIn(
                    maxWidth = 540.dp,
                    maxHeight = screenHeight * 0.75f,
                )
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(size = 8.dp))
                .background(MaterialTheme.colors.background.backgroundElevation())
                .padding(vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                SectionTitle(text = "Add to Favorite Group")

                IconButton(
                    onClick = viewModel::refreshFavoriteGroups,
                    content = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            tint = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                            contentDescription = null,
                        )
                    },
                )
            }

            Divider()

            LazyColumn {
                if (viewModel.isLoading) {
                    item {
                        CenterText(text = "...")
                    }
                }

                if (!viewModel.isLoading && viewModel.items.isNotEmpty()) {
                    items(viewModel.items) { item ->
                        FavoriteGroupItem(
                            item = item,
                            isRemoving = viewModel.isRemoving,
                            onClick = {
                                viewModel.addPostToFavoriteGroup(context, item)

                                onDismiss()
                            },
                            onRemoveClick = viewModel::removePostFromFavoriteGroup,
                        )
                    }
                }

                if (!viewModel.isLoading && viewModel.items.isEmpty()) {
                    item {
                        CenterText(text = "You don't have any Favorite Group")
                    }
                }

                if (!viewModel.isLoading) {
                    item {
                        CreateNewFavGroupButton(
                            postId = viewModel.post.id,
                            enabled = !viewModel.isRemoving,
                            onClick = onNewFavoriteGroupClick,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }
}
