package com.uragiristereo.mikansei.feature.home.posts.post_dialog.core

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.core.ScoreState
import com.uragiristereo.mikansei.core.resources.R

@Composable
internal fun ScoreSection(
    score: Int,
    state: ScoreState,
    enabled: Boolean,
    onVoteChange: (ScoreState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    when (state) {
                        ScoreState.NONE -> onVoteChange(ScoreState.UPVOTED)
                        else -> onVoteChange(ScoreState.NONE)
                    }
                },
                enabled = enabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
            )
            .padding(
                horizontal = 4.dp,
                vertical = 0.dp,
            ),
    ) {
        IconButton(
            enabled = enabled,
            interactionSource = when (state) {
                ScoreState.NONE -> interactionSource
                else -> remember { MutableInteractionSource() }
            },
            onClick = {
                when (state) {
                    ScoreState.NONE -> onVoteChange(ScoreState.UPVOTED)
                    else -> onVoteChange(ScoreState.NONE)
                }
            },
            modifier = Modifier.padding(end = 20.dp),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.expand_less),
                    contentDescription = null,
                    tint = when {
                        !enabled -> LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
                        state == ScoreState.UPVOTED -> MaterialTheme.colors.primary
                        state == ScoreState.DOWNVOTED -> LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                        else -> LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    },
                )
            },
        )

        Row(
            modifier = Modifier.weight(1f),
        ) {
            val textColor = when {
                !enabled -> LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
                state == ScoreState.UPVOTED -> MaterialTheme.colors.primary
                state == ScoreState.DOWNVOTED -> MaterialTheme.colors.error
                else -> LocalContentColor.current.copy(alpha = ContentAlpha.high)
            }

            Text(
                text = when (state) {
                    ScoreState.UPVOTED -> "Upvoted"
                    ScoreState.DOWNVOTED -> "Downvoted"
                    else -> "Upvote"
                },
                style = MaterialTheme.typography.body1,
                color = textColor,
                modifier = Modifier.animateContentSize(),
            )

            Text(
                text = " ($score)",
                style = MaterialTheme.typography.body1,
                color = textColor,
            )
        }

        Divider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(
                    width = 1.dp,
                    height = 24.dp,
                ),
        )

        IconButton(
            enabled = enabled,
            onClick = {
                when (state) {
                    ScoreState.NONE -> onVoteChange(ScoreState.DOWNVOTED)
                    else -> onVoteChange(ScoreState.NONE)
                }
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.expand_more),
                    contentDescription = null,
                    tint = when {
                        !enabled -> LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
                        state == ScoreState.DOWNVOTED -> MaterialTheme.colors.error
                        state == ScoreState.UPVOTED -> LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                        else -> LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    },
                )
            },
        )
    }
}
