package com.uragiristereo.mikansei.feature.image.more.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.core.ScoreChips
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.Chips
import com.uragiristereo.mikansei.core.ui.composable.SidesGradient
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
internal fun MoreActionsRow(
    showExpandButton: Boolean,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onExpandClick: () -> Unit,
    onOpenInExternalClick: () -> Unit,
    onAddToClick: () -> Unit,
    favoriteCount: Int,
    isOnFavorite: Boolean,
    onToggleFavorite: (Boolean) -> Unit,
    favoriteButtonEnabled: Boolean,
    score: Int,
    scoreState: PostVote.Status,
    voteButtonEnabled: Boolean,
    onVoteChange: (PostVote.Status) -> Unit,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier) {
        val (boxRef, rowRef) = createRefs()

        LazyRow(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
            ),
            modifier = Modifier
                .constrainAs(rowRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth(),
        ) {
            item {
                Chips(
                    selected = isOnFavorite,
                    text = "$favoriteCount",
                    enabled = favoriteButtonEnabled,
                    selectedIcon = painterResource(id = R.drawable.favorite_fill),
                    unselectedIcon = painterResource(id = R.drawable.favorite),
                    elevation = 0.dp,
                    onSelectedChange = onToggleFavorite,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            item {
                ScoreChips(
                    score = score,
                    state = scoreState,
                    enabled = voteButtonEnabled,
                    onUpvoteClick = {
                        onVoteChange(PostVote.Status.UPVOTED)
                    },
                    onDownvoteClick = {
                        onVoteChange(PostVote.Status.DOWNVOTED)
                    },
                    onUnvoteClick = {
                        onVoteChange(PostVote.Status.NONE)
                    },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            item {
                Chips(
                    text = stringResource(id = R.string.add_to_action),
                    icon = painterResource(id = R.drawable.add_to_photos),
                    elevation = 0.dp,
                    onClick = onAddToClick,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            item {
                Chips(
                    text = stringResource(id = R.string.download_action),
                    icon = painterResource(id = R.drawable.download),
                    elevation = 0.dp,
                    onClick = onDownloadClick,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            item {
                Chips(
                    text = stringResource(id = R.string.share_action),
                    icon = painterResource(id = R.drawable.share),
                    elevation = 0.dp,
                    onClick = onShareClick,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            if (showExpandButton) {
                item {
                    Chips(
                        text = stringResource(id = R.string.show_original_action),
                        icon = painterResource(id = R.drawable.open_in_full),
                        elevation = 0.dp,
                        onClick = onExpandClick,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }

            item {
                Chips(
                    text = stringResource(id = R.string.open_externally_action),
                    icon = painterResource(id = R.drawable.open_in_browser),
                    elevation = 0.dp,
                    onClick = onOpenInExternalClick,
                )
            }
        }

        SidesGradient(
            color = MaterialTheme.colors.background.backgroundElevation(),
            modifier = Modifier
                .constrainAs(boxRef) {
                    height = Dimension.fillToConstraints
                    width = Dimension.matchParent

                    top.linkTo(rowRef.top)
                    bottom.linkTo(rowRef.bottom)
                },
        )
    }
}
