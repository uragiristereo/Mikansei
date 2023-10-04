package com.uragiristereo.mikansei.core.product.shared.postfavoritevote.core

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun ScoreChips(
    score: Int,
    state: PostVote.Status,
    enabled: Boolean,
    onUpvoteClick: () -> Unit,
    onDownvoteClick: () -> Unit,
    onUnvoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(shape = RoundedCornerShape(percent = 50))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = when (state) {
                        PostVote.Status.NONE -> MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
                        else -> Color.Transparent
                    },
                ),
                shape = RoundedCornerShape(percent = 50),
            )
            .background(
                color = when (state) {
                    PostVote.Status.UPVOTED -> MaterialTheme.colors.primary
                    PostVote.Status.DOWNVOTED -> MaterialTheme.colors.error
                    PostVote.Status.NONE -> MaterialTheme.colors.background.backgroundElevation(0.dp)
                }.copy(
                    alpha = when {
                        enabled -> ContentAlpha.high
                        else -> ContentAlpha.disabled
                    },
                )
            )
            .clickable(
                onClick = {
                    when (state) {
                        PostVote.Status.NONE -> onUpvoteClick()
                        else -> onUnvoteClick()
                    }
                },
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled,
            )
            .padding(
                vertical = 6.dp,
                horizontal = 10.dp,
            ),
    ) {
        val contentColor = when (state) {
            PostVote.Status.NONE -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.background
        }.copy(
            alpha = when {
                enabled -> ContentAlpha.high
                else -> ContentAlpha.disabled
            },
        )

        Icon(
            painter = painterResource(id = R.drawable.expand_less),
            contentDescription = null,
            tint = contentColor.copy(
                alpha = when {
                    state == PostVote.Status.DOWNVOTED -> ContentAlpha.disabled
                    enabled -> ContentAlpha.high
                    else -> ContentAlpha.disabled
                },
            ),
            modifier = Modifier.size(20.dp),
        )

        Text(
            text = "$score",
            color = contentColor,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.animateContentSize(),
        )

        Divider(
            color = contentColor,
            modifier = Modifier.size(width = 1.dp, height = 16.dp),
        )

        Icon(
            painter = painterResource(id = R.drawable.expand_more),
            contentDescription = null,
            tint = contentColor.copy(
                alpha = when {
                    state == PostVote.Status.UPVOTED -> ContentAlpha.disabled
                    enabled -> ContentAlpha.high
                    else -> ContentAlpha.disabled
                },
            ),
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    onClick = {
                        when (state) {
                            PostVote.Status.NONE -> onDownvoteClick()
                            else -> onUnvoteClick()
                        }
                    },
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                ),
        )
    }
}
